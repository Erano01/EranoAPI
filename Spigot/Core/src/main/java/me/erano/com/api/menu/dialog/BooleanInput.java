package me.erano.com.api.menu.dialog;

/** A checkbox. */
public class BooleanInput extends DialogInput {

    private boolean initial;

    BooleanInput(String key, String label) {
        super(key, label);
    }

    public BooleanInput initial(boolean initial) {
        this.initial = initial;
        return this;
    }

    public boolean getInitial() {
        return initial;
    }
}
