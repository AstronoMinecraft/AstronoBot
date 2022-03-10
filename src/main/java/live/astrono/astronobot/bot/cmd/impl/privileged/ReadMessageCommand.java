package main.java.live.astrono.astronobot.bot.cmd.impl.privileged;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.CommandUtil;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.nio.charset.StandardCharsets;

public class ReadMessageCommand implements BotCommand {
    @Override
    public String getName() {
        return "readmessage";
    }

    @Override
    public String getDescription() {
        return "Reads the contents of a message.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.DEVELOPER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.OTHER;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.CHANNEL, "channel", "The channel containing the message to read.", true)
                .addOption(OptionType.STRING, "message", "The id of the message to read.", true);
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        CommandUtil.requireArgument(
                        CommandUtil.requireArgument(event.getOption("channel"))
                                .getAsMessageChannel())
                .retrieveMessageById(CommandUtil.requireArgument(event.getOption("message")).getAsString())
                .queue(message -> {
                    StringBuilder messageContent = new StringBuilder(
                            String.format("Message content for message %d in <#%d>." +
                                        "\nCreated by: %s (%d)" +
                                        "\nSent at: <t:%d>." +
                                        "\nEdited at: <t:%d>" +
                                        "\nJump: %s" +
                                        "\nAttachments:",
                            message.getIdLong(), message.getChannel().getIdLong(),
                            message.getAuthor().getAsTag(), message.getAuthor().getIdLong(),
                            message.getTimeCreated().toEpochSecond(), message.getTimeCreated().toEpochSecond(),
                            message.getJumpUrl()));
                    for (Message.Attachment attachment :
                            message.getAttachments()) {
                        messageContent.append(String.format("%d - %s (%s): %s @ <t:%d>\n",
                                attachment.getIdLong(), attachment.getFileName(),
                                attachment.getContentType(), attachment.getProxyUrl(),
                                attachment.getTimeCreated().toEpochSecond()));
                    }
                    StringBuilder extraContent = new StringBuilder();
                    extraContent.append("Embeds:\n");
                    for (MessageEmbed embed :
                            message.getEmbeds()) {
                        extraContent.append(embed.toData() + "\n");
                    }
                    event.reply("Sending message content...")
                            .setEphemeral(true)
                            .queue(interactionHook -> {
                                    MessageAction action = event.getChannel()
                                            .sendMessage(messageContent.toString())
                                            .addFile(message.getContentRaw().getBytes(StandardCharsets.UTF_8), "message.md");
                                    if (!message.getEmbeds().isEmpty())
                                            action.addFile(extraContent.toString().getBytes(StandardCharsets.UTF_8), "extra.txt");
                                    action.queue();
                                }, throwable -> event.replyEmbeds(Embeds.commandError(permission)
                                            .setDescription("Unable to find message with provided id.")
                                            .build())
                                    .setEphemeral(true)
                                    .queue());
                });
    }
}
