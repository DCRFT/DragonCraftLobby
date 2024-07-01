package pl.dcrft.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.MaintenanceManager;
import pl.dcrft.Managers.MessageManager;

import java.util.Arrays;

public class CommandPreprocessListener implements Listener {
    private static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().length() > 1 && e.getMessage().startsWith("/")) {



            String[] args = e.getMessage().split(" ");
            args = Arrays.copyOfRange(args, 1, args.length);

            Player p = e.getPlayer();
            String command = e.getMessage().substring(1);
            command = command.split(" ")[0].replace(":", "%colon%");

            // TODO remove & move crestart to DragonCraftUtils
            if(command.equalsIgnoreCase("restart")){
                e.setCancelled(true);
                if (p.hasPermission("r.adm")) {
                    if (args.length == 0) {
                        MaintenanceManager.restartServer();
                    } else {
                        if (!args[0].chars().allMatch(Character::isDigit) || Integer.parseInt(args[0]) < 1) {
                            MessageManager.sendPrefixedMessage(p, "maintenance.wrong_value");
                        } else {
                            MaintenanceManager.restartServer(Integer.parseInt(args[0]));
                        }
                    }
                } else {
                    return;
                }
            }

            String aliasResult = plugin.getConfig().getString("aliases." + command);
            if (aliasResult != null) {
                String userArguments = e.getMessage().substring(command.length() + 1);
                e.setMessage(e.getMessage().charAt(0) + aliasResult + userArguments);
            }
        }
    }
}