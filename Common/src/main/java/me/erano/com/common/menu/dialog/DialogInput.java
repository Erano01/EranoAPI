package me.erano.com.common.menu.dialog;

/** A field of a {@link DialogMenu}; its value reaches {@link DialogClick} under {@link #getKey()}. */
public abstract class DialogInput {

    private final String key;
    private final String label;

    DialogInput(String key, String label) {
        this.key = key;
        this.label = label;
    }

    /** Letters, digits and {@code _} only. */
    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public static TextInput text(String key, String label) {
        return new TextInput(key, label);
    }

    public static BooleanInput bool(String key, String label) {
        return new BooleanInput(key, label);
    }

    public static NumberInput number(String key, String label, float start, float end) {
        return new NumberInput(key, label, start, end);
    }

    public static OptionInput options(String key, String label) {
        return new OptionInput(key, label);
    }
}
