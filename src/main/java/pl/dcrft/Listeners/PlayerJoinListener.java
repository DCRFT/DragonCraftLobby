package pl.dcrft.Listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
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
import pl.dcrft.Managers.SessionManager;
import pl.dcrft.Utils.AnimationUtil;
import pl.dcrft.Utils.GroupUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerJoinListener implements Listener {
    public static DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!p.isOp()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                DatabaseManager.openConnection();
                try {
                    DatabaseManager.openConnection();

                    Statement statement = DatabaseManager.connection.createStatement();

                    ResultSet result = statement.executeQuery("SELECT ID FROM `" + DatabaseManager.table_bungee + "` WHERE nick = '" + p.getName() + "'");

                    boolean val = result.next();

                    String since = PlaceholderAPI.setPlaceholders(p, "%player_first_join_date%");

                    String rank = GroupUtil.getPlayerGroup(p.getName());

                    if (!val) {
                        statement.executeUpdate("INSERT INTO " + DatabaseManager.table_bungee + " (nick, ranga, since, online) VALUES ('" + p.getName() + "', '" + rank + "', '" + since + "', 'lobby', 'teraz');");
                        statement.executeUpdate("INSERT INTO " + DatabaseManager.table_survival + " (nick, kille, dedy, kdr, bloki, slub) VALUES ('" + p.getName() + "', '0', '0', '0', '0', 'brak'); ");
                        statement.executeUpdate("INSERT INTO " + DatabaseManager.table_skyblock + " (nick, kille, dedy, kdr, wyspa, slub) VALUES ('" + p.getName() + "', '0', '0', '0', '0', 'brak'); ");
                        //statement.executeUpdate("INSERT INTO staty_pvp (nick, kille, dedy, kdr, ranga, poziom, sj) VALUES ('" + p.getName() + "', '0', '0', '0', 'Gracz', '0', '0'); ");
                    } else {
                        statement.executeUpdate("UPDATE " + DatabaseManager.table_bungee + " SET ranga='" + rank + "', since='" + since + "', online='teraz' WHERE nick='" + p.getName() + "';");
                    }

                    ResultSet resultsurvi = statement.executeQuery("SELECT ID FROM `" + DatabaseManager.table_survival + "` WHERE nick = '" + p.getName() + "'");
                    boolean valsurvi = resultsurvi.next();

                    if (!valsurvi) {
                        statement.executeUpdate("INSERT INTO " + DatabaseManager.table_survival + " (nick, kille, dedy, kdr, bloki, czasgry, slub) VALUES ('" + p.getName() + "', '0', '0', '0', '0', '0', 'brak'); ");
                    }

                    ResultSet resultsky = statement.executeQuery("SELECT ID FROM `" + DatabaseManager.table_skyblock + "` WHERE nick = '" + p.getName() + "'");
                    boolean valsky = resultsky.next();

                    if (!valsky) {
                        statement.executeUpdate("INSERT INTO " + DatabaseManager.table_skyblock + " (nick, kille, dedy, kdr, poziom, czasgry, kasa, slub) VALUES ('" + p.getName() + "', '0', '0', '0', '0', '0', '0', 'brak'); ");
                    }

                    result.close();
                    resultsurvi.close();
                    resultsky.close();
                    statement.close();

                } catch (SQLException ex) {
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
