package main.java.live.astrono.astronobot.bot.cmd;

public enum Category {
    PLAYER_STATISTICS("Player Statistics", "These commands are made to be able to quickly search information on a player", "\uD83D\uDC64"),
    GENERAL_STATISTICS("General Statistics", "These commands are made to be able to quickly search information on a plot or how many people are online a node.", "\uD83D\uDCCB"),
    OTHER("Other", "These commands are just random commands.", "\uD83E\uDDE9"),
    MODERATION("Moderation", "These commands are mod related and are intended for staff use.", "\uD83D\uDCD8"),
    ADMINISTRATION("Administration", "These commands are admin related and are intended for staff use.", "\uD83D\uDCD5");

    private final String name;
    private final String description;
    private final String emoji;

    Category(String name, String description, String emoji) {
        this.name = name;
        this.description = description;
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
    }
}
