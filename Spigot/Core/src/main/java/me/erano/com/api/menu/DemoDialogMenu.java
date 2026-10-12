package me.erano.com.api.menu;

import org.bukkit.entity.Player;

import me.erano.com.common.menu.dialog.DialogButton;
import me.erano.com.common.menu.dialog.DialogInput;
import me.erano.com.common.menu.dialog.DialogMenu;

// PianoMenu'nün dialog karşılığı: her input tipinden biri, gönderilince değerleri oyuncuya yazar.
public class DemoDialogMenu extends DialogMenu<Player> {

    private final MenuDispatcher menuDispatcher;

    public DemoDialogMenu(MenuDispatcher menuDispatcher) {
        super("§6EranoAPI Dialog");
        this.menuDispatcher = menuDispatcher;
    }

    @Override
    protected void decorate(Player player) {
        addBody("Merhaba §e" + player.getName() + "§r, bu bir dialog menü.");
        addInput(DialogInput.text("nick", "Takma ad").initial(player.getName()).maxLength(16));
        addInput(DialogInput.bool("pvp", "PvP açık").initial(true));
        addInput(DialogInput.number("volume", "Ses", 0, 100).step(1).initial(50));
        addInput(DialogInput.options("mode", "Mod")
                .option("survival", "Survival", true)
                .option("creative", "Creative"));

        addButton(new DialogButton<Player>()
                .label("§aGönder")
                .tooltip("Değerleri sohbete yazar")
                .consumer(click -> click.getPlayer().sendMessage("nick=" + click.getText("nick")
                        + " pvp=" + click.getBoolean("pvp")
                        + " volume=" + click.getNumber("volume")
                        + " mode=" + click.getText("mode"))));
        addButton(new DialogButton<Player>()
                .label("Piano")
                .tooltip("Envanter menüsüne geçer")
                .consumer(click -> menuDispatcher.openMenu(new PianoMenu(), click.getPlayer())));
        setExitButton(new DialogButton<Player>().label("Kapat"));
    }
}
