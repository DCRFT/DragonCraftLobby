package pl.dcrft.Listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.LanguageManager;
import pl.dcrft.Managers.Statistic.ServerType;
import pl.dcrft.Managers.Statistic.StatisticGUIManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvetoryClickListener implements Listener {
    public static DragonCraftLobby plugin = DragonCraftLobby.getInstance();
    List<Sound> list = new ArrayList<>(Arrays.asList(
            Sound.MUSIC_DISC_5,
            Sound.MUSIC_DISC_11,
            Sound.MUSIC_DISC_13,
            Sound.MUSIC_DISC_BLOCKS,
            Sound.MUSIC_DISC_CAT,
            Sound.MUSIC_DISC_CHIRP,
            Sound.MUSIC_DISC_FAR,
            Sound.MUSIC_DISC_MALL,
            Sound.MUSIC_DISC_MELLOHI,
            Sound.MUSIC_DISC_OTHERSIDE,
            Sound.MUSIC_DISC_PIGSTEP,
            Sound.MUSIC_DISC_STAL,
            Sound.MUSIC_DISC_STRAD,
            Sound.MUSIC_DISC_WAIT,
            Sound.MUSIC_DISC_WARD));

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();


        String title = e.getView().getTitle();

        if(!p.isOp()) e.setCancelled(true);
        if (title.contains(LanguageManager.getMessage("music.title"))) {

            e.getClickedInventory().close();

            if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.BARRIER) {

                    p.stopAllSounds();
                    p.playSound(p.getLocation(), list.get(e.getSlot()-9), 100F, 1F);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 1F);
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER) {
                p.stopAllSounds();
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 0.5F);
            }
        } else if (title.contains(LanguageManager.getMessage("statistics.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if(!e.getCurrentItem().getEnchantments().isEmpty()) return;
                switch (e.getCurrentItem().getType()) {
                    case IRON_PICKAXE:
                        StatisticGUIManager.showStatistics(ServerType.Survival, p, title.replace(LanguageManager.getMessage("statistics.title"), ""));
                        return;
                    case GRASS_BLOCK:
                        //StatisticGUIManager.showStatistics(ServerType.SkyBlock, p, title.replace(LanguageManager.getMessage("statistics.title"), ""));
                        return;
                    case IRON_SWORD:
                        //StatisticGUIManager.showStatistics(ServerType.PvP, p, title.replace(LanguageManager.getMessage("statistics.title"), ""));
                        return;
                    case CRAFTING_TABLE:
                        //StatisticGUIManager.showStatistics(ServerType.HNS, p, title.replace(LanguageManager.getMessage("statistics.title"), ""));
                }
            }

        }
    }
}