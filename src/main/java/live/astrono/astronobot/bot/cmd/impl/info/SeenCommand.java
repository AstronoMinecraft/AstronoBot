package main.java.live.astrono.astronobot.bot.cmd.impl.info;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.bot.cmd.sys.PlayerArgument;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SeenCommand implements BotCommand {
    @Override
    public String getName() {
        return "seen";
    }

    @Override
    public String getDescription() {
        return "View when a account was last online!";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.USER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.PLAYER_STATISTICS;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.STRING, "user", "The name of a user to lookup!", true);
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        PlayerArgument playerArgument = new PlayerArgument(event.getOption("user").getAsString());
        EmbedBuilder builder = new EmbedBuilder();

        if (!playerArgument.exists()) {
            event.replyEmbeds(Embeds.error()
                    .setDescription("Could not find that player!")
                    .build()).queue();
            return;
        }

        builder.addField("Last Seen", "<t:" + playerArgument.getData().get("latestJoin") + ":R>", true);
        builder.setAuthor((String) playerArgument.getData().get("username"), null, playerArgument.getHead());
        event.replyEmbeds(builder.build()).queue();
    }
}

