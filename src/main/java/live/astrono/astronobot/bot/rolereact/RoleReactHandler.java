package main.java.live.astrono.astronobot.bot.rolereact;

import main.java.live.astrono.astronobot.AstronoBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RoleReactHandler extends ListenerAdapter {
    @Override
    public void onSelectionMenu(@NotNull SelectionMenuEvent event) {
        if (!event.getComponentId().equals("roleReact")) return;
        Guild guild = Objects.requireNonNull(event.getMember()).getGuild();

        for (ReactRole reactRole : ReactRole.values()) {
            String name = reactRole.option.getValue();
            Role role = Objects.requireNonNull(guild.getRoleById(AstronoBot.getInstance().getConfig().reactRoles.get(name)));
            if (event.getValues().contains(name)) guild.addRoleToMember(event.getMember(), role).queue();
            else guild.removeRoleFromMember(event.getMember(), role).queue();
        }

        event.reply("Roles Applied!").setEphemeral(true).queue();
    }
}
