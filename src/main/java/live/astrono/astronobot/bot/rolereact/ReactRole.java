package main.java.live.astrono.astronobot.bot.rolereact;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public enum ReactRole {
    // 📜 📔 📆 🎫

    SERVER_UPDATES(SelectOption.of("Server Updates", "server_updates")
            .withDescription("The Astrono Network update notification role.")
            .withEmoji(Emoji.fromUnicode("\uD83D\uDCDC"))),

    STAFF_UPDATES(SelectOption.of("Staff Updates", "staff_updates")
            .withDescription("Get notified about staff applications or promotions.")
            .withEmoji(Emoji.fromUnicode("\uD83D\uDCD4"))),

    EVENTS(SelectOption.of("Events", "events")
            .withDescription("Get notified about Astrono events.")
            .withEmoji(Emoji.fromUnicode("\uD83D\uDCC6"))),

    SALES(SelectOption.of("Sales", "sales")
            .withDescription("Get notified about Astrono store sales.")
            .withEmoji(Emoji.fromUnicode("\uD83C\uDFAB"))); // note: spaces are part of the unicode character

    public final SelectOption option;

    ReactRole(SelectOption option) {
        this.option = option;
    }
}
