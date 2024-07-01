package pl.dcrft.Utils.ErrorUtils;

import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.LanguageManager;

public class ErrorUtil {
    public static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();
    public static void logError(ErrorReason type){
        switch (type){
            case DATABASE:
                for (String s : LanguageManager.getMessageList("errors.database")){
                    plugin.getLogger().info((s));
                }
                return;
            case DATA:
                for (String s : LanguageManager.getMessageList("errors.data")){
                    plugin.getLogger().info((s));
                }
                return;
            case CONFIG:
                for (String s : LanguageManager.getMessageList("errors.config")){
                    plugin.getLogger().info((s));
                }
                return;
            case MESSAGES:
                for (String s : LanguageManager.getMessageList("errors.messages")){
                    plugin.getLogger().info((s));
                }
                return;
            case OTHER:
                for (String s : LanguageManager.getMessageList("errors.other")){
                    plugin.getLogger().info((s));
                }
        }
    }
}