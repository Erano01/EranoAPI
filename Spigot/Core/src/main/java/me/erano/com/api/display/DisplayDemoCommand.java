package me.erano.com.api.display;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.erano.com.api.EranoServices;
import me.erano.com.api.hologram.Hologram;

/** /eranodemo: manual test of {@link MessageService} and the hologram service. */
public class DisplayDemoCommand implements CommandExecutor {

    private final Map<UUID, Hologram> holograms = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || args.length == 0) {
            return false;
        }
        Player player = (Player) sender;
        switch (args[0].toLowerCase()) {
            case "actionbar":
                EranoServices.messages().sendActionBar(player, ChatColor.GOLD + "EranoAPI " + ChatColor.GREEN + "action bar");
                return true;
            case "title":
                EranoServices.messages().sendTitle(player, ChatColor.GOLD + "EranoAPI", ChatColor.GRAY + "subtitle", 10, 40, 10);
                return true;
            case "holo": {
                Hologram old = holograms.remove(player.getUniqueId());
                if (old != null) {
                    old.remove();
                }
                holograms.put(player.getUniqueId(), EranoServices.holograms().show(player.getEyeLocation().add(player.getLocation().getDirection().multiply(3)),
                        Arrays.asList(ChatColor.GOLD + "Spawn #3", "", ChatColor.GRAY + "only you see this"), Collections.singletonList(player)));
                player.sendMessage("Per viewer: " + EranoServices.holograms().isPerViewer());
                return true;
            }
            case "unholo": {
                Hologram old = holograms.remove(player.getUniqueId());
                if (old != null) {
                    old.remove();
                }
                return true;
            }
            default:
                return false;
        }
    }
}
