package pl.dcrft.Listeners;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
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
import pl.dcrft.Utils.ErrorUtils.ErrorReason;
import pl.dcrft.Utils.ErrorUtils.ErrorUtil;
import pl.dcrft.Utils.GroupUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PlayerJoinListener implements Listener {
    public static DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e){


        final DatabaseManager databaseManager =  new DatabaseManager();
        Player p = e.getPlayer();


        if(!p.hasPlayedBefore()){
        LuckPerms lp = plugin.getLuckPerms();
        User user = lp.getUserManager().getUser(p.getUniqueId());
            String group = "nowy";
            InheritanceNode node = InheritanceNode.builder(group).value(true).expiry(7, TimeUnit.DAYS).build();
            DataMutateResult res = user.data().add(node);
         lp.getUserManager().saveUser(user);
        }

        if(!p.isOp()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {

                    final ResultSet result = databaseManager.query("SELECT ID FROM `" + DatabaseManager.table_bungee + "` WHERE nick = '" + p.getName() + "'");

                    final SimpleDateFormat dtf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Date date = new Date(p.getFirstPlayed());
                    final String since = dtf.format(date);

                    String rank = GroupUtil.getPlayerGroup(p.getName());

                    if (!result.next()) {
                        databaseManager.update("INSERT INTO " + DatabaseManager.table_bungee + " (nick, ranga, since, online) VALUES ('" + p.getName() + "', '" + rank + "', '" + since + "', 'teraz');");
                        databaseManager.update("INSERT INTO " + DatabaseManager.table_survival + " (nick, kille, dedy, kdr, bloki, slub) VALUES ('" + p.getName() + "', '0', '0', '0', '0', 'brak'); ");
                        databaseManager.update("INSERT INTO " + DatabaseManager.table_skyblock + " (nick, kille, dedy, kdr, poziom, kasa, slub) VALUES ('" + p.getName() + "', '0', '0', '0', '0', '0', 'brak'); ");
                        databaseManager.update("INSERT INTO " + DatabaseManager.table_pvp +  " (nick, kille, dedy, asysty, kdr, ranking, poziom, kasa, killstreak, czasgry) VALUES ('" + p.getName() + "', '0', '0', '0', '0', '1000', '1', '0', '0', '0'); ");
                    } else {
                        String online = "teraz";
                        if(p.hasPermission("dcc.login.admin")) online = "-";
                        databaseManager.update("UPDATE " + DatabaseManager.table_bungee + " SET ranga='" + rank + "', since='" + since + "', online='" + online + "' WHERE nick='" + p.getName() + "';");
                    }
                    result.close();

                    ResultSet resultsurvi = databaseManager.query("SELECT ID FROM `" + DatabaseManager.table_survival + "` WHERE nick = '" + p.getName() + "'");
                    if (!resultsurvi.next()) {
                        databaseManager.update("INSERT INTO " + DatabaseManager.table_survival + " (nick, kille, dedy, kdr, bloki, czasgry, slub) VALUES ('" + p.getName() + "', '0', '0', '0', '0', '0', 'brak'); ");
                    }
                    resultsurvi.close();

                    ResultSet resultsky = databaseManager.query("SELECT ID FROM `" + DatabaseManager.table_skyblock + "` WHERE nick = '" + p.getName() + "'");
                    if (!resultsky.next()) {
                        databaseManager.update("INSERT INTO " + DatabaseManager.table_skyblock + " (nick, kille, dedy, kdr, poziom, czasgry, kasa, slub) VALUES ('" + p.getName() + "', '0', '0', '0', '0', '0', '0', 'brak'); ");
                    }
                    resultsky.close();

                    ResultSet resultpvp = databaseManager.query("SELECT ID FROM `" + DatabaseManager.table_pvp + "` WHERE nick = '" + p.getName() + "'");
                    if (!resultpvp.next()) {
                        databaseManager.update("INSERT INTO " + DatabaseManager.table_pvp +  " (nick, kille, dedy, asysty, kdr, ranking, poziom, kasa, killstreak, czasgry) VALUES ('" + p.getName() + "', '0', '0', '0', '0', '1000', '1', '0', '0', '0'); ");
                    }
                    resultpvp.close();

                    ResultSet resulthns = databaseManager.query("SELECT ID FROM `" + DatabaseManager.table_hns + "` WHERE nick = '" + p.getName() + "'");
                    if (!resulthns.next()) {
                        databaseManager.update("INSERT INTO " + DatabaseManager.table_hns +  " (nick, kille, dedy, kasa, czasgry) VALUES ('" + p.getName() + "', '0', '0', '0', '0'); ");
                    }
                    resulthns.close();

                } catch (SQLException ex) {
                    ErrorUtil.logError(ErrorReason.DATABASE);
                    throw new RuntimeException(ex);
                }


            });
        }

        SessionManager newSession = new SessionManager(e.getPlayer());
        SessionManager.list.add(newSession);

        p.getInventory().clear();

        p.getInventory().setHeldItemSlot(plugin.getConfig().getInt("slots.selector"));

        ItemStack music = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.music")));
        ItemMeta musicMeta = music.getItemMeta();
        musicMeta.displayName(Component.text(LanguageManager.getMessage("items.music")));
        music.setItemMeta(musicMeta);

        ItemStack profile = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta profileMeta = (SkullMeta) profile.getItemMeta();
        profileMeta.setOwningPlayer(p);
        profileMeta.displayName(Component.text(LanguageManager.getMessage("items.profile")));
        profile.setItemMeta(profileMeta);

        ItemStack players = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.players.visible")));
        ItemMeta playersMeta = players.getItemMeta();
        playersMeta.displayName(Component.text(LanguageManager.getMessage("items.players.visible")));
        players.setItemMeta(playersMeta);

        ItemStack selector = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.selector")));
        ItemMeta selectorMeta = selector.getItemMeta();
        selectorMeta.displayName(Component.text(LanguageManager.getMessage("items.selector")));
        selector.setItemMeta(selectorMeta);

        ItemStack wh_on = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.whitelist.enabled")));
        ItemMeta wh_onMeta = wh_on.getItemMeta();
        wh_onMeta.displayName(Component.text(LanguageManager.getMessage("items.whitelist.enabled")));
        wh_on.setItemMeta(wh_onMeta);

        ItemStack wh_off = new ItemStack(Material.getMaterial(plugin.getConfig().getString("items.whitelist.disabled")));
        ItemMeta wh_offMeta = wh_off.getItemMeta();
        wh_offMeta.displayName(Component.text(LanguageManager.getMessage("items.whitelist.disabled")));
        wh_off.setItemMeta(wh_offMeta);

        p.getInventory().setItem(plugin.getConfig().getInt("slots.music"), music);
        p.getInventory().setItem(plugin.getConfig().getInt("slots.profile"), profile);
        p.getInventory().setItem(plugin.getConfig().getInt("slots.players"), players);
        p.getInventory().setItem(plugin.getConfig().getInt("slots.selector"), selector);

        if(p.hasPermission("dcl.adm")) {
            if (plugin.getServer().hasWhitelist()) {
                p.getInventory().setItem(plugin.getConfig().getInt("slots.whitelist"), wh_on);
            } else {
                p.getInventory().setItem(plugin.getConfig().getInt("slots.whitelist"), wh_off);
            }
        }

        AnimationUtil.playAnimation(p, LanguageManager.getMessageList("welcome.title"), LanguageManager.getMessage("welcome.subtitle"));
        BossBarManager.startBroadcasting(p);

        new PanelManager().showRepeatingPanel(p);

    }

}
