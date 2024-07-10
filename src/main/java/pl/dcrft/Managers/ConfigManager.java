package pl.dcrft.Managers;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Utils.ErrorUtils.ErrorReason;
import pl.dcrft.Utils.ErrorUtils.ErrorUtil;

import java.io.File;
import java.io.IOException;


public class ConfigManager {
    public static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    public static File databaseConfigFile;
    public static FileConfiguration databaseConfig;

    public static File messagesConfigFile;
    public static FileConfiguration messagesConfig;

    public static void CheckConfig() {
        if (plugin.getConfig() == null) {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
        }
    }
    public static FileConfiguration getDatabaseFile() {
        return databaseConfig;
    }

    public static void createCustomConfig() {
        databaseConfigFile = new File(plugin.getDataFolder(), "database.yml");
        if (!databaseConfigFile.exists()) {
            databaseConfigFile.getParentFile().mkdirs();
            plugin.saveResource("database.yml", false);
        }

        databaseConfig = new YamlConfiguration();

        try {
            databaseConfig.load(databaseConfigFile);
        } catch (InvalidConfigurationException | IOException e) {
            ErrorUtil.logError(ErrorReason.DATABASE);
            throw new RuntimeException(e);
        }

    }


    public static FileConfiguration getMessagesFile() {
        return messagesConfig;
    }

    public static void createMessagesFile() {
        messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            messagesConfigFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
            plugin.saveResource("messages.yml", false);
        }

        messagesConfig = new YamlConfiguration();

        try {
            messagesConfig.load(messagesConfigFile);
        } catch (InvalidConfigurationException | IOException e) {
             ErrorUtil.logError(ErrorReason.MESSAGES);
            throw new RuntimeException(e);
        }

    }

}
