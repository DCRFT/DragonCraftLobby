package pl.dcrft.Managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.dcrft.Managers.Statistic.ServerType;
import pl.dcrft.Managers.Statistic.StatisticGUIManager;
import pl.dcrft.Utils.ConfigUtil;

public class CommandManager implements CommandExecutor {

    public boolean onCommand(final @NotNull CommandSender sender, final Command cmd, final @NotNull String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("gracz")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("§e§lDragon§6§lCraft§e » §cWygląda na to, że jesteś konsolą! Ta komenda tak nie zadziała :/");
                return false;
            } else if (args.length == 0) {
                StatisticGUIManager.showStatistics(ServerType.Survival, (Player) sender, sender.getName());
                return true;
            } else {
                StatisticGUIManager.showStatistics(ServerType.Survival, (Player) sender, args[0]);
            }
        } else if (cmd.getName().equalsIgnoreCase("dcl")) {
            if (sender.hasPermission("dcc.adm")) {
                if (args.length == 0) {
                    sender.sendMessage("§e§lDragon§6§lCraft§b§lLobby");
                    MessageManager.sendMessageList(sender, "help");
                    return false;
                } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("przeladuj")) {
                    ConfigUtil.reloadFiles();
                    MessageManager.sendPrefixedMessage(sender, "maintenance.reload_plugin");
                    return true;
                }
            } else {
                MessageManager.sendPrefixedMessage(sender, "unknown-command");
            }
        }
        return false;
    }
}

