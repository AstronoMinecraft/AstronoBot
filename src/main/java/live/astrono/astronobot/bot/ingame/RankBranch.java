package main.java.live.astrono.astronobot.bot.ingame;

public enum RankBranch {
    ADMINISTRATION(5),
    MODERATION(4),
    BUILDER(3),
    YOUTUBE(2),
    DONOR(1);

    private final int weight;

    RankBranch(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}