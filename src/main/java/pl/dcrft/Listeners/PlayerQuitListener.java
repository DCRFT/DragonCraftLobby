package pl.dcrft.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.DatabaseManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerQuitListener implements Listener {
    private static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(!p.isOp()) {
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy 'o' HH:mm");
            final LocalDateTime now = LocalDateTime.now();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                DatabaseManager.openConnection();
                final Statement statement;
                try {
                    statement = DatabaseManager.connection.createStatement();
                    statement.executeUpdate("UPDATE staty_ogolem SET online='" + dtf.format(now) + "' WHERE nick='" + p.getName() + "'");
                    statement.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            });
        }
    }

}
