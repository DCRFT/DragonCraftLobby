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

    public static File dataFile;
    public static FileConfiguration data;

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
        } catch (InvalidConfigurationException | IOException var2) {
             ErrorUtil.logError(ErrorReason.DATA);
            var2.printStackTrace();
        }

    }
    public static void saveDatabaseConfig() {
        try {
            databaseConfig.save(databaseConfigFile);

        } catch (Exception e) {
             ErrorUtil.logError(ErrorReason.CONFIG);
            e.printStackTrace();
        }
    }

    public static FileConfiguration getDataFile() {
        return data;
    }

    public static void createDataFile() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            plugin.saveResource("data.yml", false);
        }

        data = new YamlConfiguration();

        try {
            data.load(dataFile);
        } catch (InvalidConfigurationException | IOException var2) {
             ErrorUtil.logError(ErrorReason.DATA);
            var2.printStackTrace();
        }

    }
    public static void saveData() {
        try {
            data.save(dataFile);
        } catch (Exception e) {
             ErrorUtil.logError(ErrorReason.DATA);
            e.printStackTrace();
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
        }

        messagesConfig = new YamlConfiguration();

        try {
            messagesConfig.load(messagesConfigFile);
        } catch (InvalidConfigurationException | IOException var2) {
             ErrorUtil.logError(ErrorReason.MESSAGES);
            var2.printStackTrace();
        }

    }
    public static void saveMessagesFile() {
        try {
            messagesConfig.save(messagesConfigFile);

        } catch (Exception e) {
             ErrorUtil.logError(ErrorReason.MESSAGES);
            e.printStackTrace();
        }
    }

}
