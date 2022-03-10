package main.java.live.astrono.astronobot.bot.cmd.impl.fun;

import main.java.live.astrono.astronobot.Constants;
import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class InfoCommand implements BotCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Allows you to view general information about the bot.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.USER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.OTHER;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription());
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        event.replyEmbeds(Embeds.info()
                        .setTitle("Bot Info")
                        .setThumbnail(Constants.LOGO)
                        .setDescription("The new Astrono Network bot!" +
                                        "\n**Developed By:** <@711974603387306490>")
                        .setFooter("Current Version: " + Constants.VERSION)
                        .build())
                .queue();
    }
}
