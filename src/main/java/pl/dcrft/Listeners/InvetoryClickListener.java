package pl.dcrft.Listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.LanguageManager;
import pl.dcrft.Managers.Profile.ProfileManager;
import pl.dcrft.Managers.Profile.ProfileType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvetoryClickListener implements Listener {
    public static DragonCraftLobby plugin = DragonCraftLobby.getInstance();
    List<Sound> list = new ArrayList<Sound>(Arrays.asList(
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
        String title = e.getView().getTitle();

        Player p = (Player) e.getWhoClicked();

        if(!p.isOp()) e.setCancelled(true);
        if (title.contains(LanguageManager.getMessage("selector.title"))) {

            e.getClickedInventory().close();

            if(e.getCurrentItem() != null) {
                String bungee = plugin.getConfig().getString("selector." + e.getSlot() + ".server");
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("Connect");
                    out.writeUTF(bungee);
                } catch (IOException exception) {
                }
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
        } else if (title.contains(LanguageManager.getMessage("music.title"))) {

            e.getClickedInventory().close();

            if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.BARRIER) {

                    p.stopAllSounds();
                    p.playSound(p.getLocation(), list.get(e.getSlot()-9), 100F, 1F);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 1F);
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER) {
                p.stopAllSounds();
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 0.5F);
            }
        } else if (title.contains(LanguageManager.getMessage("profile.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                switch (e.getCurrentItem().getType()) {
                    case IRON_PICKAXE:
                        ProfileManager.showProfile(p, title.replace(LanguageManager.getMessage("profile.title"), ""), ProfileType.SURVIVAL);
                        return;
                    case GRASS_BLOCK:
                        ProfileManager.showProfile(p, title.replace(LanguageManager.getMessage("profile.title"), ""), ProfileType.SKYBLOCK);
                }
            }

        }
    }
}