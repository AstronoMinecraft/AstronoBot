package main.java.live.astrono.astronobot.sys.auth.permisson;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class Permissions {
    public static final CPermission BOT_OWNER = new CPermission("BOT_OWNER", 6, "bot_developer");

    public static final CPermission OWNER = new CPermission("OWNER", 5, "owner");
    public static final CPermission ADMIN = new CPermission("ADMIN", 4, "admin");
    public static final CPermission DEVELOPER = new CPermission("DEVELOPER", 3, "developer");

    public static final CPermission MODERATOR = new CPermission("SPECIALIST", 2, "moderator");
    public static final CPermission JRMODERATOR = new CPermission("JRSPECIALIST", 1, "jrmoderator");
    public static final CPermission USER = new CPermission("USER", 0, "verified");

    public static final CPermission[] ALL = new CPermission[] {
            BOT_OWNER,

            OWNER,
            ADMIN,
            DEVELOPER,

            MODERATOR,
            JRMODERATOR,

            USER,
    };

    public static Collection<CPermission> providingPermissions(CPermission permission) {
        return Arrays.stream(ALL)
                .filter(toCheck -> toCheck.index >= permission.index)
                .collect(Collectors.toList());
    }

    public static boolean isPrivileged(CPermission permission) {
        return permission.index > Permissions.USER.index;
    }
}
