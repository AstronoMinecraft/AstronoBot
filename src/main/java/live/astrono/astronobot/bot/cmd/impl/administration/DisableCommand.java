package main.java.live.astrono.astronobot.bot.cmd.impl.administration;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.bot.cmd.CommandManager;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DisableCommand implements BotCommand {
    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public String getDescription() {
        return "Disable a command and stop it from being used.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.DEVELOPER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.ADMINISTRATION;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.STRING, "command", "The name of the command to disable.", true);
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        String command = event.getOption("command").getAsString();
        BotCommand botCommand = CommandManager.commandMap.get(command);

        if (botCommand == null) {
            EmbedBuilder embedBuilder = Embeds.error();
            embedBuilder.setDescription("The specified command does not exist");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        if (command.equals("enable")) {
            EmbedBuilder embedBuilder = Embeds.error();
            embedBuilder.setDescription("Go sit in a corner and think about what you just tried to do...");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        for (Command command1 : CommandManager.discordCommandMap) {
            if (command1.getName().equals(botCommand.getName())) {
                command1.editCommand()
                        .setDefaultEnabled(false)
                        .queue();

                CommandManager.privilegeMap.put(command1.getId(), new ArrayList<>());
            }
        }

        List<Guild> guildList = AstronoBot.getConfig().guilds.stream()
                .map(event.getJDA()::getGuildById)
                .collect(Collectors.toList());

        for (Guild guild : guildList) {
            try {
                guild.updateCommandPrivileges(CommandManager.privilegeMap)
                        .queue();
            } catch (Exception e) {
                System.err.println("Error setting up command privileges in " + guild.getName() + " (this is probably normal)");
            }
        }

        EmbedBuilder embedBuilder = Embeds.info();
        embedBuilder.setDescription("The `" + command + "` command has been successfully disabled\n*Discord command permissions get cached, you may have to switch channel for this to take effect*!");
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}

