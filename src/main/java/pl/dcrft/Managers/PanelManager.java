package pl.dcrft.Managers;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import pl.dcrft.DragonCraftLobby;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PanelManager {


    public static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

     private static String prefix = plugin.getConfig().getString(("scoreboard.prefix"));
    public static void sendPanel(Player p) {

        String title = plugin.getConfig().getString("scoreboard.title");

        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard panel = manager.getNewScoreboard();
        Objective objective = panel.registerNewObjective("cokolwiek", Criteria.DUMMY, Component.text(1));

        Scoreboard emptyBoard = manager.getNewScoreboard();
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.displayName(Component.text(title));

        p.setScoreboard(emptyBoard);

        objective.getScore(" ").setScore(11);

        objective.getScore(prefix
                + plugin.getConfig().getString("scoreboard.nick")
                + p.getName()).setScore(10);

        objective.getScore("§r ").setScore(9);

        objective.getScore(prefix
                + plugin.getConfig().getString("scoreboard.ping")
                + p.getPing() + "ms").setScore(8);

        objective.getScore("§r§r ").setScore(7);

        objective.getScore(prefix
                + plugin.getConfig().getString("scoreboard.rank")
                + DragonCraftLobby.getLuckPerms().getGroupManager().getGroup(DragonCraftLobby.getLuckPerms().getPlayerAdapter(Player.class).getUser(p).getPrimaryGroup()).getDisplayName()).setScore(6);

        objective.getScore("§r§r§r ").setScore(5);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String scoredate = sdf.format(date);

        objective.getScore(prefix
                + plugin.getConfig().getString("scoreboard.time")
                + scoredate).setScore(4);

        objective.getScore("§r§r§r§r ").setScore(3);

        objective.getScore(prefix + plugin.getConfig().getString("scoreboard.server.all") +
                PlaceholderAPI.setPlaceholders(p, "%bungee_total%")).setScore(2);

        objective.getScore("§r§r§r§r§r ").setScore(1);

        objective.getScore(plugin.getConfig().getString("scoreboard.server-ip")).setScore(0);

        p.setScoreboard(panel);
    }
    int taskID;
    public void showRepeatingPanel(Player p) {
        taskID = Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if(p == null || !p.isOnline()) {
                plugin.getLogger().warning("is not online!");
                Bukkit.getScheduler().cancelTask(taskID);
            } else {
                sendPanel(p);
            }
        }, 0, 100).getTaskId();
    }
}