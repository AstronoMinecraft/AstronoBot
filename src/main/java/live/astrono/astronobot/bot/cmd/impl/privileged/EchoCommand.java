package main.java.live.astrono.astronobot.bot.cmd.impl.privileged;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.CommandUtil;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import main.java.live.astrono.astronobot.sys.util.StringUtil;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class EchoCommand implements BotCommand {
    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getDescription() {
        return "Echos the text passed to the command.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.DEVELOPER;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.STRING, "text", "The text to echo.", true)
                .addOption(OptionType.CHANNEL, "channel", "The channel to send the message in.");
    }

    @Override
    public Category getCommandCategory() {
        return Category.OTHER;
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        OptionMapping channelMapping = event.getOption("channel");
        MessageChannel channel;
        if (channelMapping != null) {
            if (channelMapping.getChannelType() != ChannelType.TEXT) {
                event.reply("This command can only be run with a guild text channel.").setEphemeral(true).queue();
                return;
            }
            channel = channelMapping.getAsMessageChannel();
        } else channel = event.getChannel();

        String msg;
        try {
            msg = StringUtil.unescape(CommandUtil.requireArgument(event.getOption("text")).getAsString());
        } catch (RuntimeException e) {
            event.reply("Invalid escape sequence.").setEphemeral(true).queue();
            return;
        }
        channel
                .sendMessage(msg)
                .queue(message -> {
                    event.reply("Message sent!").setEphemeral(true).queue();
                    event.getMember().getGuild().getTextChannelById(AstronoBot.getInstance().getConfig().logsChannel)
                            .sendMessageEmbeds(Embeds.info()
                                    .setTitle(String.format("%s <%d> used /echo!", event.getMember().getUser().getAsTag(), event.getMember().getIdLong()))
                                    .setDescription(msg)
                                    .build())
                            .queue();
                });
    }
}
