package me.erano.com.paper.core.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.erano.com.api.menu.dialog.AbstractDialogService;
import me.erano.com.common.menu.dialog.BooleanInput;
import me.erano.com.common.menu.dialog.DialogButton;
import me.erano.com.common.menu.dialog.DialogInput;
import me.erano.com.common.menu.dialog.DialogMenu;
import me.erano.com.common.menu.dialog.NumberInput;
import me.erano.com.common.menu.dialog.OptionInput;
import me.erano.com.common.menu.dialog.TextInput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/** Paper 1.21.6+: Paper's dialog API, each button calls back through Adventure's click callbacks. */
public class PaperDialogService extends AbstractDialogService {

    PaperDialogService(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void show(Player player, DialogMenu<Player> menu) {
        menu.prepare(player);

        List<DialogBody> body = new ArrayList<>();
        for (String text : menu.getBody()) {
            body.add(DialogBody.plainMessage(text(text)));
        }
        List<io.papermc.paper.registry.data.dialog.input.DialogInput> inputs = new ArrayList<>();
        for (DialogInput input : menu.getInputs()) {
            inputs.add(toPaper(input));
        }
        List<ActionButton> buttons = new ArrayList<>();
        for (DialogButton<Player> button : menu.getButtons()) {
            buttons.add(toPaper(menu, button));
        }
        ActionButton exit = menu.getExitButton() == null ? null : toPaper(menu, menu.getExitButton());

        DialogBase base = DialogBase.builder(text(menu.getTitle()))
                .body(body)
                .inputs(inputs)
                .canCloseWithEscape(menu.isCloseWithEscape())
                .build();
        player.showDialog(Dialog.create(factory -> factory.empty()
                .base(base)
                .type(DialogType.multiAction(buttons, exit, menu.getColumns()))));
    }

    @Override
    public void close(Player player) {
        player.closeDialog();
    }

    private ActionButton toPaper(DialogMenu<Player> menu, DialogButton<Player> button) {
        DialogAction action = DialogAction.customClick(
                (response, audience) -> {
                    if (audience instanceof Player) {
                        click((Player) audience, menu, button, values(menu, response));
                    }
                },
                ClickCallback.Options.builder().uses(1).build());
        return ActionButton.builder(text(button.getLabel()))
                .tooltip(button.getTooltip() == null ? null : text(button.getTooltip()))
                .width(button.getWidth())
                .action(action)
                .build();
    }

    private static Map<String, Object> values(DialogMenu<Player> menu, DialogResponseView response) {
        Map<String, Object> values = new HashMap<>();
        for (DialogInput input : menu.getInputs()) {
            String key = input.getKey();
            Object value;
            if (input instanceof BooleanInput) {
                value = response.getBoolean(key);
            } else if (input instanceof NumberInput) {
                value = response.getFloat(key);
            } else {
                value = response.getText(key);
            }
            if (value != null) {
                values.put(key, value);
            }
        }
        return values;
    }

    private static io.papermc.paper.registry.data.dialog.input.DialogInput toPaper(DialogInput input) {
        String key = input.getKey();
        Component label = text(input.getLabel());
        if (input instanceof TextInput) {
            TextInput text = (TextInput) input;
            return io.papermc.paper.registry.data.dialog.input.DialogInput.text(key, label)
                    .initial(text.getInitial())
                    .maxLength(text.getMaxLength())
                    .build();
        }
        if (input instanceof BooleanInput) {
            return io.papermc.paper.registry.data.dialog.input.DialogInput.bool(key, label)
                    .initial(((BooleanInput) input).getInitial())
                    .build();
        }
        if (input instanceof NumberInput) {
            NumberInput number = (NumberInput) input;
            return io.papermc.paper.registry.data.dialog.input.DialogInput.numberRange(key, label, number.getStart(), number.getEnd())
                    .initial(number.getInitial())
                    .step(number.getStep())
                    .build();
        }
        if (input instanceof OptionInput) {
            List<SingleOptionDialogInput.OptionEntry> entries = new ArrayList<>();
            for (OptionInput.Option option : ((OptionInput) input).getOptions()) {
                entries.add(SingleOptionDialogInput.OptionEntry.create(option.getId(), text(option.getDisplay()), option.isInitial()));
            }
            return io.papermc.paper.registry.data.dialog.input.DialogInput.singleOption(key, label, entries).build();
        }
        throw new IllegalArgumentException("Unknown dialog input " + input.getClass().getName());
    }

    // § color codes work like everywhere else in EranoAPI.
    private static Component text(String legacy) {
        return LegacyComponentSerializer.legacySection().deserialize(legacy == null ? "" : legacy);
    }
}
