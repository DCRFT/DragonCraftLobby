package pl.dcrft;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pl.dcrft.Listeners.*;
import pl.dcrft.Managers.CommandManager;
import pl.dcrft.Managers.DatabaseManager;
import pl.dcrft.Managers.LanguageManager;
import pl.dcrft.Managers.SessionManager;
import pl.dcrft.Utils.CommandUtils.CommandRunUtil;
import pl.dcrft.Utils.ConfigUtil;
import pl.dcrft.Utils.ErrorUtils.ErrorReason;
import pl.dcrft.Utils.ErrorUtils.ErrorUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DragonCraftLobby extends JavaPlugin implements Listener, CommandExecutor {
    private static DragonCraftLobby instance;
    public static DragonCraftLobby getInstance() {
        return instance;
    }
    public static LuckPerms getLuckPerms() {
        return luckPermsApi;
    }
    public static LuckPerms luckPermsApi;
    public Map<String, Object> filters;

    public DragonCraftLobby() {
        this.filters = new HashMap<>();
    }

    @Override
    public void onEnable() {

        instance = this;
        ConfigUtil.initializeFiles();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerUseListener(), this);
        getServer().getPluginManager().registerEvents(new InvetoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new CommandPreprocessListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new UnknownCommandEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        getLogger().info(LanguageManager.getMessage("plugin.header"));
        getLogger().info("§e§lDragon§6§lCraft§b§lLobby");
        getLogger().info(LanguageManager.getMessage("plugin.enabled") + getDescription().getVersion());
        getLogger().info(LanguageManager.getMessage("plugin.footer"));

        this.filters = this.getConfig().getConfigurationSection("filters").getValues(true);

        List<Command> commands = PluginCommandYamlParser.parse(this);
        for (Command command : commands) {
            getCommand(command.getName()).setExecutor(new CommandManager());
        }
        for(String cmd : getConfig().getConfigurationSection("aliases").getKeys(false)){
            Bukkit.getCommandMap().register(cmd, new CommandRunUtil(cmd));
        }
        SessionManager.getRunnable().runTaskTimer(this, 0L, 1200L);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();

        }

    }
    @Override
    public void onDisable() {
        if (DatabaseManager.connection != null) {
            try {
                DatabaseManager.connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                ErrorUtil.logError(ErrorReason.DATABASE);
            }
        }
        getLogger().info(LanguageManager.getMessage("plugin.header"));
        getLogger().info("§e§lDragon§6§lCraft§a§lCore");
        getLogger().info(LanguageManager.getMessage("plugin.disabled") + getDescription().getVersion());
        getLogger().info(LanguageManager.getMessage("plugin.footer"));
    }
}
