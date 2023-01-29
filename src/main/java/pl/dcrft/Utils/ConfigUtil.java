package pl.dcrft.Utils;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.ConfigManager;
import pl.dcrft.Managers.LanguageManager;

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
        ConfigManager.createDataFile();
        ConfigManager.createDisabledFile();
        LanguageManager.load();
    }

    public static void reloadFiles() {
        ConfigManager.saveData();

        plugin.reloadConfig();
        ConfigManager.data = YamlConfiguration.loadConfiguration(ConfigManager.dataFile);
        ConfigManager.databaseConfig = YamlConfiguration.loadConfiguration(ConfigManager.databaseConfigFile);
        ConfigManager.messagesConfig = YamlConfiguration.loadConfiguration(ConfigManager.messagesConfigFile);
        ConfigManager.disabledConfig = YamlConfiguration.loadConfiguration(ConfigManager.disabledConfigFile);

        plugin.filters = plugin.getConfig().getConfigurationSection("filters").getValues(true);
    }
}