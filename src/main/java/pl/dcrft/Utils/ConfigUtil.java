package pl.dcrft.Utils;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.ConfigManager;

import java.io.File;


public class ConfigUtil {
    public static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    public static void initializeFiles() {
        final File file = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            plugin.saveDefaultConfig();
            plugin.getConfig().options().copyDefaults(true);
        } else {
            ConfigManager.CheckConfig();
            plugin.saveConfig();
            plugin.reloadConfig();
        }

        ConfigManager.createMessagesFile();
        ConfigManager.createCustomConfig();
    }

    public static void reloadFiles() {

        plugin.reloadConfig();
        ConfigManager.databaseConfig = YamlConfiguration.loadConfiguration(ConfigManager.databaseConfigFile);
        ConfigManager.messagesConfig = YamlConfiguration.loadConfiguration(ConfigManager.messagesConfigFile);
    }
}