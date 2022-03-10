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
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnableCommnd implements BotCommand {
    @Override
    public String getName() {
        return "enable";
    }

    @Override
    public String getDescription() {
        return "Enable a command and allow it to be used.";
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

        for (Command command1 : CommandManager.discordCommandMap) {
            if (command1.getName().equals(botCommand.getName())) {
                command1.editCommand()
                        .setDefaultEnabled(!Permissions.isPrivileged(botCommand.requiredPermission()))
                        .queue();

                if (!Permissions.isPrivileged(botCommand.requiredPermission()))
                    continue;

                List<CommandPrivilege> privileges = new ArrayList<>();
                for (CPermission perm :
                        Permissions.providingPermissions(botCommand.requiredPermission())) {
                    privileges.addAll(perm.getPrivileges());
                }

                CommandManager.privilegeMap.put(command1.getId(), privileges);
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
        embedBuilder.setDescription("The `" + command + "` command has been successfully enabled!\n*Discord command permissions get cached, you may have to switch channel for this to take effect*");
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}


