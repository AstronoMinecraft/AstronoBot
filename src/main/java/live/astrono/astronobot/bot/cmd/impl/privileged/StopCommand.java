package main.java.live.astrono.astronobot.bot.cmd.impl.privileged;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class StopCommand implements BotCommand {
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stops the bot.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.BOT_OWNER;
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
        event.reply("Exiting now...").setEphemeral(true).queue();
        AstronoBot.getInstance().getJda().shutdown();
    }
}
