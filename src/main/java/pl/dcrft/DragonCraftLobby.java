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
import pl.dcrft.Utils.ConfigUtil;
import pl.dcrft.Utils.ErrorUtils.ErrorReason;
import pl.dcrft.Utils.ErrorUtils.ErrorUtil;

import java.sql.SQLException;
import java.util.List;

public final class DragonCraftLobby extends JavaPlugin implements Listener, CommandExecutor {
    private static DragonCraftLobby instance;

    public static DragonCraftLobby getInstance() {
        return instance;
    }

    public static LuckPerms getLuckPerms() {
        return luckPermsApi;
    }

    public static LuckPerms luckPermsApi;

    @Override
    public void onEnable() {

        instance = this;
        ConfigUtil.initializeFiles();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerUseListener(), this);
        getServer().getPluginManager().registerEvents(new InvetoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new HotbarSwitchListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);


        getLogger().info(LanguageManager.getMessage("plugin.header"));
        getLogger().info("§e§lDragon§6§lCraft§b§lLobby");
        getLogger().info(LanguageManager.getMessage("plugin.enabled") + getDescription().getVersion());
        getLogger().info(LanguageManager.getMessage("plugin.footer"));

        List<Command> commands = PluginCommandYamlParser.parse(this);
        for (Command command : commands) {
            getCommand(command.getName()).setExecutor(new CommandManager());
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();

        }
        DatabaseManager.initializeDataSource();

    }

    @Override
    public void onDisable() {
        try {
            DatabaseManager.getConnection().close();
        } catch (SQLException e) {
            ErrorUtil.logError(ErrorReason.DATABASE);
            throw new RuntimeException(e);
        }
        getLogger().info(LanguageManager.getMessage("plugin.header"));
        getLogger().info("§e§lDragon§6§lCraft§a§lCore");
        getLogger().info(LanguageManager.getMessage("plugin.disabled") + getDescription().getVersion());
        getLogger().info(LanguageManager.getMessage("plugin.footer"));
    }


}
