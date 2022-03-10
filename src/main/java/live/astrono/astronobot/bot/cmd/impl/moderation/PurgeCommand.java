package main.java.live.astrono.astronobot.bot.cmd.impl.moderation;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.external.ExternalFileUtil;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

public class PurgeCommand implements BotCommand {
    
    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public String getDescription() {
        return "Removes the most recent messages sent in a channel. Maximum 100 messages.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.JRMODERATOR;
    }

    @Override
    public Category getCommandCategory() {
        return Category.MODERATION;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.INTEGER, "count", "Number of messages to delete.", true);

    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {

        int messagesToRemove = (int) event.getOption("count").getAsDouble();

        if (messagesToRemove > 100 || messagesToRemove < 2) {
            EmbedBuilder embedBuilder = Embeds.error();
            embedBuilder.setDescription("Message count not within 2 to 100.");
            event.replyEmbeds(embedBuilder.build()).queue();
        } else {
            MessageChannel channel = event.getChannel();
            channel.getHistory().retrievePast(messagesToRemove).queue((messages) -> {
                // Adds the messages to the messageBuilder object
                StringBuilder stringBuilder = new StringBuilder();

                // Iterates through the message history and appends the values to the MessageBuilder.
                for (Message m : messages) {
                    stringBuilder.insert(0,
                            String.format("[%s] (%s): %s",
                                    m.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME),
                                    m.getAuthor().getName(),
                                    m.getContentRaw())
                    );
                    if (!m.getAttachments().isEmpty()) {
                        for (Message.Attachment a : m.getAttachments()) {
                            stringBuilder.insert(0,
                                    String.format(" [ATTACHMENT: %s ]\n",
                                            a.getProxyUrl())
                            );
                        }
                    } else {
                        stringBuilder.insert(0,
                                "\n"
                        );
                    }
                }

                EmbedBuilder embedBuilder = Embeds.info();
                embedBuilder.setDescription(String.format("%s purged %s messages in #%s",
                        event.getUser().getAsMention(),
                        messagesToRemove,
                        event.getChannel().getName()));
                event.replyEmbeds(embedBuilder.build()).queue();

                try {
                    File file = ExternalFileUtil.generateFile("purge_log.txt");
                    Files.writeString(file.toPath(), stringBuilder.toString(), StandardOpenOption.WRITE);

                    TextChannel evidenceLog = event.getJDA().getTextChannelById(
                            AstronoBot.getConfig().purgeEvidenceChannel
                    );

                    evidenceLog.sendMessageEmbeds(embedBuilder.build())
                            .addFile(file)
                            .queue();

                } catch (Exception e) {
                    throw new IllegalStateException();
                }

                channel.purgeMessages(messages);
            });
        }
    }
}