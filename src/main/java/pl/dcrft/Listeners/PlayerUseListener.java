package pl.dcrft.Listeners;

import fr.xephi.authme.api.v3.AuthMeApi;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.LanguageManager;
import pl.dcrft.Managers.Statistic.ServerType;
import pl.dcrft.Managers.Statistic.StatisticGUIManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerUseListener implements Listener {
    public static DragonCraftLobby plugin = DragonCraftLobby.getInstance();
    List<Material> list = new ArrayList<Material>(Arrays.asList(
            Material.MUSIC_DISC_5,
            Material.MUSIC_DISC_11,
            Material.MUSIC_DISC_13,
            Material.MUSIC_DISC_BLOCKS,
            Material.MUSIC_DISC_CAT,
            Material.MUSIC_DISC_CHIRP,
            Material.MUSIC_DISC_FAR,
            Material.MUSIC_DISC_MALL,
            Material.MUSIC_DISC_MELLOHI,
            Material.MUSIC_DISC_OTHERSIDE,
            Material.MUSIC_DISC_PIGSTEP,
            Material.MUSIC_DISC_STAL,
            Material.MUSIC_DISC_STRAD,
            Material.MUSIC_DISC_WAIT,
            Material.MUSIC_DISC_WARD));
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();


        ItemStack itemStack = p.getInventory().getItemInMainHand();

        if (itemStack.getItemMeta() != null && AuthMeApi.getInstance().isAuthenticated(p) && itemStack.getItemMeta().displayName() != null) {

            if (itemStack.getItemMeta().displayName().equals(Component.text(LanguageManager.getMessage("items.selector")))) {
                e.setCancelled(true);

                Inventory inventory = Bukkit.createInventory(null, 45, LanguageManager.getMessage("selector.title"));


                for (String i : plugin.getConfig().getConfigurationSection("selector").getKeys(false)) {
                    int j = Integer.parseInt(i);

                    ItemStack is = new ItemStack(Material.getMaterial(plugin.getConfig().getString("selector." + j + ".item")));
                    ItemMeta meta = is.getItemMeta();
                    meta.displayName(Component.text(plugin.getConfig().getString("selector." + j + ".name")));
                    List<String> list = plugin.getConfig().getStringList("selector." + j + ".lore");
                    meta.setLore(list);
                    is.setItemMeta(meta);

                    inventory.setItem(j, is);
                }

                p.openInventory(inventory);


                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 1F);

            } else if (itemStack.getItemMeta().displayName().equals(Component.text(LanguageManager.getMessage("items.music")))) {
                e.setCancelled(true);

                Inventory inventory = Bukkit.createInventory(null, 36, LanguageManager.getMessage("music.title"));

                int j = 9;

                for (Material m : list) {
                    ItemStack is = new ItemStack(m);
                    inventory.setItem(j, is);
                    j++;
                }

                ItemStack is = new ItemStack(Material.BARRIER);
                ItemMeta meta = is .getItemMeta();
                meta.displayName(Component.text(LanguageManager.getMessage("music.stop")));
                is.setItemMeta(meta);
                inventory.setItem(35, is);

                p.openInventory(inventory);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 1F);

            } else if (itemStack.getItemMeta().displayName().equals(Component.text(LanguageManager.getMessage("items.profile")))) {
                e.setCancelled(true);

                StatisticGUIManager.showStatistics(ServerType.Survival, p, p.getName());

                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 1F);

            } else if (itemStack.getItemMeta().displayName().equals(Component.text(LanguageManager.getMessage("items.players.visible")))) {
                e.setCancelled(true);

                ItemStack is = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.players.hidden")));
                ItemMeta meta = is.getItemMeta();
                meta.displayName(Component.text(LanguageManager.getMessage("items.players.hidden")));
                is.setItemMeta(meta);

                for(Player online : Bukkit.getOnlinePlayers()){
                    p.hidePlayer(plugin, online);
                }

                p.getInventory().setItem(plugin.getConfig().getInt("slots.players"), is);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 0.5F);

            } else if (itemStack.getItemMeta().displayName().equals(Component.text(LanguageManager.getMessage("items.players.hidden")))) {
                e.setCancelled(true);

                ItemStack is = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.players.visible")));
                ItemMeta meta = is.getItemMeta();
                meta.displayName(Component.text(LanguageManager.getMessage("items.players.visible")));
                is.setItemMeta(meta);

                for(Player online : Bukkit.getOnlinePlayers()){
                    p.showPlayer(plugin, online);
                }
                p.getInventory().setItem(plugin.getConfig().getInt("slots.players"), is);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 1F);

            } else if (itemStack.getItemMeta().displayName().equals(Component.text(LanguageManager.getMessage("items.whitelist.enabled")))) {
                e.setCancelled(true);

                ItemStack is = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.whitelist.disabled")));
                ItemMeta meta = is.getItemMeta();
                meta.displayName(Component.text(LanguageManager.getMessage("items.whitelist.disabled")));
                is.setItemMeta(meta);

                plugin.getServer().setWhitelist(false);

                p.getInventory().setItem(plugin.getConfig().getInt("slots.whitelist"), is);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 0.5F);

            } else if (itemStack.getItemMeta().displayName().equals(Component.text(LanguageManager.getMessage("items.whitelist.disabled")))) {
                e.setCancelled(true);

                ItemStack is = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.whitelist.enabled")));
                ItemMeta meta = is.getItemMeta();
                meta.displayName(Component.text(LanguageManager.getMessage("items.whitelist.enabled")));
                is.setItemMeta(meta);

                plugin.getServer().setWhitelist(true);

                p.getInventory().setItem(plugin.getConfig().getInt("slots.whitelist"), is);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100F, 1F);

            }

        }
    }
}
