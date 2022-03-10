package main.java.live.astrono.astronobot.bot.events;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.Constants;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import main.java.live.astrono.astronobot.sys.util.Util;
import main.java.live.astrono.astronobot.sys.util.menu.ButtonHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ButtonEvent extends ListenerAdapter {
    
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getMessageIdLong() == 918917044647387177L) {
            Integer index = 0;
            for (Long userId : AstronoBot.tickets.values()) {
                if (userId.equals(event.getUser().getIdLong())) {
                    TextChannel textChannel = event.getGuild().getTextChannelsByName("ticket-" + (index + 1), true).get(0);
                    event.reply("You already have a ticket open, " + textChannel.getAsMention())
                            .setEphemeral(true)
                            .queue();
                    return;
                }

                index += 1;
            }

            AstronoBot.ticket += 1;
            AstronoBot.tickets.put(AstronoBot.ticket, event.getUser().getIdLong());

            event.reply("Creating ticket...")
                    .setEphemeral(true)
                    .queue((message) -> event.getGuild().createTextChannel("ticket-" + AstronoBot.ticket,
                            event.getGuild().getCategoryById(918917604880547840L))
                            .queue((textChannel) -> {
                                textChannel.getManager().setTopic(event.getUser().getId()).queue();
                                message.editOriginal("Ticket has been created, " + textChannel.getAsMention()).queue();

                                EmbedBuilder embedBuilder = Embeds.info();
                                embedBuilder.setTitle("Welcome!");
                                embedBuilder.setDescription("Staff should shortly be with you!\n\nIn the meantime please state the purpose of this ticket.");
                                embedBuilder.setTimestamp(null);
                                embedBuilder.setThumbnail(Constants.LOGO);

                                Button button = Button.secondary("CLOSE_TICKET", "Close Ticket");
                                button = button.withEmoji(Emoji.fromUnicode("\uD83D\uDD10"));

                                List<Button> buttons = List.of(button);

                                textChannel.sendMessage("PING STAFF").queue((ticketMessage) -> {
                                    ticketMessage.editMessageEmbeds(embedBuilder.build())
                                            .setActionRows(Util.of(buttons))
                                            .content("_ _")
                                            .queue();
                                    ticketMessage.pin().queue();
                                });
                            }));

            return;
        }

        ButtonHandler.handleEvent(event);
    }
}