package pl.dcrft.Listeners;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.*;
import pl.dcrft.Utils.AnimationUtil;

import java.util.concurrent.TimeUnit;

public class PlayerJoinListener implements Listener {
    public static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e){


        Player p = e.getPlayer();


        if(!p.hasPlayedBefore()){
        LuckPerms lp = DragonCraftLobby.getLuckPerms();
        User user = lp.getUserManager().getUser(p.getUniqueId());
            String group = "nowy";
            InheritanceNode node = InheritanceNode.builder(group).value(true).expiry(7, TimeUnit.DAYS).build();
            user.data().add(node);
            lp.getUserManager().saveUser(user);
        }

        p.getInventory().clear();

        p.getInventory().setHeldItemSlot(4);

        ItemStack music = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.music")));
        ItemMeta musicMeta = music.getItemMeta();
        musicMeta.displayName(Component.text(LanguageManager.getMessage("items.music")));
        music.setItemMeta(musicMeta);


        ItemStack players = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.players.visible")));
        ItemMeta playersMeta = players.getItemMeta();
        playersMeta.displayName(Component.text(LanguageManager.getMessage("items.players.visible")));
        players.setItemMeta(playersMeta);

        ItemStack wh_on = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.whitelist.enabled")));
        ItemMeta wh_onMeta = wh_on.getItemMeta();
        wh_onMeta.displayName(Component.text(LanguageManager.getMessage("items.whitelist.enabled")));
        wh_on.setItemMeta(wh_onMeta);

        ItemStack wh_off = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.whitelist.disabled")));
        ItemMeta wh_offMeta = wh_off.getItemMeta();
        wh_offMeta.displayName(Component.text(LanguageManager.getMessage("items.whitelist.disabled")));
        wh_off.setItemMeta(wh_offMeta);

        p.getInventory().setItem(plugin.getConfig().getInt("slots.music"), music);
        p.getInventory().setItem(plugin.getConfig().getInt("slots.players"), players);

        if(p.hasPermission("dcl.adm")) {
            if (plugin.getServer().hasWhitelist()) {
                p.getInventory().setItem(plugin.getConfig().getInt("slots.whitelist"), wh_on);
            } else {
                p.getInventory().setItem(plugin.getConfig().getInt("slots.whitelist"), wh_off);
            }
        }

        AnimationUtil.playAnimation(p, LanguageManager.getMessageList("welcome.title"), LanguageManager.getMessage("welcome.subtitle"));
    }
}
