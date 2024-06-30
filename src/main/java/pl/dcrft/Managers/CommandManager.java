package pl.dcrft.Managers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.Statistic.ServerType;
import pl.dcrft.Managers.Statistic.StatisticGUIManager;
import pl.dcrft.Utils.ConfigUtil;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private static DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    public final ArrayList<SessionManager> list = new ArrayList<>();
    final String prefix = LanguageManager.getMessage("prefix");

    public boolean onCommand(final @NotNull CommandSender sender, final Command cmd, final @NotNull String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("restart") && sender.hasPermission("r.adm")) {
            if (args.length == 0) {
                MaintenanceManager.restartServer();
            } else {
                if (!args[0].chars().allMatch(Character::isDigit) || Integer.parseInt(args[0]) < 1) {
                    MessageManager.sendPrefixedMessage(sender, "maintenance.wrong_value");
                } else {
                    MaintenanceManager.restartServer(Integer.parseInt(args[0]));
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("czat")) {
            if (!sender.hasPermission("panel.mod")) {
                MessageManager.sendPrefixedMessage(sender, "notfound");
            } else {
                if (ConfigManager.getDataFile().getBoolean("czat")) {
                    ConfigManager.getDataFile().set("czat", false);
                    ConfigManager.saveData();
                    plugin.getServer().broadcastMessage(prefix + LanguageManager.getMessage("chat.disabled"));
                } else {
                    ConfigManager.getDataFile().set("czat", true);
                    ConfigManager.saveData();
                    plugin.getServer().broadcastMessage(prefix + LanguageManager.getMessage("chat.enabled"));
                }
            }
            return false;
        } else if (cmd.getName().equalsIgnoreCase("ac")) {
            if (!(sender instanceof Player)) {
                MessageManager.sendMessage(sender, "console_error");
                return false;
            }
            if (!sender.hasPermission("admin.chat")) {
                MessageManager.sendPrefixedMessage(sender, "notfound");
                return false;
            } else {
                if (!ConfigManager.getDataFile().contains("players." + sender.getName())) {
                    ConfigManager.getDataFile().set("players." + sender.getName() + ":", null);
                    ConfigManager.getDataFile().set("players." + sender.getName() + ".adminchat", true);
                    ConfigManager.getDataFile().set("players." + sender.getName() + ".modchat", false);
                    sender.sendMessage(LanguageManager.getMessage("staffchat.adminchat.title") + LanguageManager.getMessage("staffchat.modchat.spacer") + LanguageManager.getMessage("staffchat.enabled"));

                    ConfigManager.saveData();
                    return true;
                }
                if (ConfigManager.getDataFile().getBoolean("players." + sender.getName() + ".stream")) {
                    MessageManager.sendPrefixedMessage(sender, "staffchat.stream.error");
                    return false;
                }
                if (ConfigManager.getDataFile().getBoolean("players." + sender.getName() + ".adminchat")) {
                    ConfigManager.getDataFile().set("players." + sender.getName() + ".adminchat", false);
                    sender.sendMessage(LanguageManager.getMessage("staffchat.adminchat.title") + LanguageManager.getMessage("staffchat.modchat.spacer") + LanguageManager.getMessage("staffchat.disabled"));


                    ConfigManager.saveData();
                    return true;
                } else if (!ConfigManager.getDataFile().getBoolean("players." + sender.getName() + ".adminchat")) {
                    ConfigManager.getDataFile().set("players." + sender.getName() + ".adminchat", true);
                    sender.sendMessage(LanguageManager.getMessage("staffchat.adminchat.title") + LanguageManager.getMessage("staffchat.modchat.spacer") + LanguageManager.getMessage("staffchat.enabled"));

                    ConfigManager.saveData();
                    return true;
                }
                ConfigManager.saveData();
            }
        } else if (cmd.getName().equalsIgnoreCase("mc")) {
            if (!sender.hasPermission("mod.chat")) {
                MessageManager.sendPrefixedMessage(sender, "notfound");
                return false;
            } else {
                if (!ConfigManager.getDataFile().contains("players." + sender.getName())) {
                    ConfigManager.getDataFile().set("players." + sender.getName() + ":", null);
                    ConfigManager.getDataFile().set("players." + sender.getName() + ".modchat", true);
                    ConfigManager.getDataFile().set("players." + sender.getName() + ".adminchat", false);
                    sender.sendMessage(LanguageManager.getMessage("staffchat.modchat.title") + LanguageManager.getMessage("staffchat.modchat.spacer") + LanguageManager.getMessage("staffchat.enabled"));
                    ConfigManager.saveData();
                    return true;
                }
                if (ConfigManager.getDataFile().getBoolean("players." + sender.getName() + ".stream")) {
                    MessageManager.sendPrefixedMessage(sender, "staffchat.stream.error");
                    return false;
                }
                if (ConfigManager.getDataFile().getBoolean("players." + sender.getName() + ".modchat")) {
                    ConfigManager.getDataFile().set("players." + sender.getName() + ".modchat", false);
                    sender.sendMessage(LanguageManager.getMessage("staffchat.modchat.title") + LanguageManager.getMessage("staffchat.modchat.spacer") + LanguageManager.getMessage("staffchat.disabled"));
                    ConfigManager.saveData();
                    return true;
                } else if (!ConfigManager.getDataFile().getBoolean("players." + sender.getName() + ".modchat")) {
                    ConfigManager.getDataFile().set("players." + sender.getName() + ".modchat", true);
                    sender.sendMessage(LanguageManager.getMessage("staffchat.modchat.title") + LanguageManager.getMessage("staffchat.modchat.spacer") + LanguageManager.getMessage("staffchat.enabled"));
                    ConfigManager.saveData();
                    return true;
                }
                ConfigManager.saveData();
            }
        } else if (cmd.getName().equalsIgnoreCase("gracz")) {

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
        } else if (cmd.getName().equalsIgnoreCase("cc")) {
            if (sender.hasPermission("cc.adm")) {
                for (int i = 0; i < 100; ++i) {
                    Bukkit.getServer().broadcastMessage("");
                }
                Bukkit.getServer().broadcastMessage(prefix + LanguageManager.getMessage("chat.cleared"));
                return true;
            }
            if (!sender.hasPermission("cc.adm")) {
                MessageManager.sendPrefixedMessage(sender, "unknown-command");
            }
            return true;

        } else if (cmd.getName().equalsIgnoreCase("crestart")) {
            if (sender.hasPermission("r.adm")) {
                if (args.length == 0) {
                    MaintenanceManager.restartServer();
                } else {
                    if (!args[0].chars().allMatch(Character::isDigit) || Integer.parseInt(args[0]) < 1) {
                        MessageManager.sendPrefixedMessage(sender, "maintenance.wrong_value");
                    } else {
                        MaintenanceManager.restartServer(Integer.parseInt(args[0]));
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }
}

