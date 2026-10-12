package me.erano.com.common.menu.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.erano.com.common.menu.Menu;

//usage (Bukkit):
//public class SettingsDialog extends DialogMenu<Player> {
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
 * A screen of text, inputs and buttons instead of item slots: Minecraft's dialog screen on Java Edition
 * (1.21.6+), a form on Bedrock. Shown through the platform's {@link DialogService}.
 *
 * <p>Like {@code InventoryMenu#decorate}, the content is built in {@link #decorate(Object)}, for the player
 * it's shown to, every time it's opened.
 *
 * @param <P> the platform's player type
 */
public abstract class DialogMenu<P> implements Menu<P> {

    private final String title;
    private final List<String> body = new ArrayList<>();
    private final List<DialogInput> inputs = new ArrayList<>();
    private final List<DialogButton<P>> buttons = new ArrayList<>();
    private DialogButton<P> exitButton;
    private int columns = 2;
    private boolean closeWithEscape = true;

    protected DialogMenu(String title) {
        this.title = title;
    }

    // Alt sınıf addBody / addInput / addButton ile içeriği burada kurar.
    protected abstract void decorate(P player);

    /** Clears the content and builds it again for {@code player}; called by the dialog service before showing. */
    public final void prepare(P player) {
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

    public void addButton(DialogButton<P> button) {
        buttons.add(button);
    }

    /** Button under the others, also run when the dialog is closed with Escape. */
    public void setExitButton(DialogButton<P> exitButton) {
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

    public List<DialogButton<P>> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public DialogButton<P> getExitButton() {
        return exitButton;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isCloseWithEscape() {
        return closeWithEscape;
    }
}
