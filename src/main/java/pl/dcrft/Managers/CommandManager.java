package pl.dcrft.Managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.dcrft.Utils.ConfigUtil;

public class CommandManager implements CommandExecutor {

    public boolean onCommand(final @NotNull CommandSender sender, final Command cmd, final @NotNull String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("dcl")) {
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

