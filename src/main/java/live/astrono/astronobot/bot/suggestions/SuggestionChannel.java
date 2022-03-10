package main.java.live.astrono.astronobot.bot.suggestions;

import main.java.live.astrono.astronobot.AstronoBot;

public enum SuggestionChannel {
    SUGGESTIONS(Type.SUGGESTION, "suggestions", "Suggestions", "Suggestion", true),
    BUG_REPORTS(Type.BUG_REPORT, "bug_reports", "Bug Reports", "Bug Report", false);

    public static final SuggestionChannel[] VALUES = values();

    public Type type;
    public String id;
    public String name;
    public String singular;
    public boolean voted;

    SuggestionChannel(Type type, String id, String name, String singular, boolean voted) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.singular = singular;
        this.voted = voted;
    }

    public long getChannelId() {
        return AstronoBot.getInstance().getConfig().suggestionChannels.get(id);
    }

    enum Type {
        SUGGESTION,
        BUG_REPORT
    }
}
