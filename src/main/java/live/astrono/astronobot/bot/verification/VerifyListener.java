package main.java.live.astrono.astronobot.bot.verification;

import main.java.live.astrono.astronobot.database.impl.DatabaseQuery;
import main.java.live.astrono.astronobot.database.impl.queries.BasicQuery;
import main.java.live.astrono.astronobot.sys.RandomString;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;

public class VerifyListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getChannel().getIdLong() == 934774978417680474L) {
            Role role = event.getGuild().getRoleById(934747993356251159L);

            event.getMessage().delete()
                    .queue();

            new DatabaseQuery()
                    .query(new BasicQuery("SELECT * FROM players WHERE verifyCode = ? LIMIT 1;", (statement) -> {
                        statement.setString(1, event.getMessage().getContentRaw());
                    })).compile()
                    .runAsync((result) -> {
                        if (!result.isEmpty()) {
                            ResultSet resultSet = result.getResult();
                            String uuid = resultSet.getString("uuid");
                            String username = resultSet.getString("username");

                            new DatabaseQuery()
                                    .query(new BasicQuery("SELECT * FROM verified_users WHERE uuid = ? LIMIT 1;", (statement) -> {
                                        statement.setString(1, uuid);
                                    })).compile()
                                    .run((res) -> {
                                        if (!res.isEmpty()) {
                                            try {
                                                User user = event.getJDA().getUserById(res.getResult().getLong("discordId"));

                                                if (user != null) {
                                                    Member member = event.getGuild().getMember(user);

                                                    if (member != null) {
                                                        event.getGuild().removeRoleFromMember(member, role).queue();
                                                    }

                                                    user.openPrivateChannel().queue((privateChannel -> {
                                                        try {
                                                            privateChannel.sendMessage("Your verification with **" + username + "** was removed due to verifying with another discord account!")
                                                                    .queue();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }));
                                                }
                                            } catch (Exception ignored) {
                                                ignored.printStackTrace();
                                            }

                                            new DatabaseQuery()
                                                    .query(new BasicQuery("UPDATE verified_users SET discordId = ? WHERE uuid = ?", (statement) -> {
                                                        statement.setLong(1, event.getAuthor().getIdLong());
                                                        statement.setString(2, uuid);
                                                    })).compile();
                                        } else {
                                            new DatabaseQuery()
                                                    .query(new BasicQuery("INSERT INTO verified_users (uuid, username, discordId) VALUES (?,?,?)", (statement) -> {
                                                        statement.setString(1, uuid);
                                                        statement.setString(2, username);
                                                        statement.setLong(3, event.getAuthor().getIdLong());
                                                    })).compile();
                                        }
                                    });

                            event.getAuthor().openPrivateChannel().queue((privateChannel -> {
                                privateChannel.sendMessage("You **successfully** verified!")
                                        .queue();
                            }));

                            event.getGuild().addRoleToMember(event.getMember(), role).queue();
                            event.getMember().modifyNickname(username)
                                    .queue();

                            RandomString randomString = new RandomString(6);
                            String code = randomString.nextString();
                            new DatabaseQuery()
                                    .query(new BasicQuery("UPDATE players SET verifyCode = ? WHERE uuid = ?", (statement) -> {
                                        statement.setString(1, "#" + code);
                                        statement.setString(2, uuid);
                                    })).compile();
                        } else {
                            event.getAuthor().openPrivateChannel().queue((privateChannel -> {
                                privateChannel.sendMessage("Verification attempt **failed**, the code you entered was invalid!")
                                        .queue();
                            }));
                        }
                    });
        }
    }
}
