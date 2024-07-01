package pl.dcrft.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.ConfigManager;
import pl.dcrft.Managers.LanguageManager;
import pl.dcrft.Managers.MessageManager;
import pl.dcrft.Utils.ColorUtil;

import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.Stream;


public class PlayerChatListener implements Listener {
    public static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();
        if(!ConfigManager.getDataFile().getBoolean("czat") && !e.getPlayer().hasPermission("panel.mod")){
            e.setCancelled(true);
            MessageManager.sendPrefixedMessage(p, "chat_muted");
            return;
        }

        String message = e.getMessage();

        String niezmieniona = message;
        for (final Map.Entry<String, Object> filter : plugin.filters.entrySet()) {
            if(!ConfigManager.getDataFile().getBoolean("players." + p.getName() + ".modchat") && !ConfigManager.getDataFile().getBoolean("players." + p.getName() + ".adminchat")) {
                message = message.toLowerCase().replaceAll(filter.getKey().toLowerCase(), filter.getValue().toString());
            }
        }
        if (!message.equalsIgnoreCase(niezmieniona)) {
            if (e.getPlayer().isOp()) {
                return;
            }
            e.setMessage(message);
            String[] words = plugin.getConfig().getStringList("replacement").toArray(new String[0]);

            String finalMessage = message;
            if (Stream.of(words).noneMatch(word -> finalMessage.contains(word.toLowerCase()))) {
                String msg = MessageFormat.format(LanguageManager.getMessage("censored_notification"), p.getName(), niezmieniona);
                Bukkit.getServer().getLogger().info(msg);

                for (Player o : Bukkit.getOnlinePlayers()) {
                    if (o.hasPermission("panel.mod") && !ConfigManager.getDataFile().getBoolean("players." + o.getName() + ".stream")) {
                        o.sendMessage(LanguageManager.getMessage("prefix") + msg);
                    }
                }
            }
        }

        if (e.getMessage().isEmpty()) e.setCancelled(true);

        if (ConfigManager.getDataFile().getBoolean("players." + e.getPlayer().getName() + ".adminchat")) {
            if (ConfigManager.getDataFile().getBoolean("players." + e.getPlayer().getName() + ".adminchat")) {
                if (ConfigManager.getDataFile().getBoolean("players." + e.getPlayer().getName() + ".stream")) {
                    MessageManager.sendPrefixedMessage(p, "stream.cant_write_turned_off");
                    ConfigManager.getDataFile().set("players." + e.getPlayer().getName() + ".adminchat", false);
                    e.setCancelled(true);
                    ConfigManager.saveData();
                    return;
                }
                e.setCancelled(true);
                Player sender = e.getPlayer();
                String wiad;
                if (!message.equalsIgnoreCase(niezmieniona)) {
                    wiad = message;
                }
                else {
                    wiad = niezmieniona;
                }
                String msg = ColorUtil.colorize(
                        LanguageManager.getMessage("staffchat.adminchat.title") +
                                LanguageManager.getMessage("staffchat.adminchat.spacer") +
                                sender.getDisplayName() +
                                LanguageManager.getMessage("staffchat.adminchat.spacer") +
                                wiad);
                Bukkit.getServer().getLogger().info(msg);
                for(Player o : Bukkit.getOnlinePlayers()){
                    if(o.hasPermission("admin.see") && !ConfigManager.getDataFile().getBoolean("players." + o.getName() + ".stream")) {
                        o.sendMessage(msg);
                    }
                }
            }
        }
        if (ConfigManager.getDataFile().getBoolean("players." + e.getPlayer().getName() + ".modchat")) {
            if (ConfigManager.getDataFile().getBoolean("players." + e.getPlayer().getName() + ".modchat")) {
                if (ConfigManager.getDataFile().getBoolean("players." + e.getPlayer().getName() + ".stream")) {
                    MessageManager.sendPrefixedMessage(p, "stream.cant_write_turned_off");
                    ConfigManager.getDataFile().set("players." + e.getPlayer().getName() + ".modchat", false);
                    e.setCancelled(true);
                    ConfigManager.saveData();
                    return;
                }
                e.setCancelled(true);
                Player sender = e.getPlayer();
                String wiad;
                if (!message.equalsIgnoreCase(niezmieniona)) {
                    wiad = message;
                }
                else {
                    wiad = niezmieniona;
                }
                String msg = ColorUtil.colorize(
                        LanguageManager.getMessage("staffchat.modchat.title") +
                                LanguageManager.getMessage("staffchat.modchat.spacer")  +
                                sender.getDisplayName() +
                                LanguageManager.getMessage("staffchat.modchat.spacer") +
                                wiad);
                Bukkit.getServer().getLogger().info(msg);
                for(Player o : Bukkit.getOnlinePlayers()){
                    if(o.hasPermission("mod.see") && !ConfigManager.getDataFile().getBoolean("players." + o.getName() + ".stream")) {
                        o.sendMessage(msg);

                    }
                }
            }
        }
    }
}