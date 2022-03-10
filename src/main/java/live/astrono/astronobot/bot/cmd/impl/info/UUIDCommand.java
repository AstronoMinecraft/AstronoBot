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

public class UUIDCommand implements BotCommand {
    @Override
    public String getName() {
        return "uuid";
    }

    @Override
    public String getDescription() {
        return "View info on a player!";
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
                .addOption(OptionType.STRING, "player", "The name of a player to get the uuid for!", true);
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        PlayerArgument playerArgument = new PlayerArgument(event.getOption("player").getAsString());
        EmbedBuilder builder = new EmbedBuilder();

        if (!playerArgument.exists()) {
            event.replyEmbeds(Embeds.error()
                    .setDescription("Could not find that player!")
                    .build()).queue();
            return;
        }

        builder.setTitle("UUID");
        builder.setDescription((String) playerArgument.getData().get("uuid"));
        builder.setAuthor((String) playerArgument.getData().get("username"), null, playerArgument.getHead());
        event.replyEmbeds(builder.build()).queue();
    }
}


