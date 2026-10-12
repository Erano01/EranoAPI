package me.erano.com.api.menu.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCustomClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.MultiActionDialog;
import net.md_5.bungee.api.dialog.action.ActionButton;
import net.md_5.bungee.api.dialog.action.CustomClickAction;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.body.PlainMessageBody;
import net.md_5.bungee.api.dialog.input.InputOption;
import net.md_5.bungee.api.dialog.input.SingleOptionInput;

/**
 * Spigot 1.21.6+: bungeecord-dialog. Every button sends {@code eranoapi:dialog/<session>/<button>} back
 * ({@link PlayerCustomClickEvent}), mapped here to the button of the player's open dialog.
 */
public class SpigotDialogService extends AbstractDialogService implements Listener {

    private static final String NAMESPACE = "eranoapi";
    private static final int EXIT = -1;

    private final Map<UUID, Session> sessions = new HashMap<>();
    private int nextSession;

    SpigotDialogService(Plugin plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void show(Player player, DialogMenu menu) {
        menu.prepare(player);
        int session = nextSession++;

        List<DialogBody> body = new ArrayList<>();
        for (String text : menu.getBody()) {
            body.add(new PlainMessageBody(text(text)));
        }
        List<net.md_5.bungee.api.dialog.input.DialogInput> inputs = new ArrayList<>();
        for (DialogInput input : menu.getInputs()) {
            inputs.add(toBungee(input));
        }
        List<ActionButton> buttons = new ArrayList<>();
        for (int i = 0; i < menu.getButtons().size(); i++) {
            buttons.add(toBungee(menu.getButtons().get(i), session, i));
        }
        ActionButton exit = menu.getExitButton() == null ? null : toBungee(menu.getExitButton(), session, EXIT);

        DialogBase base = new DialogBase(text(menu.getTitle()))
                .body(body)
                .inputs(inputs)
                .canCloseWithEscape(menu.isCloseWithEscape());
        sessions.put(player.getUniqueId(), new Session(session, menu));
        player.showDialog(new MultiActionDialog(base, buttons, menu.getColumns(), exit));
    }

    @Override
    public void close(Player player) {
        sessions.remove(player.getUniqueId());
        player.clearDialog();
    }

    @EventHandler
    public void onCustomClick(PlayerCustomClickEvent event) {
        if (!NAMESPACE.equals(event.getId().getNamespace())) {
            return;
        }
        String[] path = event.getId().getKey().split("/");
        if (path.length != 3 || !path[0].equals("dialog")) {
            return;
        }
        Player player = event.getPlayer();
        Session session = sessions.get(player.getUniqueId());
        if (session == null || !path[1].equals(String.valueOf(session.id))) {
            return;
        }
        int index = Integer.parseInt(path[2]);
        DialogMenu menu = session.menu;
        DialogButton button = index == EXIT ? menu.getExitButton() : index < menu.getButtons().size() ? menu.getButtons().get(index) : null;
        if (button == null) {
            return;
        }
        // Buttons close the dialog (after_action: close).
        sessions.remove(player.getUniqueId());
        click(player, menu, button, values(menu, event.getData()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        sessions.remove(event.getPlayer().getUniqueId());
    }

    // The payload is the inputs' NBT as JSON: text and option ids are strings, checkboxes 0 / 1 or booleans.
    private static Map<String, Object> values(DialogMenu menu, JsonElement data) {
        Map<String, Object> values = new HashMap<>();
        if (data == null || !data.isJsonObject()) {
            return values;
        }
        JsonObject object = data.getAsJsonObject();
        for (DialogInput input : menu.getInputs()) {
            JsonElement element = object.get(input.getKey());
            if (element == null || !element.isJsonPrimitive()) {
                continue;
            }
            JsonPrimitive value = element.getAsJsonPrimitive();
            if (input instanceof BooleanInput) {
                values.put(input.getKey(), value.isBoolean() ? value.getAsBoolean() : value.isNumber() ? value.getAsInt() != 0 : Boolean.parseBoolean(value.getAsString()));
            } else if (input instanceof NumberInput) {
                values.put(input.getKey(), value.getAsFloat());
            } else {
                values.put(input.getKey(), value.getAsString());
            }
        }
        return values;
    }

    private static ActionButton toBungee(DialogButton button, int session, int index) {
        ActionButton bungee = new ActionButton(text(button.getLabel()), new CustomClickAction(NAMESPACE + ":dialog/" + session + "/" + index));
        bungee.width(button.getWidth());
        if (button.getTooltip() != null) {
            bungee.tooltip(text(button.getTooltip()));
        }
        return bungee;
    }

    private static net.md_5.bungee.api.dialog.input.DialogInput toBungee(DialogInput input) {
        String key = input.getKey();
        BaseComponent label = text(input.getLabel());
        if (input instanceof TextInput) {
            TextInput text = (TextInput) input;
            return new net.md_5.bungee.api.dialog.input.TextInput(key, label).initial(text.getInitial()).maxLength(text.getMaxLength());
        }
        if (input instanceof BooleanInput) {
            return new net.md_5.bungee.api.dialog.input.BooleanInput(key, label).initial(((BooleanInput) input).getInitial());
        }
        if (input instanceof NumberInput) {
            NumberInput number = (NumberInput) input;
            return new net.md_5.bungee.api.dialog.input.NumberRangeInput(key, label, number.getStart(), number.getEnd(), number.getInitial(), number.getStep());
        }
        if (input instanceof OptionInput) {
            List<InputOption> options = new ArrayList<>();
            for (OptionInput.Option option : ((OptionInput) input).getOptions()) {
                options.add(new InputOption(option.getId(), text(option.getDisplay()), option.isInitial()));
            }
            return new SingleOptionInput(key, null, label, null, options);
        }
        throw new IllegalArgumentException("Unknown dialog input " + input.getClass().getName());
    }

    // § color codes work like everywhere else in EranoAPI.
    private static BaseComponent text(String legacy) {
        return TextComponent.fromLegacy(legacy == null ? "" : legacy);
    }

    private static final class Session {

        private final int id;
        private final DialogMenu menu;

        private Session(int id, DialogMenu menu) {
            this.id = id;
            this.menu = menu;
        }
    }
}
