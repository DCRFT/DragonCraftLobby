package pl.dcrft.Managers.Profile;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.dcrft.DragonCraftLobby;
import pl.dcrft.Managers.DatabaseManager;
import pl.dcrft.Managers.LanguageManager;
import pl.dcrft.Managers.MessageManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class ProfileManager {

    private static final DragonCraftLobby plugin = DragonCraftLobby.getInstance();

    public static void showProfile(Player p, String arg, ProfileType type) {
        if (arg.equalsIgnoreCase("JaneQ") || arg.equalsIgnoreCase("NickNickerYT") || arg.equalsIgnoreCase("MikiIgi192") || arg.equalsIgnoreCase("kalkulator888")) {
            MessageManager.sendPrefixedMessage(p, "profile.unknown-player");
            return;
        } else {

            switch (type) {
                case SURVIVAL:
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        try {
                            DatabaseManager.openConnection();
                            final Statement statement = DatabaseManager.connection.createStatement();
                            final ResultSet result = statement.executeQuery("SELECT " + DatabaseManager.table_bungee + ".nick AS ogolem_nick, " + DatabaseManager.table_bungee + ".ranga AS ogolem_ranga, " + DatabaseManager.table_bungee + ".since AS ogolem_since, " + DatabaseManager.table_bungee + ".online AS ogolem_online, " + DatabaseManager.table_survival + ".nick AS s16_nick, " + DatabaseManager.table_survival + ".kille AS s16_kille, " + DatabaseManager.table_survival + ".dedy AS s16_dedy, " + DatabaseManager.table_survival + ".kdr AS s16_kdr, " + DatabaseManager.table_survival + ".bloki AS s16_bloki, " + DatabaseManager.table_survival + ".czasgry AS s16_czasgry, " + DatabaseManager.table_survival + ".slub AS s16_slub FROM " + DatabaseManager.table_bungee +
                                    " LEFT JOIN " + DatabaseManager.table_survival +
                                    " ON " + DatabaseManager.table_survival + ".nick = " + DatabaseManager.table_bungee + ".nick" +
                                    " WHERE " + DatabaseManager.table_bungee + ".nick = '" + arg + "'");
                            final boolean val = result.next();
                            if (!val) {
                                MessageManager.sendPrefixedMessage(p, "profile.unknown-player");
                            }
                            else {

                                final ItemStack siekiera = new ItemStack(Material.WOODEN_AXE);
                                final ItemMeta meta6 = siekiera.getItemMeta();

                                final Statement pun = DatabaseManager.connection.createStatement();
                                final ResultSet punishments = pun.executeQuery(
                                        " SELECT * FROM bm_players" +
                                                " LEFT JOIN (SELECT player_id AS p_id, reason AS ban_reason, expires AS ban_expires, created AS ban_created FROM bm_player_bans) AS bans" +
                                                " ON bm_players.id = bans.p_id" +
                                                " LEFT JOIN (SELECT player_id AS p_id, reason AS warn_reason, expires AS warn_expires, created AS warn_created FROM bm_player_warnings) AS warns" +
                                                " ON bm_players.id = warns.p_id" +
                                                " LEFT JOIN (SELECT player_id AS p_id, reason AS mute_reason, expires AS mute_expires, created AS mute_created FROM bm_player_mutes) AS mutes" +
                                                " ON bm_players.id = mutes.p_id" +
                                                " WHERE bm_players.name = '" + arg + "'");

                                if(punishments.next()){
                                    meta6.displayName(Component.text(LanguageManager.getMessage("profile.punishments.title")));
                                    List<Component> lore = new ArrayList<>();

                                    List<String> bans = new ArrayList<>();
                                    List<String> warns = new ArrayList<>();
                                    List<String> mutes = new ArrayList<>();

                                    bans.add(punishments.getString("ban_reason"));
                                    warns.add(punishments.getString("warn_reason"));
                                    mutes.add(punishments.getString("mute_reason"));

                                    while (punishments.next()) {
                                        warns.add(punishments.getString("warn_reason"));
                                    }

                                    if (bans.get(0) != null) {
                                        lore.add(Component.text(LanguageManager.getMessage("profile.punishments.bans")));
                                        for (String s : bans) {
                                            lore.add(Component.text(LanguageManager.getMessage("profile.punishments.color") + s));
                                        }
                                    }
                                    if (warns.get(0) != null) {
                                        lore.add(Component.text(LanguageManager.getMessage("profile.punishments.warns")));
                                        for (String s : warns) {
                                            lore.add(Component.text(LanguageManager.getMessage("profile.punishments.color") + s));
                                        }
                                    }
                                    if(mutes.get(0) != null) {
                                        lore.add(Component.text(LanguageManager.getMessage("profile.punishments.mutes")));
                                        for (String s : mutes) {
                                            lore.add(Component.text(LanguageManager.getMessage("profile.punishments.color") + s));
                                        }
                                    }
                                    if(lore.size() > 0) {
                                        meta6.lore(lore);
                                    } else {
                                        meta6.displayName(Component.text(LanguageManager.getMessage("profile.punishments.title-none")));
                                    }
                                }
                                siekiera.setItemMeta(meta6);

                                final Inventory inv = Bukkit.createInventory(null, 54, Component.text(LanguageManager.getMessage("profile.title") + arg));

                                inv.setItem(25, siekiera);

                                final ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                                final ItemStack whiteGlass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);

                                final ItemStack glowa = new ItemStack(Material.PLAYER_HEAD);

                                final SkullMeta meta = (SkullMeta) glowa.getItemMeta();
                                meta.setOwningPlayer(Bukkit.getOfflinePlayer(arg));
                                meta.displayName(Component.text(LanguageManager.getMessage("profile.head") + arg));
                                glowa.setItemMeta(meta);
                                inv.setItem(13, glowa);

                                int[] white = new int[]{ 10, 11, 15, 16, 28, 34, 37, 38, 40, 42, 43 };
                                for (int i : white) {
                                    inv.setItem(i, whiteGlass);
                                }
                                final String kille = result.getString("s16_kille");
                                final String dedy = result.getString("s16_dedy");
                                final String kdr = result.getString("s16_kdr");
                                final String bloki = result.getString("s16_bloki");
                                final String since = result.getString("ogolem_since");
                                final String ranga = result.getString("ogolem_ranga");
                                final String online = result.getString("ogolem_online");
                                final String timeplayed = result.getString("s16_czasgry");
                                final String slub = result.getString("s16_slub");


                                final ItemStack survi = new ItemStack(Material.IRON_PICKAXE);
                                final ItemMeta metaSurvi = survi.getItemMeta();
                                metaSurvi.displayName(Component.text(LanguageManager.getMessage("profile.server.survival")));
                                survi.setItemMeta(metaSurvi);
                                inv.setItem(3, survi);

                                final ItemStack sky = new ItemStack(Material.GRASS_BLOCK);
                                final ItemMeta metaSky = sky.getItemMeta();
                                metaSky.displayName(Component.text(LanguageManager.getMessage("profile.server.skyblock")));
                                sky.setItemMeta(metaSky);
                                inv.setItem(5, sky);

                                if (getServer().getPlayer(arg) != null) {
                                    final ItemStack welna = new ItemStack(Material.LIME_DYE);
                                    final ItemMeta meta2 = welna.getItemMeta();
                                    meta2.displayName(Component.text(LanguageManager.getMessage("profile.status.online")));
                                    welna.setItemMeta(meta2);
                                    inv.setItem(12, welna);
                                } else {
                                    final ItemStack welna = new ItemStack(Material.RED_DYE);
                                    final ItemMeta meta2 = welna.getItemMeta();
                                    meta2.displayName(Component.text(LanguageManager.getMessage("profile.status.offline") + online));
                                    welna.setItemMeta(meta2);
                                    inv.setItem(12, welna);
                                }
                                final ItemStack emeraldBlock = new ItemStack(Material.EMERALD);
                                final ItemMeta meta3 = emeraldBlock.getItemMeta();
                                meta3.displayName(Component.text(LanguageManager.getMessage("profile.rank") + ranga));
                                emeraldBlock.setItemMeta(meta3);
                                inv.setItem(14, emeraldBlock);
                                final ItemStack mapa = new ItemStack(Material.MAP);
                                final ItemMeta meta4 = mapa.getItemMeta();
                                if (slub == null || slub.equalsIgnoreCase("NULL")) {
                                    meta4.displayName(Component.text(LanguageManager.getMessage("profile.marry.none")));
                                } else {
                                    meta4.displayName(Component.text(LanguageManager.getMessage("profile.marry.active") + slub));
                                }
                                mapa.setItemMeta(meta4);
                                inv.setItem(23, mapa);
                                final ItemStack kilof = new ItemStack(Material.DIAMOND_PICKAXE);
                                final ItemMeta meta5 = kilof.getItemMeta();
                                meta5.displayName(Component.text((LanguageManager.getMessage("profile.blocks") + bloki)));
                                kilof.setItemMeta(meta5);
                                inv.setItem(21, kilof);



                                final ItemStack miecz = new ItemStack(Material.IRON_SWORD);
                                final ItemMeta meta7 = miecz.getItemMeta();
                                meta7.displayName(Component.text(LanguageManager.getMessage("profile.kills") + kille));
                                final List<Component> lore = new ArrayList<>();
                                lore.add(Component.text(LanguageManager.getMessage("profile.deaths") + dedy));
                                lore.add(Component.text(LanguageManager.getMessage("profile.kdr") + kdr));
                                meta7.lore(lore);
                                miecz.setItemMeta(meta7);
                                inv.setItem(19, miecz);

                                ItemStack clock = new ItemStack(Material.CLOCK);
                                ItemMeta meta10 = clock.getItemMeta();
                                meta10.setDisplayName(LanguageManager.getMessage("profile.timeplayed") + timeplayed);
                                clock.setItemMeta(meta10);
                                inv.setItem(30, clock);


                                final ItemStack jablko = new ItemStack(Material.GOLDEN_APPLE);
                                final ItemMeta meta8 = jablko.getItemMeta();
                                meta8.displayName(Component.text(LanguageManager.getMessage("profile.since") + since));
                                jablko.setItemMeta(meta8);
                                inv.setItem(39, jablko);

                                final ItemStack wersja = new ItemStack(Material.REDSTONE_TORCH);
                                final ItemMeta meta9 = wersja.getItemMeta();
                                meta9.displayName(Component.text(LanguageManager.getMessage("profile.ver") + plugin.getDescription().getVersion()));
                                wersja.setItemMeta(meta9);
                                inv.setItem(41, wersja);

                                for(int i = 0; i < inv.getSize(); i++) {
                                    if(inv.getItem(i) == null ) inv.setItem(i, blackGlass);
                                }
                                Bukkit.getScheduler().runTask(plugin, () -> p.openInventory(inv));
                                punishments.close();
                                pun.close();
                            }
                            result.close();
                            statement.close();


                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    return;
                case SKYBLOCK:
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        try {
                            DatabaseManager.openConnection();
                            final Statement statement = DatabaseManager.connection.createStatement();
                            final ResultSet result = statement.executeQuery("SELECT " + DatabaseManager.table_bungee + ".nick AS ogolem_nick, " + DatabaseManager.table_bungee + ".ranga AS ogolem_ranga, " + DatabaseManager.table_bungee + ".since AS ogolem_since, " + DatabaseManager.table_bungee + ".online AS ogolem_online, " + DatabaseManager.table_skyblock + ".nick AS sky_nick, " + DatabaseManager.table_skyblock + ".kille AS sky_kille, " + DatabaseManager.table_skyblock + ".dedy AS sky_dedy, " + DatabaseManager.table_skyblock + ".kdr AS sky_kdr, " + DatabaseManager.table_skyblock + ".poziom AS sky_poziom, " + DatabaseManager.table_skyblock + ".kasa AS sky_kasa, " + DatabaseManager.table_skyblock + ".czasgry AS sky_czasgry, " + DatabaseManager.table_skyblock + ".slub AS sky_slub FROM " + DatabaseManager.table_bungee +
                                    " LEFT JOIN " + DatabaseManager.table_skyblock +
                                    " ON " + DatabaseManager.table_skyblock + ".nick = " + DatabaseManager.table_bungee + ".nick" +
                                    " WHERE " + DatabaseManager.table_bungee + ".nick = '" + arg + "'");
                            final boolean val = result.next();
                            if (!val) {
                                MessageManager.sendPrefixedMessage(p, "profile.unknown-player");
                            }
                            else {

                                final ItemStack siekiera = new ItemStack(Material.WOODEN_AXE);
                                final ItemMeta meta6 = siekiera.getItemMeta();

                                final Statement pun = DatabaseManager.connection.createStatement();
                                final ResultSet punishments = pun.executeQuery(
                                        " SELECT * FROM bm_players" +
                                                " LEFT JOIN (SELECT player_id AS p_id, reason AS ban_reason, expires AS ban_expires, created AS ban_created FROM bm_player_bans) AS bans" +
                                                " ON bm_players.id = bans.p_id" +
                                                " LEFT JOIN (SELECT player_id AS p_id, reason AS warn_reason, expires AS warn_expires, created AS warn_created FROM bm_player_warnings) AS warns" +
                                                " ON bm_players.id = warns.p_id" +
                                                " LEFT JOIN (SELECT player_id AS p_id, reason AS mute_reason, expires AS mute_expires, created AS mute_created FROM bm_player_mutes) AS mutes" +
                                                " ON bm_players.id = mutes.p_id" +
                                                " WHERE bm_players.name = '" + arg + "'");

                                if(punishments.next()){
                                    meta6.displayName(Component.text(LanguageManager.getMessage("profile.punishments.title")));
                                    List<Component> lore = new ArrayList<>();

                                    List<String> bans = new ArrayList<>();
                                    List<String> warns = new ArrayList<>();
                                    List<String> mutes = new ArrayList<>();

                                    bans.add(punishments.getString("ban_reason"));
                                    warns.add(punishments.getString("warn_reason"));
                                    mutes.add(punishments.getString("mute_reason"));

                                    while (punishments.next()) {
                                        warns.add(punishments.getString("warn_reason"));
                                    }

                                    if (bans.get(0) != null) {
                                        lore.add(Component.text(LanguageManager.getMessage("profile.punishments.bans")));
                                        for (String s : bans) {
                                            lore.add(Component.text(LanguageManager.getMessage("profile.punishments.color") + s));
                                        }
                                    }
                                    if (warns.get(0) != null) {
                                        lore.add(Component.text(LanguageManager.getMessage("profile.punishments.warns")));
                                        for (String s : warns) {
                                            lore.add(Component.text(LanguageManager.getMessage("profile.punishments.color") + s));
                                        }
                                    }
                                    if(mutes.get(0) != null) {
                                        lore.add(Component.text(LanguageManager.getMessage("profile.punishments.mutes")));
                                        for (String s : mutes) {
                                            lore.add(Component.text(LanguageManager.getMessage("profile.punishments.color") + s));
                                        }
                                    }
                                    if(lore.size() > 0) {
                                        meta6.lore(lore);
                                    } else {
                                        meta6.displayName(Component.text(LanguageManager.getMessage("profile.punishments.title-none")));
                                    }
                                }
                                siekiera.setItemMeta(meta6);

                                final Inventory inv = Bukkit.createInventory(null, 54, Component.text(LanguageManager.getMessage("profile.title") + arg));

                                inv.setItem(25, siekiera);

                                final ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                                final ItemStack whiteGlass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);

                                final ItemStack glowa = new ItemStack(Material.PLAYER_HEAD);

                                final SkullMeta meta = (SkullMeta) glowa.getItemMeta();
                                meta.setOwningPlayer(Bukkit.getOfflinePlayer(arg));
                                meta.displayName(Component.text(LanguageManager.getMessage("profile.head") + arg));
                                glowa.setItemMeta(meta);
                                inv.setItem(13, glowa);

                                int[] white = new int[]{ 10, 11, 15, 16, 28, 34, 37, 38, 40, 42, 43 };
                                for (int i : white) {
                                    inv.setItem(i, whiteGlass);
                                }
                                final String kille = result.getString("sky_kille");
                                final String dedy = result.getString("sky_dedy");
                                final String kdr = result.getString("sky_kdr");
                                final String poziom = result.getString("sky_poziom");
                                final String kasa = result.getString("sky_kasa");
                                final String since = result.getString("ogolem_since");
                                final String ranga = result.getString("ogolem_ranga");
                                final String online = result.getString("ogolem_online");
                                final String timeplayed = result.getString("sky_czasgry");
                                final String slub = result.getString("sky_slub");


                                final ItemStack survi = new ItemStack(Material.IRON_PICKAXE);
                                final ItemMeta metaSurvi = survi.getItemMeta();
                                metaSurvi.displayName(Component.text(LanguageManager.getMessage("profile.server.survival")));
                                survi.setItemMeta(metaSurvi);
                                inv.setItem(3, survi);

                                final ItemStack sky = new ItemStack(Material.GRASS_BLOCK);
                                final ItemMeta metaSky = sky.getItemMeta();
                                metaSky.displayName(Component.text(LanguageManager.getMessage("profile.server.skyblock")));
                                sky.setItemMeta(metaSky);
                                inv.setItem(5, sky);

                                if (getServer().getPlayer(arg) != null) {
                                    final ItemStack welna = new ItemStack(Material.LIME_DYE);
                                    final ItemMeta meta2 = welna.getItemMeta();
                                    meta2.displayName(Component.text(LanguageManager.getMessage("profile.status.online")));
                                    welna.setItemMeta(meta2);
                                    inv.setItem(12, welna);
                                } else {
                                    final ItemStack welna = new ItemStack(Material.RED_DYE);
                                    final ItemMeta meta2 = welna.getItemMeta();
                                    meta2.displayName(Component.text(LanguageManager.getMessage("profile.status.offline") + online));
                                    welna.setItemMeta(meta2);
                                    inv.setItem(12, welna);
                                }
                                final ItemStack emeraldBlock = new ItemStack(Material.EMERALD);
                                final ItemMeta meta3 = emeraldBlock.getItemMeta();
                                meta3.displayName(Component.text(LanguageManager.getMessage("profile.rank") + ranga));
                                emeraldBlock.setItemMeta(meta3);
                                inv.setItem(14, emeraldBlock);
                                final ItemStack mapa = new ItemStack(Material.MAP);
                                final ItemMeta meta4 = mapa.getItemMeta();
                                if (slub == null || slub.equalsIgnoreCase("NULL")) {
                                    meta4.displayName(Component.text(LanguageManager.getMessage("profile.marry.none")));
                                } else {
                                    meta4.displayName(Component.text(LanguageManager.getMessage("profile.marry.active") + slub));
                                }
                                mapa.setItemMeta(meta4);
                                inv.setItem(23, mapa);

                                final ItemStack emerald = new ItemStack(Material.DIAMOND);
                                final ItemMeta meta5 = emerald.getItemMeta();
                                meta5.displayName(Component.text((LanguageManager.getMessage("profile.level") + poziom)));
                                emerald.setItemMeta(meta5);
                                inv.setItem(21, emerald);


                                final ItemStack miecz = new ItemStack(Material.IRON_SWORD);
                                final ItemMeta meta7 = miecz.getItemMeta();
                                meta7.displayName(Component.text(LanguageManager.getMessage("profile.kills") + kille));
                                final List<Component> lore = new ArrayList<>();
                                lore.add(Component.text(LanguageManager.getMessage("profile.deaths") + dedy));
                                lore.add(Component.text(LanguageManager.getMessage("profile.kdr") + kdr));
                                meta7.lore(lore);
                                miecz.setItemMeta(meta7);
                                inv.setItem(19, miecz);

                                ItemStack clock = new ItemStack(Material.CLOCK);
                                ItemMeta meta10 = clock.getItemMeta();
                                meta10.setDisplayName(LanguageManager.getMessage("profile.timeplayed") + timeplayed);
                                clock.setItemMeta(meta10);
                                inv.setItem(30, clock);


                                final ItemStack ikasa = new ItemStack(Material.DRAGON_EGG);
                                final ItemMeta metaKasa = ikasa.getItemMeta();
                                metaKasa.displayName(Component.text((LanguageManager.getMessage("profile.money") + kasa)));
                                ikasa.setItemMeta(metaKasa);
                                inv.setItem(32, ikasa);


                                final ItemStack jablko = new ItemStack(Material.GOLDEN_APPLE);
                                final ItemMeta meta8 = jablko.getItemMeta();
                                meta8.displayName(Component.text(LanguageManager.getMessage("profile.since") + since));
                                jablko.setItemMeta(meta8);
                                inv.setItem(39, jablko);

                                final ItemStack wersja = new ItemStack(Material.REDSTONE_TORCH);
                                final ItemMeta meta9 = wersja.getItemMeta();
                                meta9.displayName(Component.text(LanguageManager.getMessage("profile.ver") + plugin.getDescription().getVersion()));
                                wersja.setItemMeta(meta9);
                                inv.setItem(41, wersja);

                                for(int i = 0; i < inv.getSize(); i++) {
                                    if(inv.getItem(i) == null ) inv.setItem(i, blackGlass);
                                }
                                Bukkit.getScheduler().runTask(plugin, () -> p.openInventory(inv));
                                punishments.close();
                                pun.close();
                            }
                            result.close();
                            statement.close();


                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
            }
        }

    }
}
