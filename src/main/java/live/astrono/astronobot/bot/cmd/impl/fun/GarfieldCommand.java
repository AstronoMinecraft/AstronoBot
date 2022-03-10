package main.java.live.astrono.astronobot.bot.cmd.impl.fun;

import com.google.gson.JsonParser;
import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GarfieldCommand implements BotCommand {
    @Override
    public String getName() {
        return "garfield";
    }

    @Override
    public String getDescription() {
        return "Gets a random garfield comic.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.USER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.OTHER;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription());
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        EmbedBuilder builder = new EmbedBuilder();
        try {
            URL url = new URL("https://labscore.vercel.app/v2/garfield");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String link = JsonParser.parseString(in.readLine()).getAsJsonObject().get("link").getAsString();

                if (link == null) {
                    throw new IOException();
                } else {
                    builder.setTitle("Garfield Comic");
                    builder.setImage(link);
                    builder.setColor(new Color(252, 166, 28));
                }
            }
        } catch (Exception e) {
            builder.setTitle(":rotating_light: API BROKE :rotating_light:");
            builder.setDescription("Ping: <@711974603387306490>");
        }
        event.replyEmbeds(builder.build()).queue();
    }
}
