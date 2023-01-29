package pl.dcrft.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import pl.dcrft.DragonCraftLobby;

import java.time.Duration;
import java.util.List;

public class AnimationUtil {
    public static DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    public static void playAnimation(Player p, List<String> title, String subtitle) {

        for(int i = 0; i < title.size(); i++) {
            int finalI = i;
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                    () -> {
                        final Component mainTitle = Component.text(title.get(finalI));
                        final Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO);

                        final Title ftitle = Title.title(mainTitle, Component.empty(), times);

                        p.showTitle(ftitle);

                        p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 1F);
                    }, 40 + i * 2L);

        }
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                () -> {

                    final Component mainTitle = Component.text(title.get(title.size()-1));
                    final Component subTitle = Component.text(subtitle);

                    final Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ofSeconds(1));

                    final Title ftitle = Title.title(mainTitle, subTitle, times);

                    p.showTitle(ftitle);

                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 100F, 0.1F);
                }, 45 + title.size() * 2L);

    }
}
