package me.erano.com.common.yaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Brings a user's yml up to date with the one a new version bundles: every key the bundled file has and the user's
 * doesn't is added, with its comments, next to the key it follows in the bundled file. Nothing else changes: the
 * user's values, comments, order and own keys stay as they are, line for line.
 *
 * <p>Works on the text, not on a parsed tree, so comments survive on every server (Bukkit only keeps them from
 * 1.18). Reads the block style yml plugins write: {@code key: value} mappings by indentation, comments, lists and
 * flow values ({@code [1, 2]}, {@code {a: 1}}) as values. A list is one value: its items are never merged.
 */
public final class YamlUpdate {

    /** {@code key:} at the start of a line, plain or quoted, followed by a value, a comment or nothing. */
    private static final Pattern KEY = Pattern.compile("^( *)(\"[^\"]*\"|'[^']*'|[^\\s#'\"\\-{\\[][^:#]*?|-[^\\s:#][^:#]*?):(?: +(.*))?$");

    private YamlUpdate() {
    }

    /**
     * @param current  the user's file
     * @param bundled  the file this version ships
     * @param userOwned dot paths whose children are the user's own entries (kits, maps ...): the bundled ones are not
     *                  added there, only the path itself when it is missing
     */
    public static Result addMissing(String current, String bundled, Collection<String> userOwned) {
        Document user = Document.parse(current);
        Document shipped = Document.parse(bundled);
        Set<String> owned = new HashSet<>(userOwned);
        // Insertion line -> blocks, in the order they are planned
        TreeMap<Integer, List<String>> insertions = new TreeMap<>();
        List<String> added = new ArrayList<>();
        merge(user, user.root, shipped, shipped.root, "", owned, insertions, added);
        if (added.isEmpty()) {
            return new Result(current, Collections.<String>emptyList());
        }
        List<String> lines = new ArrayList<>(user.lines);
        for (Map.Entry<Integer, List<String>> insertion : insertions.descendingMap().entrySet()) {
            lines.addAll(insertion.getKey(), insertion.getValue());
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            text.append(lines.get(i));
            if (i < lines.size() - 1 || user.endsWithNewline) {
                text.append(user.newline);
            }
        }
        return new Result(text.toString(), added);
    }

    private static void merge(Document user, Node userNode, Document shipped, Node shippedNode, String path,
                              Set<String> owned, TreeMap<Integer, List<String>> insertions, List<String> added) {
        List<String> order = new ArrayList<>(shippedNode.children.keySet());
        for (int i = 0; i < order.size(); i++) {
            String key = order.get(i);
            Node shippedChild = shippedNode.children.get(key);
            String childPath = path.isEmpty() ? key : path + "." + key;
            Node userChild = userNode.children.get(key);
            if (userChild != null) {
                // An empty section (its keys all gone) is still a section to fill
                if (!userChild.hasValue && shippedChild.isSection() && !owned.contains(childPath)) {
                    merge(user, userChild, shipped, shippedChild, childPath, owned, insertions, added);
                }
                continue;
            }
            int at = insertionPoint(user, userNode, order, i);
            boolean beforeNext = at != user.end(userNode) && !hasEarlierSibling(userNode, order, i);
            List<String> block = shipped.block(shippedChild, childIndent(userNode, shippedNode, shippedChild), beforeNext);
            List<String> planned = insertions.get(at);
            if (planned == null) {
                insertions.put(at, block);
            } else {
                planned.addAll(block);
            }
            added.add(childPath);
        }
    }

    private static boolean hasEarlierSibling(Node section, List<String> order, int index) {
        for (int i = index - 1; i >= 0; i--) {
            if (section.children.containsKey(order.get(i))) {
                return true;
            }
        }
        return false;
    }

    /** After the nearest earlier bundled sibling the user has; else before the nearest later one; else at the section's end. */
    private static int insertionPoint(Document user, Node section, List<String> order, int index) {
        for (int i = index - 1; i >= 0; i--) {
            Node before = section.children.get(order.get(i));
            if (before != null) {
                return user.end(before);
            }
        }
        for (int i = index + 1; i < order.size(); i++) {
            Node after = section.children.get(order.get(i));
            if (after != null) {
                return after.commentStart;
            }
        }
        return user.end(section);
    }

    /** The indentation the user's file gives this section's children. */
    private static int childIndent(Node userSection, Node shippedSection, Node shippedChild) {
        if (!userSection.children.isEmpty()) {
            return userSection.children.values().iterator().next().indent;
        }
        return userSection.indent + (shippedChild.indent - shippedSection.indent);
    }

    /** What {@link #addMissing} did. */
    public static final class Result {

        private final String text;
        private final List<String> added;

        Result(String text, List<String> added) {
            this.text = text;
            this.added = Collections.unmodifiableList(added);
        }

        /** The updated file; the user's file unchanged when nothing was added. */
        public String text() {
            return text;
        }

        /** Dot paths of the keys added, in file order. */
        public List<String> added() {
            return added;
        }

        public boolean changed() {
            return !added.isEmpty();
        }
    }

    /** A key: its line, the comment lines right above it, its children if it is a section. */
    private static final class Node {
        final int indent;
        final int line;
        final boolean hasValue;
        int commentStart;
        final Map<String, Node> children = new LinkedHashMap<>();

        Node(int indent, int line, boolean hasValue) {
            this.indent = indent;
            this.line = line;
            this.hasValue = hasValue;
            this.commentStart = line;
        }

        boolean isSection() {
            return !hasValue && !children.isEmpty();
        }
    }

    private static final class Document {
        final List<String> lines;
        final String newline;
        final boolean endsWithNewline;
        final Node root = new Node(-1, -1, false);
        /** Key lines in file order, for {@link #end}. */
        final List<Node> keys = new ArrayList<>();

        private Document(List<String> lines, String newline, boolean endsWithNewline) {
            this.lines = lines;
            this.newline = newline;
            this.endsWithNewline = endsWithNewline;
        }

        static Document parse(String text) {
            String newline = text.contains("\r\n") ? "\r\n" : "\n";
            List<String> lines = new ArrayList<>();
            for (String line : text.split("\r?\n", -1)) {
                lines.add(line);
            }
            boolean endsWithNewline = !lines.isEmpty() && lines.get(lines.size() - 1).isEmpty();
            if (endsWithNewline) {
                lines.remove(lines.size() - 1);
            }
            Document document = new Document(lines, newline, endsWithNewline);
            document.build();
            return document;
        }

        private void build() {
            List<Node> stack = new ArrayList<>();
            stack.add(root);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }
                Matcher key = KEY.matcher(line);
                int indent = indent(line);
                Node top = stack.get(stack.size() - 1);
                // A list item, or a line inside a value (list of maps, multi-line string): not a key of the file
                if (!key.matches() || (top != root && top.hasValue && indent > top.indent)) {
                    continue;
                }
                while (stack.size() > 1 && stack.get(stack.size() - 1).indent >= indent) {
                    stack.remove(stack.size() - 1);
                }
                Node parent = stack.get(stack.size() - 1);
                String value = key.group(3);
                boolean hasValue = value != null && !value.trim().isEmpty() && !value.trim().startsWith("#");
                Node node = new Node(indent, i, hasValue || nextIsListItem(i, indent));
                node.commentStart = commentStart(i);
                parent.children.putIfAbsent(unquote(key.group(2).trim()), node);
                keys.add(node);
                stack.add(node);
            }
        }

        /** {@code key:} followed by {@code - item} lines: a list value, even at the key's own indentation. */
        private boolean nextIsListItem(int line, int indent) {
            for (int i = line + 1; i < lines.size(); i++) {
                String next = lines.get(i);
                String trimmed = next.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                return trimmed.startsWith("- ") || trimmed.equals("-") ? indent(next) >= indent : false;
            }
            return false;
        }

        /** The comment lines right above the key (no blank line between). */
        private int commentStart(int line) {
            int start = line;
            while (start > 0 && lines.get(start - 1).trim().startsWith("#")) {
                start--;
            }
            return start;
        }

        /** After the node's last line: its value, children and comments inside, not the blank lines that follow. */
        int end(Node node) {
            if (node == root) {
                return lines.size();
            }
            int end = lines.size();
            int index = keys.indexOf(node);
            for (int i = index + 1; i < keys.size(); i++) {
                if (keys.get(i).indent <= node.indent) {
                    end = keys.get(i).commentStart;
                    break;
                }
            }
            while (end > node.line + 1 && lines.get(end - 1).trim().isEmpty()) {
                end--;
            }
            return end;
        }

        /**
         * The node's lines with its comments, re-indented to {@code indent}, with the blank line that separated it
         * from what came before it ({@code beforeNext} false) or after it ({@code beforeNext} true).
         */
        List<String> block(Node node, int indent, boolean beforeNext) {
            int shift = indent - node.indent;
            int end = end(node);
            List<String> block = new ArrayList<>();
            if (!beforeNext && node.commentStart > 0 && lines.get(node.commentStart - 1).trim().isEmpty()) {
                block.add("");
            }
            for (int i = node.commentStart; i < end; i++) {
                block.add(shift(lines.get(i), shift));
            }
            if (beforeNext && end < lines.size() && lines.get(end).trim().isEmpty()) {
                block.add("");
            }
            return block;
        }

        private static String shift(String line, int shift) {
            if (line.trim().isEmpty() || shift == 0) {
                return line;
            }
            if (shift > 0) {
                StringBuilder spaces = new StringBuilder();
                for (int i = 0; i < shift; i++) {
                    spaces.append(' ');
                }
                return spaces + line;
            }
            int remove = Math.min(-shift, indent(line));
            return line.substring(remove);
        }

        private static int indent(String line) {
            int indent = 0;
            while (indent < line.length() && line.charAt(indent) == ' ') {
                indent++;
            }
            return indent;
        }

        private static String unquote(String key) {
            if (key.length() >= 2 && (key.charAt(0) == '"' || key.charAt(0) == '\'')
                    && key.charAt(key.length() - 1) == key.charAt(0)) {
                return key.substring(1, key.length() - 1);
            }
            return key;
        }
    }
}
