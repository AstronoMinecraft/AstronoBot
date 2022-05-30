package main.java.live.astrono.astronobot.sys;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.database.impl.DatabaseQuery;
import main.java.live.astrono.astronobot.database.impl.queries.BasicQuery;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class UsernameUtil {
    public static final long VERIFIED = AstronoBot.getConfig().permissionRoles.get("verified");

    public static void updateGuild(HashMap<Long, String> accounts, Guild guild) {
        Role verifiedRoles = guild.getRoleById(VERIFIED);
        guild.loadMembers((member) -> {
            updateMember(member, accounts.get(member.getIdLong()), verifiedRoles);
        });
        guild.pruneMemberCache();
    }

    public static void updateGuild(Guild guild) {
        HashMap<Long, String> accounts = new HashMap<>();

        new DatabaseQuery()
                .query(new BasicQuery("SELECT discordId AS discordId, uuid FROM verified_users WHERE discordId IS NOT NULL AND uuid IS NOT NULL;"))
                .compile()
                .run((result) -> {
                    for (ResultSet set : result) {
                        try {
                            if (!accounts.containsKey(set.getLong("discordId"))) {
                                accounts.put(set.getLong("discordId"), getUsername(set.getString("uuid")));
                            }
                            // big funny bad table
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });

        updateGuild(accounts, guild);
    }

    public static void updateMember(Member member, String verifyName, Role verifiedRole) {
        Guild guild = member.getGuild();
        boolean canUpdateNickName = guild.getSelfMember().hasPermission(Permission.NICKNAME_MANAGE);
        boolean canUpdateRole = guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES);

        // If user isn't verified
        if (guild.getSelfMember().canInteract(member)) {
            if (verifyName == null) {
                // Remove verified role if they have it
                if (member.getRoles().contains(verifiedRole) && verifiedRole != null && canUpdateRole) {
                    guild.removeRoleFromMember(member, verifiedRole)
                            .reason("Removing user's verified role, because they are not verified.")
                            .queue();
                }

                if (canUpdateNickName) {
                    guild.modifyNickname(member, null)
                            .reason("Removing user's nickname, because they are not verified.")
                            .queue();
                }

            } else {
                // Remove verified role if they have it
                if (!member.getRoles().contains(verifiedRole) && verifiedRole != null && canUpdateRole) {
                    guild.addRoleToMember(member, verifiedRole)
                            .reason("Giving user verified role, because they are verified.")
                            .queue();
                }

                if (!member.getEffectiveName().equals(verifyName) && canUpdateNickName) {
                    guild.modifyNickname(member, verifyName)
                            .reason("Updating user's nickname to reflect their name.")
                            .queue();
                }
            }
        }
    }

    public static void updateMember(Member member) {
        new DatabaseQuery()
                .query(new BasicQuery("SELECT uuid FROM verified_users WHERE discordId = ?;", (statement) -> {
                    statement.setLong(1, member.getIdLong());
                }))
                .compile()
                .run((result) -> {
                    if (result.isEmpty()) {
                        return;
                    }

                    ResultSet table = result.getResult();
                    updateMember(member, getUsername(table.getString("uuid")), member.getGuild().getRoleById(VERIFIED));
                });
    }

    public static String getUsername(String uuid) {
        AtomicReference<String> name = new AtomicReference<>();

        new DatabaseQuery()
                .query(new BasicQuery("SELECT username AS name FROM players WHERE uuid = ?;", (statement) -> {
                    statement.setString(1, uuid);
                }))
                .compile()
                .run((result) -> {
                    if (result.isEmpty()) {
                        return;
                    }

                    ResultSet table = result.getResult();
                    name.set(table.getString("name"));
                });

        return name.get();
    }
}
