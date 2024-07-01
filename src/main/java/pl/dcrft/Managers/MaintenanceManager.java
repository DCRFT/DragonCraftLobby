package pl.dcrft.Managers;

import org.bukkit.Bukkit;
import pl.dcrft.DragonCraftLobby;

import java.text.MessageFormat;


public class MaintenanceManager {
    public static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    public static void saveAll() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all"));
    }

    public static void restartServer() {
        saveAll();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.spigot().restart());
    }

    public static void restartServer(int minutes) {
        saveAll();

        if(minutes > 1) {
            MessageManager.broadcastPrefixed(MessageFormat.format(LanguageManager.getMessage("maintenance.restart.broadcast"), minutes + " " + LanguageManager.getMessage("maintenance.timeformat.minutes")));
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            MessageManager.broadcastPrefixed(MessageFormat.format(LanguageManager.getMessage("maintenance.restart.broadcast"), "1 " + LanguageManager.getMessage("maintenance.timeformat.minute")));
            saveAll();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                MessageManager.broadcastPrefixed(MessageFormat.format(LanguageManager.getMessage("maintenance.restart.broadcast"), "30 " + LanguageManager.getMessage("maintenance.timeformat.seconds")));
                saveAll();
            }, 600L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                MessageManager.broadcastPrefixed(MessageFormat.format(LanguageManager.getMessage("maintenance.restart.broadcast"), "15 " + LanguageManager.getMessage("maintenance.timeformat.seconds")));
                saveAll();
            }, 900L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                int i = 10;
                while (i > 0) {
                    if (i == 1) {
                        MessageManager.broadcastPrefixed(MessageFormat.format(LanguageManager.getMessage("maintenance.restart.broadcast"), i + " " + LanguageManager.getMessage("maintenance.timeformat.second")));
                    } else {
                        MessageManager.broadcastPrefixed(MessageFormat.format(LanguageManager.getMessage("maintenance.restart.broadcast"), i + " " + LanguageManager.getMessage("maintenance.timeformat.seconds")));
                    }
                    saveAll();
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i--;
                }
                MessageManager.broadcastPrefixed(LanguageManager.getMessage("maintenance.saving"));
                saveAll();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MessageManager.broadcastPrefixed(LanguageManager.getMessage("maintenance.restart.in_progress"));
                saveAll();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                saveAll();
                Bukkit.spigot().restart();
            }, 1000L);
        }, (minutes - 1) * 1200L);
    }

}