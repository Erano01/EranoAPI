package me.erano.com.api.menu.dialog;

import java.util.Map;

import org.bukkit.entity.Player;

/** A {@link DialogButton} click: who clicked and what the dialog's inputs held, by {@link DialogInput#getKey()}. */
public class DialogClick {

    private final Player player;
    private final DialogMenu menu;
    private final Map<String, Object> values;

    public DialogClick(Player player, DialogMenu menu, Map<String, Object> values) {
        this.player = player;
        this.menu = menu;
        this.values = values;
    }

    public Player getPlayer() {
        return player;
    }

    public DialogMenu getMenu() {
        return menu;
    }

    /** {@link TextInput} value, or the id of the chosen {@link OptionInput} option. */
    public String getText(String key) {
        Object value = values.get(key);
        return value instanceof String ? (String) value : null;
    }

    /** {@link BooleanInput} value. */
    public Boolean getBoolean(String key) {
        Object value = values.get(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }

    /** {@link NumberInput} value. */
    public Float getNumber(String key) {
        Object value = values.get(key);
        return value instanceof Float ? (Float) value : null;
    }
}
