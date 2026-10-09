package me.erano.com.common.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

class YamlUpdateTest {

    private static final String BUNDLED = String.join("\n",
            "# Header of the file",
            "",
            "# How many players",
            "min-players: 2",
            "",
            "lobby:",
            "  enabled: true",
            "  # Rules in the lobby",
            "  rules:",
            "    damage: false",
            "    pvp: false",
            "",
            "# Items, a list",
            "items:",
            "- {item: STONE, weight: 10}",
            "- item: APPLE",
            "  weight: 5",
            "kits:",
            "  ares:",
            "    icon: IRON_SWORD",
            "  tarzan:",
            "    icon: STICK",
            "",
            "# What happens at the end",
            "end-action: RESTART",
            "");

    @Test
    void upToDateFileIsLeftAlone() {
        YamlUpdate.Result result = YamlUpdate.addMissing(BUNDLED, BUNDLED, Collections.<String>emptyList());
        assertFalse(result.changed());
        assertEquals(BUNDLED, result.text());
    }

    @Test
    void missingKeysComeBackWithTheirComments() {
        String user = BUNDLED
                .replace("# How many players\nmin-players: 2\n\n", "")
                .replace("    pvp: false\n", "")
                .replace("  # Rules in the lobby\n  rules:\n    damage: false\n", "")
                .replace("\n# What happens at the end\nend-action: RESTART\n", "\n");
        YamlUpdate.Result result = YamlUpdate.addMissing(user, BUNDLED, Collections.<String>emptyList());
        assertEquals(Arrays.asList("min-players", "lobby.rules", "end-action"), result.added());
        assertEquals(BUNDLED.trim(), result.text().trim());
    }

    @Test
    void userValuesCommentsAndOwnKeysStay() {
        String user = String.join("\n",
                "# my notes",
                "lobby:",
                "  enabled: false   # off on this server",
                "  my-own: 1",
                "items: []",
                "end-action: SHUTDOWN",
                "");
        YamlUpdate.Result result = YamlUpdate.addMissing(user, BUNDLED, Collections.<String>emptyList());
        assertEquals(Arrays.asList("min-players", "lobby.rules", "kits"), result.added());
        String text = result.text();
        // rules goes where the bundled file has it, after enabled; the user's own key stays after
        assertEquals(true, text.contains("  enabled: false   # off on this server\n  # Rules in the lobby\n  rules:\n"
                + "    damage: false\n    pvp: false\n  my-own: 1\n"));
        assertEquals(true, text.contains("items: []\n"));
        assertEquals(true, text.contains("end-action: SHUTDOWN"));
        assertEquals(false, text.contains("{item: STONE"));
    }

    @Test
    void userOwnedSectionsGetNoBundledEntries() {
        String user = BUNDLED.replace("  tarzan:\n    icon: STICK\n", "");
        YamlUpdate.Result result = YamlUpdate.addMissing(user, BUNDLED, Collections.singletonList("kits"));
        assertFalse(result.changed());
    }

    @Test
    void anEmptiedSectionIsFilledAgain() {
        YamlUpdate.Result result = YamlUpdate.addMissing("language:\nother: 1\n", "language:\n  default: en\nother: 1\n",
                Collections.<String>emptyList());
        assertEquals("language:\n  default: en\nother: 1\n", result.text());
    }

    @Test
    void reindentsToTheUsersIndentation() {
        String user = "lobby:\n    enabled: true\n";
        YamlUpdate.Result result = YamlUpdate.addMissing(user, "lobby:\n  enabled: true\n  rules:\n    pvp: false\n",
                Collections.<String>emptyList());
        assertEquals("lobby:\n    enabled: true\n    rules:\n      pvp: false\n", result.text());
    }
}
