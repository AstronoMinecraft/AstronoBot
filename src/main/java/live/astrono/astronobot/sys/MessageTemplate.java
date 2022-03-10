package main.java.live.astrono.astronobot.sys;

import main.java.live.astrono.astronobot.Constants;
import main.java.live.astrono.astronobot.bot.rolereact.ReactRole;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import main.java.live.astrono.astronobot.sys.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.List;
import java.util.function.Consumer;

public enum MessageTemplate {
    // When updating these make sure to use `/template edit` to update the existing message

    ROLE_REACT(message -> {
        SelectionMenu.Builder menu = SelectionMenu.create("roleReact")
                .setMinValues(0)
                .setMaxValues(25);

        for (ReactRole role : ReactRole.values()) {
            menu.addOptions(role.option);
        }

        message.editMessage(new MessageBuilder()
                        .setContent("> **Role Menu**\nUse the select menu below to add and remove certain roles.")
                        .setActionRows(ActionRow.of(menu.build()))
                        .build())
                .queue();
    }),

    TICKET_MESSAGE(message -> {
        EmbedBuilder embedBuilder = Embeds.info();
        embedBuilder.setTitle("Support Tickets");
        embedBuilder.setDescription("If you need support for a purchase or any other issue please feel free to open a ticket!");
        embedBuilder.setTimestamp(null);
        embedBuilder.setThumbnail(Constants.LOGO);

        Button button = Button.secondary("OPEN_TICKET", "Open Ticket");
        button = button.withEmoji(Emoji.fromUnicode("\uD83C\uDFAB"));

        List<Button> buttons = List.of(button);

        message.editMessageEmbeds(embedBuilder.build())
                .setActionRows(Util.of(buttons))
                .content("_ _")
                .queue();
    });

    private final Consumer<Message> consumer;

    MessageTemplate(Consumer<Message> consumer) {
        this.consumer = consumer;
    }

    public void consume(Message message) {
        consumer.accept(message);
    }
}
