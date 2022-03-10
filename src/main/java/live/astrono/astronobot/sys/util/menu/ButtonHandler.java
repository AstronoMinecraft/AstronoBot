package main.java.live.astrono.astronobot.sys.util.menu;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import main.java.live.astrono.astronobot.sys.util.TemporaryRunnableStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Collections;
import java.util.function.Consumer;

public class ButtonHandler {
    
    private static final TemporaryRunnableStorage<Long, ButtonListener> BUTTON_CONSUMERS = new TemporaryRunnableStorage<>();
    
    public static void addListener(long user, Message message, Consumer<ButtonClickEvent> consumer, boolean persistent) {
        BUTTON_CONSUMERS.put(message.getIdLong(), new ButtonListener(user, consumer), () -> {
            AstronoBot.getJda()
                    .getTextChannelById(message.getChannel().getIdLong()).retrieveMessageById(message.getIdLong())
                    .flatMap((msg) -> persistent, (msg) -> message.editMessage(message).setActionRows(Collections.emptyList()))
                    .queue();
        }, persistent);
    }
    
    public static void addListener(long user, Message message, Consumer<ButtonClickEvent> consumer) {
        addListener(user, message, consumer, false);
    }
    
    public static void handleEvent(ButtonClickEvent event) {
        ButtonListener listener = BUTTON_CONSUMERS.get(event.getMessageIdLong());
        if (listener != null) {
            if (event.getUser().getIdLong() != listener.user) {
                EmbedBuilder embed = Embeds.error();
                embed.setDescription("You can't use this button!");

                event.deferReply(true).addEmbeds(embed.build()).queue();
                return;
            }
            
            BUTTON_CONSUMERS.expireKey(event.getMessageIdLong());
            listener.consumer.accept(event);
        } else {
            EmbedBuilder embed = Embeds.error();
            embed.setDescription("You can't use this button!");

            event.deferReply(true).addEmbeds(embed.build()).queue();
        }
    }

    private record ButtonListener(long user, Consumer<ButtonClickEvent> consumer) {

    }
}