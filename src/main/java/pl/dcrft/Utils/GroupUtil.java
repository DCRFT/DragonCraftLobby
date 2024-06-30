package pl.dcrft.Utils;

import pl.dcrft.DragonCraftLobby;

public class GroupUtil {

    private static DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    public static String getPlayerGroup(String p) {
        String[] ranks = new String[]{"Gracz", "VIP", "SVIP", "MVIP", "EVIP", "Pomocnik", "Moderator"};
        String rawRank = plugin.getLuckPerms().getUserManager().getUser(p).getPrimaryGroup();
        String rank = "Gracz";
        if (rawRank.equalsIgnoreCase("vip") || rawRank.equalsIgnoreCase("vip+")) {
            rank = "VIP";
        } else if (rawRank.equalsIgnoreCase("svip") || rawRank.equalsIgnoreCase("svip+")) {
            rank = "SVIP";
        } else if (rawRank.equalsIgnoreCase("mvip") || rawRank.equalsIgnoreCase("mvip+")) {
            rank = "MVIP";
        } else if (rawRank.equalsIgnoreCase("evip") || rawRank.equalsIgnoreCase("evip+")) {
            rank = "EVIP";
        } else if (rawRank.equalsIgnoreCase("pomocnik")) {
            rank = "Pomocnik";
        } else if (rawRank.equalsIgnoreCase("moderator")) {
            rank = "Moderator";
        } else if (rawRank.equalsIgnoreCase("viceadministrator")) {
            rank = "ViceAdministrator";
        } else if (rawRank.equalsIgnoreCase("administrator")) {
            rank = "Administrator";
        } else if (rawRank.equalsIgnoreCase("media")) {
            rank = "Media";
        } else if (rawRank.equalsIgnoreCase("opiekun")) {
            rank = "Opiekun";
        } else if (rawRank.equalsIgnoreCase("budowniczy")) {
            rank = "Budowniczy";
        }
        return rank;
    }

}
