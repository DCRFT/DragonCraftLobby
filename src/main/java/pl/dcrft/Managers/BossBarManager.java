package pl.dcrft.Managers;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.dcrft.DragonCraftLobby;

import java.util.List;
import java.util.Random;

public class BossBarManager {
    private static DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    final static List<String> list = LanguageManager.getMessageList("bossbar");

    public static void showBar(Player p) {
        Random rand = new Random();
        String randomElement = list.get(rand.nextInt(list.size()));
        BossBar bossBar = BossBar.bossBar(Component.text(randomElement), 1, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
        p.showBossBar(bossBar);
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                () -> {
                    bossBar.progress(0.8F);
                },20L);
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                () -> {
                    bossBar.progress(0.6F);
                }, 40L);
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                () -> {
                    bossBar.progress(0.4F);
                },  60L);
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                () -> {
                    bossBar.progress(0.2F);
                }, 80L);
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                () -> {
                    bossBar.progress(0);
                }, 100L);
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                () -> {
                    p.hideBossBar(bossBar);
                }, 120L);
    }

    public static void startBroadcasting(Player p) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin,
                () -> {
                showBar(p);
                }, 0L, 120L);
    }
}
