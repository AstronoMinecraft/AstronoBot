package main.java.live.astrono.astronobot.bot.cmd;

import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface BotCommand {
    String getName();
    String getDescription();
    CPermission requiredPermission();
    Category getCommandCategory();

    CommandData createCommand();

    void execute(SlashCommandEvent event, CPermission permission);
}
