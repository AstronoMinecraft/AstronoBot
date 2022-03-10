package main.java.live.astrono.astronobot.sys.auth;

import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import net.dv8tion.jda.api.entities.Member;

public class Auth {
    public static CPermission getUserPermission(Member user) {
        for (CPermission permission : Permissions.ALL) {
            if (permission.matches(user)) return permission;
        }

        return Permissions.USER;
    }
}
