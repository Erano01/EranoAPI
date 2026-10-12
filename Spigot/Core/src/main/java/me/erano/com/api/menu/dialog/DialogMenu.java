package me.erano.com.api.menu.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import me.erano.com.api.menu.Menu;
import me.erano.com.api.menu.MenuDispatcher;

//usage:
//public class SettingsDialog extends DialogMenu {
//    public SettingsDialog() { super("Ayarlar"); }
//
//    @Override
//    protected void decorate(Player player) {
//        addBody("Merhaba " + player.getName());
//        addInput(DialogInput.text("nick", "Takma ad"));
//        addButton(new DialogButton().label("Kaydet").consumer(click -> ...click.getText("nick")...));
//    }
//}
//menuDispatcher.openMenu(new SettingsDialog(), player);

/**
 * Minecraft's dialog screen (1.21.6+): text, inputs and buttons instead of item slots. Shown through
 * {@link me.erano.com.api.EranoServices#dialogs()}; check its {@code isSupported()} on older servers.
 *
 * <p>Like {@link me.erano.com.api.menu.InventoryMenu#decorate(Player)}, the content is built in
 * {@link #decorate(Player)}, for the player it's shown to, every time it's opened.
 */
public abstract class DialogMenu implements Menu {

    private final String title;
    private final List<String> body = new ArrayList<>();
    private final List<DialogInput> inputs = new ArrayList<>();
    private final List<DialogButton> buttons = new ArrayList<>();
    private DialogButton exitButton;
    private int columns = 2;
    private boolean closeWithEscape = true;

    protected DialogMenu(String title) {
        this.title = title;
    }

    // Alt sınıf addBody / addInput / addButton ile içeriği burada kurar.
    protected abstract void decorate(Player player);

    @Override
    public void open(Player player, MenuDispatcher dispatcher) {
        dispatcher.openDialog(this, player);
    }

    /** Clears the content and builds it again for {@code player}; called by the dialog service before showing. */
    public final void prepare(Player player) {
        body.clear();
        inputs.clear();
        buttons.clear();
        exitButton = null;
        decorate(player);
    }

    public void addBody(String text) {
        body.add(text);
    }

    public void addInput(DialogInput input) {
        inputs.add(input);
    }

    public void addButton(DialogButton button) {
        buttons.add(button);
    }

    /** Button under the others, also run when the dialog is closed with Escape. */
    public void setExitButton(DialogButton exitButton) {
        this.exitButton = exitButton;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setCloseWithEscape(boolean closeWithEscape) {
        this.closeWithEscape = closeWithEscape;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getBody() {
        return Collections.unmodifiableList(body);
    }

    public List<DialogInput> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    public List<DialogButton> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public DialogButton getExitButton() {
        return exitButton;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isCloseWithEscape() {
        return closeWithEscape;
    }
}
