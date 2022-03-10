package main.java.live.astrono.astronobot.bot.cmd.impl.moderation;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.CommandUtil;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Objects;

public class MuteCommand implements BotCommand {
    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "Mutes a user.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.DEVELOPER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.MODERATION;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.USER, "user", "The user to mute.", true)
                .addOption(OptionType.STRING, "reason", "The reason this user is being muted.");
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        Member victim = CommandUtil.requireArgument(CommandUtil.requireArgument(event.getOption("user")).getAsMember());
        OptionMapping reasonMapping = event.getOption("reason");
        String reason = reasonMapping != null ? reasonMapping.getAsString() : "Unspecified reason.";

        Guild guild = victim.getGuild();
        guild.addRoleToMember(victim,
                        Objects.requireNonNull(guild.getRoleById(AstronoBot.getInstance().getConfig().mutedRole)))
                .reason(String.format("Muted - \"%s\" by %s <%s>",
                        reason, Objects.requireNonNull(event.getMember()).getUser().getAsTag(), event.getMember().getIdLong()))
                .queue(unused -> event.replyEmbeds(Embeds.success()
                                .setTitle("️️⚒️ Successfully Muted User")
                                .addField("Staff", event.getMember().getAsMention(), false)
                                .addField("Reason", reason, false)
                                .addField("Muted", victim.getAsMention(), false)
                                .build())
                        .queue());
    }
}
