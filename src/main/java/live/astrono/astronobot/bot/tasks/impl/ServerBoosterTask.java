package main.java.live.astrono.astronobot.bot.tasks.impl;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.bot.tasks.sys.LoopingTask;
import main.java.live.astrono.astronobot.bot.tasks.sys.MidnightTask;
import main.java.live.astrono.astronobot.database.impl.DatabaseQuery;
import main.java.live.astrono.astronobot.database.impl.queries.BasicQuery;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ServerBoosterTask implements MidnightTask {
    @Override
    public void run() {
        Guild guild = AstronoBot.getJda().getGuildById(934747993356251156L);
        ArrayList<String> mentions = new ArrayList<>();
        System.out.println("Checking for server boosters...");

        if (guild != null) {
            guild.findMembers((member -> member.getTimeBoosted() != null)).onSuccess((members) -> {
                if (members.size() == 0)
                    return;

                for (Member member : members) {
                    new DatabaseQuery()
                            .query(new BasicQuery("SELECT * FROM verified_users WHERE discordId = ?;", (statement) -> {
                                statement.setLong(1, member.getIdLong());
                            }))
                            .compile()
                            .run((result) -> {
                                if (result.isEmpty()) {
                                    return;
                                }

                                ResultSet table = result.getResult();

                                new DatabaseQuery()
                                        .query(new BasicQuery("SELECT * FROM players WHERE uuid = ?;", (statement) -> {
                                            statement.setString(1, table.getString("uuid"));
                                        }))
                                        .compile()
                                        .run((rslt) -> {
                                            if (rslt.isEmpty()) {
                                                return;
                                            }

                                            ResultSet tbl = rslt.getResult();

                                            System.out.println(member.getIdLong() + " : " + tbl.getString("uuid"));

                                            new DatabaseQuery()
                                                    .query(new BasicQuery("UPDATE players SET gold = ? WHERE uuid = ?", (statement) -> {
                                                        statement.setInt(1, tbl.getInt("gold") + 10);
                                                        statement.setString(2, table.getString("uuid"));
                                                    })).compile();
                                        });
                            });

                    mentions.add(member.getAsMention());
                }

                Objects.requireNonNull(guild.getTextChannelById(952095317115953152L))
                        .sendMessage(String.join(" ", mentions) + " have all been given **10 Astrono Gold** for boosting one day.")
                        .queue();

                guild.pruneMemberCache();
            });
        }

        System.out.println("Awarded the server boosters Astrono Gold!");
    }
}