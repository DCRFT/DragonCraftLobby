package pl.dcrft.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerQuitListener implements Listener {
    private static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(p.hasPermission("dcc.login.admin")) return;

        final DatabaseManager databaseManager = new DatabaseManager();

            final SimpleDateFormat dtf = new SimpleDateFormat("dd.MM.yyyy 'o' HH:mm");
            Date date = new Date(System.currentTimeMillis());

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

                databaseManager.update("UPDATE " + DatabaseManager.table_bungee + " SET online='" + dtf.format(date) + "' WHERE nick='" + p.getName() + "'");


            });

    }

}
