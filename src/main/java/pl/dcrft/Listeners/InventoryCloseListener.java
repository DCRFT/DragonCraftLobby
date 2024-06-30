package pl.dcrft.Listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import pl.dcrft.Managers.LanguageManager;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        String title = e.getView().getTitle();
        if (title.contains(LanguageManager.getMessage("music.title")) || title.contains(LanguageManager.getMessage("statistics.title"))) {
             Player p = (Player) e.getPlayer();
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 0.5F);
        }
    }

}
