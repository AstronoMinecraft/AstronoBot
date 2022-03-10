package main.java.live.astrono.astronobot.bot.cmd.impl.info;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.bot.cmd.sys.PlayerArgument;
import main.java.live.astrono.astronobot.bot.ingame.Rank;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class PlayerCommand implements BotCommand {
    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public String getDescription() {
        return "View info on a player!";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.USER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.PLAYER_STATISTICS;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.STRING, "player", "The name of a player to lookup!", true);
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        PlayerArgument playerArgument = new PlayerArgument(event.getOption("player").getAsString());
        EmbedBuilder builder = new EmbedBuilder();

        if (!playerArgument.exists()) {
            event.replyEmbeds(Embeds.error()
                    .setDescription("Could not find that player!")
                    .build()).queue();
            return;
        }

        builder.setTitle("Profile");

        builder.addField("Name", (String) playerArgument.getData().get("username"), false);
        builder.addField("UUID", (String) playerArgument.getData().get("uuid"), false);

        StringBuilder stringBuilder = new StringBuilder("");
        for (Rank rank : playerArgument.getRanks()) {
            stringBuilder.append("[");
            stringBuilder.append(rank.name);
            stringBuilder.append("] ");
        }
        builder.addField("Ranks", stringBuilder.toString(), false);


        String whois = (String) playerArgument.getData().get("whois");
        if (playerArgument.getData().get("whois") == null) {
            whois = "N/A";
        }
        builder.addField("Whois", whois.replace("\\n", "\n").replaceAll("&.", ""), false);
        builder.addField("Credits", String.valueOf(playerArgument.getData().get("credits")), false);
        builder.addField("Join Date", "<t:" + playerArgument.getData().get("firstJoin") + ">", false);

        builder.setAuthor((String) playerArgument.getData().get("username"), null, playerArgument.getHead());
        event.replyEmbeds(builder.build()).queue();
    }
}


