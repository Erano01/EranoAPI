package me.erano.com.common.menu.dialog;

/** A text box. */
public class TextInput extends DialogInput {

    private String initial = "";
    private int maxLength = 32;

    TextInput(String key, String label) {
        super(key, label);
    }

    public TextInput initial(String initial) {
        this.initial = initial;
        return this;
    }

    public TextInput maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public String getInitial() {
        return initial;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
