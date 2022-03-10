# Astrono Bot
### Contribution

Astrono Bot is created in java 16 and there are no current rules in place for contribution. If you want to add/modify something you are able to create a fork and submit a pull request.

Use `./gradlew run` to start the bot.

Most pull requests are accepted, do not be afraid to make a change.

### Config File

This project uses a config file inorder to specify things such as bot token, prefix and channel infomation. Please create the file named `config.json` with the format below before starting the bot.

```json
{
  "token": "TOKEN",
  "mysql_url": "jdbc:mysql://MYSQL_IP:3306/SCHEMA_NAME",
  "mysql_username": "MYSQL_USERNAME",
  "mysql_password": "MYSQL_PASSWORD",

  "isDev": true,
  "owners": [
    long
  ],
  "guilds": [
    long
  ],
  "permissionRoles": {
    "bot_developer": long,

    "owner": long,
    "manager": long,
    "admin": long,
    "developer": long,

    "srmoderator": long,
    "moderator": long,
    "jrmoderator": long,

    "artist": long,
    "builder": long,

    "verified": long
  },
  "reactRoles": {
    "server_updates": long,
    "staff_updates": long,
    "events": long,
    "sales": long
  },

  "mutedRole": long,
  "logsChannel": long,
  "purgeEvidenceChannel": long
}
```
