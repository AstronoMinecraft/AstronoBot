package main.java.live.astrono.astronobot.sys.auth.permisson;

import main.java.live.astrono.astronobot.AstronoBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.Collection;
import java.util.Collections;

public class CPermission {
    public final int index;
    public final String id;
    private final String role;

    CPermission(String id, int index, String role) {
        this.id = id;
        this.index = index;
        this.role = role;

    }

    public long getRoleId() {
        return AstronoBot.getInstance().getConfig().permissionRoles.get(role);
    }

    public boolean matches(Member member) {
        return member.getRoles().contains(member.getGuild().getRoleById(getRoleId()));
    }

    public Collection<CommandPrivilege> getPrivileges() {
        return Collections.singletonList(CommandPrivilege.enableRole(getRoleId()));
    }
}
