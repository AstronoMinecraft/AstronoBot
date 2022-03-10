package main.java.live.astrono.astronobot.bot.cmd;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.sys.auth.Auth;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.CommandException;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CommandManager extends ListenerAdapter {
    public static final Map<String, BotCommand> commandMap = new HashMap<>();
    public static final Map<String, Collection<? extends CommandPrivilege>> privilegeMap = new HashMap<>();
    public static List<Command> discordCommandMap = Collections.emptyList();

    public void addCommand(BotCommand command) {
        commandMap.put(command.getName(), command);
    }

    public void registerCommands() {
        AstronoBot bot = AstronoBot.getInstance();
        JDA jda = bot.getJda();
        jda.updateCommands().queue(); // empty global commands

        List<Guild> guildList = bot.getConfig().guilds.stream()
                .map(jda::getGuildById)
                .collect(Collectors.toList());

        for (Guild guild : guildList) {
            guild
                    .updateCommands()
                    .addCommands(commandMap.values().stream()
                            .map(command -> command.createCommand().setDefaultEnabled(!Permissions.isPrivileged(command.requiredPermission())))
                            .collect(Collectors.toList()))
                    .queue(commands1 -> {
                        discordCommandMap = commands1;

                        for (Command cmd :
                                commands1) {

                            BotCommand botCommand = commandMap.get(cmd.getName());
                            if (!Permissions.isPrivileged(botCommand.requiredPermission())) continue;

                            List<CommandPrivilege> privileges = new ArrayList<>();
                            for (CPermission permission :
                                    Permissions.providingPermissions(botCommand.requiredPermission())) {
                                privileges.addAll(permission.getPrivileges());
                            }

                            privilegeMap.put(cmd.getId(), privileges);
                        }

                        try {
                            guild.updateCommandPrivileges(privilegeMap)
                                    .queue();
                        } catch (Exception e) {
                            System.err.println("Error setting up command privileges in " + guild.getName() + " (this is probably normal)");
                        }
                    });
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        BotCommand command = commandMap.get(event.getName());
        if (command == null) {
            event.reply("Error executing command.").queue();
            return;
        }

        CPermission permission = Auth.getUserPermission(Objects.requireNonNull(event.getMember()));
        if (permission.index < commandMap.get(event.getName()).requiredPermission().index) {
            event.replyEmbeds(Embeds.error(permission)
                            .addField("No Permission!", "You don't have permission to use this command.", false)
                            .build())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        try {
            command.execute(event, permission);
        } catch (CommandException e) {
            event.replyEmbeds(Embeds.error(permission)
                            .setDescription(e.getMessage())
                            .build())
                    .setEphemeral(true)
                    .queue();
        } catch (Exception e) {
            System.err.println("Error in command: " + command.getName());
            e.printStackTrace();

            event.replyEmbeds(Embeds.commandError(permission).build())
                    .setEphemeral(true)
                    .queue();
        }
    }

    public Map<String, BotCommand> getCommandMap() {
        return commandMap;
    }
}
