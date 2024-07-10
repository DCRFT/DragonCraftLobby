package pl.dcrft.Managers;

import org.bukkit.ChatColor;

import java.util.*;


public class LanguageManager {

    public static String getMessage(String key){
            String message = ConfigManager.getMessagesFile().getString(key);
            if(message != null){
                return ChatColor.translateAlternateColorCodes('&', message);
            }
            return "§cError! Unknown string §e" + key + " §cCheck if it exists in §emessages.yml§c.";
    }
    public static List<String> getMessageList(String key){
        List<String> message = new ArrayList<>();
        for (String s : ConfigManager.getMessagesFile().getStringList(key)) {
            message.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return message;
    }
}