package main.java.live.astrono.astronobot.bot.cmd.impl.fun;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class PingCommand implements BotCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Gets the ping from the bot to Discord.";
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
        long time = System.currentTimeMillis();
        event.reply("Calculating...").queue(interactionHook -> {
            interactionHook.editOriginal("Ping: " + (System.currentTimeMillis() - time) + "ms").queue();
        });
    }
}
