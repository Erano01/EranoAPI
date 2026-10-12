package me.erano.com.bedrock.crossplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;

import me.erano.com.api.bedrock.BedrockService;
import me.erano.com.api.menu.dialog.AbstractDialogService;
import me.erano.com.common.menu.dialog.BooleanInput;
import me.erano.com.common.menu.dialog.DialogButton;
import me.erano.com.common.menu.dialog.DialogInput;
import me.erano.com.common.menu.dialog.DialogMenu;
import me.erano.com.common.menu.dialog.DialogService;
import me.erano.com.common.menu.dialog.NumberInput;
import me.erano.com.common.menu.dialog.OptionInput;
import me.erano.com.common.menu.dialog.TextInput;

/**
 * Shows {@link DialogMenu}s as Bedrock forms (Cumulus, shipped by Floodgate and Geyser):
 * <ul>
 * <li>no inputs, two buttons, no exit button: modal form (yes / no)</li>
 * <li>no inputs: simple form, a list of buttons, the exit button last</li>
 * <li>inputs: custom form; with several buttons, they become a dropdown at the end</li>
 * </ul>
 * Closing the form runs the exit button.
 */
abstract class FormBedrockService extends AbstractDialogService implements BedrockService {

    FormBedrockService(Plugin plugin) {
        super(plugin);
    }

    abstract boolean isBedrock(UUID player);

    abstract void send(UUID player, Form form);

    @Override
    public boolean isBedrock(Player player) {
        return isBedrock(player.getUniqueId());
    }

    @Override
    public DialogService<Player> forms() {
        return this;
    }

    // Both BedrockService and DialogService: forms work on every version.
    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public void show(Player player, DialogMenu<Player> menu) {
        menu.prepare(player);
        send(player.getUniqueId(), toForm(player, menu));
    }

    // Bedrock forms can't be closed by the server.
    @Override
    public void close(Player player) {
    }

    private Form toForm(Player player, DialogMenu<Player> menu) {
        String content = String.join("\n", menu.getBody());
        List<DialogButton<Player>> buttons = menu.getButtons();
        DialogButton<Player> exit = menu.getExitButton();
        Map<String, Object> noValues = new HashMap<>();

        if (menu.getInputs().isEmpty() && buttons.size() == 2 && exit == null) {
            return ModalForm.builder()
                    .title(menu.getTitle())
                    .content(content)
                    .button1(buttons.get(0).getLabel())
                    .button2(buttons.get(1).getLabel())
                    .validResultHandler(response -> click(player, menu, buttons.get(response.clickedFirst() ? 0 : 1), noValues))
                    .build();
        }

        if (menu.getInputs().isEmpty()) {
            SimpleForm.Builder form = SimpleForm.builder().title(menu.getTitle()).content(content);
            for (DialogButton<Player> button : buttons) {
                form.button(button.getLabel());
            }
            if (exit != null) {
                form.button(exit.getLabel());
            }
            return form
                    .validResultHandler(response -> {
                        int id = response.clickedButtonId();
                        clickOrExit(player, menu, id < buttons.size() ? buttons.get(id) : exit, noValues);
                    })
                    .closedOrInvalidResultHandler(() -> clickOrExit(player, menu, exit, noValues))
                    .build();
        }

        CustomForm.Builder form = CustomForm.builder().title(menu.getTitle());
        int index = 0;
        if (!content.isEmpty()) {
            form.label(content);
            index++;
        }
        // Cumulus numbers every component, labels included.
        List<Integer> indexes = new ArrayList<>();
        for (DialogInput input : menu.getInputs()) {
            add(form, input);
            indexes.add(index++);
        }
        int actionIndex = index;
        if (buttons.size() > 1) {
            List<String> labels = new ArrayList<>();
            for (DialogButton<Player> button : buttons) {
                labels.add(button.getLabel());
            }
            form.dropdown("", labels, 0);
        }
        return form
                .validResultHandler(response -> {
                    DialogButton<Player> button = buttons.size() > 1 ? buttons.get(response.asDropdown(actionIndex))
                            : buttons.size() == 1 ? buttons.get(0) : exit;
                    clickOrExit(player, menu, button, values(menu, indexes, response));
                })
                .closedOrInvalidResultHandler(() -> clickOrExit(player, menu, exit, noValues))
                .build();
    }

    private void clickOrExit(Player player, DialogMenu<Player> menu, DialogButton<Player> button, Map<String, Object> values) {
        if (button != null) {
            click(player, menu, button, values);
        }
    }

    private static void add(CustomForm.Builder form, DialogInput input) {
        if (input instanceof TextInput) {
            form.input(input.getLabel(), "", ((TextInput) input).getInitial());
        } else if (input instanceof BooleanInput) {
            form.toggle(input.getLabel(), ((BooleanInput) input).getInitial());
        } else if (input instanceof NumberInput) {
            NumberInput number = (NumberInput) input;
            float step = number.getStep() == null ? 1 : number.getStep();
            float initial = number.getInitial() == null ? (number.getStart() + number.getEnd()) / 2 : number.getInitial();
            form.slider(input.getLabel(), number.getStart(), number.getEnd(), step, initial);
        } else if (input instanceof OptionInput) {
            List<String> displays = new ArrayList<>();
            int initial = 0;
            List<OptionInput.Option> options = ((OptionInput) input).getOptions();
            for (int i = 0; i < options.size(); i++) {
                displays.add(options.get(i).getDisplay());
                if (options.get(i).isInitial()) {
                    initial = i;
                }
            }
            form.dropdown(input.getLabel(), displays, initial);
        } else {
            throw new IllegalArgumentException("Unknown dialog input " + input.getClass().getName());
        }
    }

    private static Map<String, Object> values(DialogMenu<Player> menu, List<Integer> indexes, CustomFormResponse response) {
        Map<String, Object> values = new HashMap<>();
        for (int i = 0; i < menu.getInputs().size(); i++) {
            DialogInput input = menu.getInputs().get(i);
            int index = indexes.get(i);
            if (input instanceof TextInput) {
                values.put(input.getKey(), response.asInput(index));
            } else if (input instanceof BooleanInput) {
                values.put(input.getKey(), response.asToggle(index));
            } else if (input instanceof NumberInput) {
                values.put(input.getKey(), response.asSlider(index));
            } else if (input instanceof OptionInput) {
                values.put(input.getKey(), ((OptionInput) input).getOptions().get(response.asDropdown(index)).getId());
            }
        }
        return values;
    }
}
