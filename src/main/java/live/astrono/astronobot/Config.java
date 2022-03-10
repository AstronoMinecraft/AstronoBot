package main.java.live.astrono.astronobot;

import java.util.List;
import java.util.Map;

public class Config {
    public String token;
    public String mysql_url;
    public String mysql_username;
    public String mysql_password;

    public boolean isDev = false;
    public List<Long> owners;
    public List<Long> guilds;
    public Map<String, Long> permissionRoles;
    public Map<String, Long> reactRoles;
    public Map<String, Long> suggestionChannels;
    public long discussionChannel;
    public Map<String, String> suggestionEmojis;
    public Map<String, Long> moduleChannels;
    public long mutedRole;
    public long logsChannel;
    public long betaBotChannel;
    public long purgeEvidenceChannel;
}
