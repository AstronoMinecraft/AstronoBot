package main.java.live.astrono.astronobot.bot.ingame;

import java.util.List;

public enum Rank {
    OWNER(RankBranch.ADMINISTRATION, "§0", "§x§f§f§0§8§0§0", "Owner", "A", 4, true,
            List.of("astrono.owner"), List.of()),
    MANAGER(RankBranch.ADMINISTRATION, "§8", "§x§f§d§2§7§0§0", "Manager", "B", 3, true,
            List.of("astrono.manager"), List.of()),
    ADMIN(RankBranch.ADMINISTRATION, "§8", "§x§f§a§6§6§0§0", "Admin", "C", 2, false,
            List.of("astrono.admin"), List.of()),
    DEVELOPER(RankBranch.ADMINISTRATION, "§8", "§x§f§a§6§6§0§0", "Developer", "D", 1, false,
            List.of("astrono.developer"), List.of()),

    SRMOD(RankBranch.MODERATION, "§x§7§3§7§3§7§3", "§x§a§9§f§b§4§0", "SrMod", "E", 3, false,
            List.of("astrono.srmod"), List.of()),
    MOD(RankBranch.MODERATION, "§x§a§4§a§4§a§4", "§x§5§4§f§a§5§4", "Mod", "F", 2, false,
            List.of("astrono.mod"), List.of()),
    JRMOD(RankBranch.MODERATION, "§x§a§4§a§4§a§4", "§x§2§9§c§d§2§9", "JrMod", "G", 1, false,
            List.of("astrono.jrmod"), List.of()),

    BUILDER(RankBranch.BUILDER, "§x§6§9§3§7§2§4", "§x§d§e§6§4§3§5", "Builder", "H", 1, false,
            List.of("astrono.builder", "worldedit.limit.unrestricted", "worldedit.*", "fawe.*"), List.of()),

    YOUTUBER(RankBranch.YOUTUBE, "§x§d§e§2§a§2§a", "§x§d§1§b§c§b§c", "YouTuber", "I", 1, false,
            List.of("astrono.youtuber"), List.of()),

    MOON(RankBranch.DONOR, "§7", "§f", "Moon", "J", 4, false,
            List.of("astrono.moon"), List.of("worldedit.limit.100000")),
    JUPITER(RankBranch.DONOR, "§8", "§6", "Jupiter", "K", 3, false,
            List.of("astrono.jupiter"), List.of("worldedit.limit.25000")),
    MARS(RankBranch.DONOR, "§8", "§c", "Mars", "L", 2, false,
            List.of("astrono.mars"), List.of("worldedit.limit.10000")),
    EARTH(RankBranch.DONOR, "§a", "§b", "Earth", "M", 1, false,
            List.of("astrono.earth"), List.of("worldedit.limit.5000")),
    MEMBER(RankBranch.DONOR, "§7", "§7", "Member", "N", 0, false,
            List.of("astrono.player"), List.of());

    public final RankBranch rankBranch;
    public final String prefixColor;
    public final String color;
    public final String name;
    public final String teamPrefix;
    public final Integer weight;
    public final Boolean locked;

    public final List<String> permissions;
    public final List<String> creativePermissions;

    Rank(RankBranch rankBranch, String prefixColor, String color, String name, String teamPrefix, Integer weight, Boolean locked, List<String> permissions, List<String> creativePermissions) {
        this.rankBranch = rankBranch;
        this.prefixColor = prefixColor;
        this.color = color;
        this.name = name;
        this.teamPrefix = teamPrefix;
        this.weight = weight;
        this.locked = locked;

        this.permissions = permissions;
        this.creativePermissions = creativePermissions;
    }

    public String getPrefix() {
        if (weight == 0) {
            return "";
        }
        return prefixColor + "[" + color + name + prefixColor + "]§r";
    }


    public static Rank fromBranch(RankBranch branch, int rankNum) {
        for (Rank rank : Rank.values()) {
            if (rank.weight == rankNum && rank.rankBranch == branch) {
                return rank;
            }
        }

        return null;
    }
}
