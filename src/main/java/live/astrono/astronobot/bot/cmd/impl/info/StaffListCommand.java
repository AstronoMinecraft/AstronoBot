package main.java.live.astrono.astronobot.bot.cmd.impl.info;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.bot.ingame.Rank;
import main.java.live.astrono.astronobot.bot.ingame.RankBranch;
import main.java.live.astrono.astronobot.database.impl.DatabaseQuery;
import main.java.live.astrono.astronobot.database.impl.queries.BasicQuery;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.menu.MultiSelectorBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffListCommand implements BotCommand {
    @Override
    public String getName() {
        return "stafflist";
    }

    @Override
    public String getDescription() {
        return "Gets current staff members.";
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
        return new CommandData(getName(), getDescription());
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        MultiSelectorBuilder builder = new MultiSelectorBuilder();
        builder.setChannel(event.getChannel().getIdLong());
        builder.setUser(event.getMember().getIdLong());

        new DatabaseQuery()
                .query(new BasicQuery("SELECT * FROM players WHERE (moderation > 0 || administration > 0 || builder = 1);", (statement) -> {

                })).compile()

                .runAsync((result) -> {
                    Map<Rank, List<String>> ranks = new HashMap<>();

                    registerRank(ranks,
                            Rank.BUILDER,

                            Rank.JRMOD,
                            Rank.MOD,
                            Rank.SRMOD,

                            Rank.ADMIN,
                            Rank.MANAGER,
                            Rank.OWNER,

                            Rank.DEVELOPER
                    );

                    for (ResultSet set : result) {
                        String name = set.getString("username");
                        int moderationNum = set.getInt("moderation");
                        int administrationNum = set.getInt("administration");
                        int builderNum = set.getInt("builder");

                        if (administrationNum > 0) {
                            ranks.get(Rank.fromBranch(RankBranch.ADMINISTRATION, administrationNum)).add(name);
                        }

                        if (moderationNum == 0 && builderNum > 0 && administrationNum == 0) {
                            ranks.get(Rank.fromBranch(RankBranch.BUILDER, builderNum)).add(name);
                        } else if (moderationNum > 0 && administrationNum == 0) {
                            ranks.get(Rank.fromBranch(RankBranch.MODERATION, moderationNum)).add(name);
                        }
                    }

                    EmbedBuilder buildersPage = new EmbedBuilder();
                    buildersPage.addField("Builders", String.join("\n", ranks.get(Rank.BUILDER)), false);

                    EmbedBuilder moderationPage = new EmbedBuilder();
                    moderationPage.addField("SrMods", String.join("\n", ranks.get(Rank.SRMOD)), false);
                    moderationPage.addField("Mods", String.join("\n", ranks.get(Rank.MOD)), false);
                    moderationPage.addField("JrMods", String.join("\n", ranks.get(Rank.JRMOD)), false);

                    EmbedBuilder adminPage = new EmbedBuilder();
                    adminPage.addField("Owner", String.join("\n", ranks.get(Rank.OWNER)), false);
                    adminPage.addField("Managers", String.join("\n", ranks.get(Rank.MANAGER)), false);
                    adminPage.addField("Admins", String.join("\n", ranks.get(Rank.ADMIN)), false);

                    EmbedBuilder devPage = new EmbedBuilder();
                    devPage.addField("Astrono Developers", String.join("\n", ranks.get(Rank.DEVELOPER)), false);

                    builder.addPage("Moderation", moderationPage);
                    builder.addPage("Administration", adminPage);
                    builder.addPage("Builders", buildersPage);
                    builder.addPage("Developers", devPage);

                    builder.build().send(event);
                });
    }

    private void registerRank(Map<Rank, List<String>> map, Rank... ranks) {
        for (Rank rank : ranks) {
            map.put(rank, new ArrayList<>());
        }
    }
}
