package pl.dcrft.Managers;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.dcrft.DragonCraftLobby;

import java.util.ArrayList;

public class SessionManager {

    private static SessionManager instance;

    public static SessionManager getInstance() {
        return instance;
    }
    private static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    public static final ArrayList<SessionManager> list = new ArrayList<>();
    private final Player p;

    private Location lastLoc;

    private int afkMinutes;
    private static final String prefix = LanguageManager.getMessage("prefix");

    public SessionManager(Player p) {
        instance = this;
        this.p = p;
        this.lastLoc = p.getLocation();
        this.afkMinutes = 0;
    }

    public void resetMinute() {
        if (this.p != null && this.p.isOnline())
            if (!this.p.hasPermission("afkkick.ignore")) {
                this.afkMinutes = 0;
                this.lastLoc = this.p.getLocation();
            }
    }

    public static BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            public void run() {
                for (int i = 0; i < SessionManager.list.size(); i++) {
                    SessionManager session = SessionManager.list.get(i);
                    session.initializeSessionManager();
                }

            }
        };
    }
    public void initializeSessionManager() {
        int kick_warn_delay = plugin.getConfig().getInt("afk.kick_warn_delay");
        int kick_delay = plugin.getConfig().getInt("afk.kick_delay");
        if (this.p != null && this.p.isOnline())
            if (!this.p.hasPermission("afkkick.ignore"))
                if (this.lastLoc.getWorld() != this.p.getLocation().getWorld()) {
                    return;
                }
        if (this.lastLoc.distanceSquared(this.p.getLocation()) < 4.0D) {
            this.afkMinutes++;
            this.lastLoc = this.p.getLocation();
            if (this.afkMinutes == kick_warn_delay) {
                boolean sound_on_get_warn = Boolean.parseBoolean(plugin.getConfig().getString("afk.sound_on_get_warn"));

                if (sound_on_get_warn) {
                    this.p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10,3);
                }
            } else if(this.afkMinutes >= kick_delay) {
                kickPlayer();
            }
        } else {
            this.afkMinutes = 0;
            this.lastLoc = this.p.getLocation();
        }
    }

    private void kickPlayer() {
        if (!this.p.hasPermission("afkkick.ignore"))
            SessionManager.this.p.kick(Component.text(prefix + LanguageManager.getMessage("afk.kick_msg")));
    }


    public Player getPlayer() {
        return this.p;
    }
}