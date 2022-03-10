package main.java.live.astrono.astronobot.bot.cmd.impl.administration;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.Constants;
import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class StatusCommand implements BotCommand {
    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getDescription() {
        return "Returns general info about the bot and its environment.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.DEVELOPER;
    }

    @Override
    public Category getCommandCategory() {
        return Category.ADMINISTRATION;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription());
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {

        long s = (System.currentTimeMillis() - AstronoBot.getInstance().getStartupTime()) / 1000;
        String time = String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));

        long l = Runtime.getRuntime().maxMemory();
        long m = Runtime.getRuntime().totalMemory();
        long n = Runtime.getRuntime().freeMemory();
        long o = m - n;
        String mem = String.format("%2d%% %d/%dMB", o * 100L / l, toMiB(o), toMiB(l));
        String alloc = String.format("%2d%% %dMB", m * 100L / l, toMiB(m));

        event.replyEmbeds(Embeds.info()
                        .setTitle("Bot Status")
                        .addField("Uptime", time, true)
                        .addField("Java", System.getProperty("java.version")
                                + "-" + System.getProperty("java.vendor"), true)
                        .addField("OS", System.getProperty("os.name")
                                + "-" + System.getProperty("os.arch")
                                + "@" + System.getProperty("os.version"), true)
                        .addField("Memory", mem + " (Allocated: " + alloc + ")", true)
                        .addField("Version", Constants.VERSION, true)
                        .build())
                .queue();
    }

    public static long toMiB(long bytes) {
        return bytes / 1024L / 1024L;
    }
}
