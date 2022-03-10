package main.java.live.astrono.astronobot.bot.cmd.impl.info;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.DateUtil;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DiscordBoostersCommand implements BotCommand {
    
    @Override
    public String getName() {
        return "discordboosters";
    }

    @Override
    public String getDescription() {
        return "Gets current members who are boosting the discord server.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.USER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.GENERAL_STATISTICS;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription());
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        Guild guild = event.getGuild();

        EmbedBuilder embed = Embeds.info();
        embed.setThumbnail("https://cdn.discordapp.com/emojis/699936318398136341.png?v=1");

        event.getGuild().findMembers((member -> member.getTimeBoosted() != null)).onSuccess((members) -> {
            members.sort(Comparator.comparing(Member::getTimeBoosted));
            embed.setDescription("A list of members that are currently boosting this Discord server.\n\n"+
                    getFormattedBoosters(members));
            embed.setColor(new Color(255, 115, 250));
            event.replyEmbeds(embed.build()).queue();
            guild.pruneMemberCache();
        });
    }

    private static String getFormattedBoosters(List<Member> members) {
        if (members.size() == 0) return "*None*";
        
        else {
            SimpleDateFormat format = new SimpleDateFormat("d'/'M'/'y");
    
            List<String> elements = new ArrayList<>();
            for (Member member : members) {
                String timeBoosted = format.format(DateUtil.toDate(member.getTimeBoosted().toInstant().toEpochMilli()));
                elements.add(member.getAsMention() + " - " + timeBoosted);
            }
    
            return String.join("\n", elements);
        }
    }
    
}