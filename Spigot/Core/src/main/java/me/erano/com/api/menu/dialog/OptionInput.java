package me.erano.com.api.menu.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A button that cycles through options; {@link DialogClick#getText(String)} gives the chosen option's id. */
public class OptionInput extends DialogInput {

    private final List<Option> options = new ArrayList<>();

    OptionInput(String key, String label) {
        super(key, label);
    }

    public OptionInput option(String id, String display) {
        return option(id, display, false);
    }

    public OptionInput option(String id, String display, boolean initial) {
        options.add(new Option(id, display, initial));
        return this;
    }

    public List<Option> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public static final class Option {

        private final String id;
        private final String display;
        private final boolean initial;

        Option(String id, String display, boolean initial) {
            this.id = id;
            this.display = display;
            this.initial = initial;
        }

        public String getId() {
            return id;
        }

        public String getDisplay() {
            return display;
        }

        public boolean isInitial() {
            return initial;
        }
    }
}
