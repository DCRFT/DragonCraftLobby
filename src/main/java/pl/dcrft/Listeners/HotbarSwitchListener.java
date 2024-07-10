package pl.dcrft.Listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class HotbarSwitchListener implements Listener {
    @EventHandler
    public void onHotbarSwitch(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 100F, 2F);
    }
}
