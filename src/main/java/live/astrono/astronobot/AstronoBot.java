package main.java.live.astrono.astronobot;

import com.google.gson.Gson;
import main.java.live.astrono.astronobot.bot.cmd.CommandManager;
import main.java.live.astrono.astronobot.bot.cmd.impl.administration.DisableCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.administration.EnableCommnd;
import main.java.live.astrono.astronobot.bot.cmd.impl.administration.QueryCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.administration.StatusCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.fun.GarfieldCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.fun.HelpCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.fun.InfoCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.fun.PingCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.info.*;
import main.java.live.astrono.astronobot.bot.cmd.impl.moderation.MuteCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.moderation.PurgeCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.moderation.UnmuteCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.privileged.EchoCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.privileged.MessageTemplateCommand;
import main.java.live.astrono.astronobot.bot.cmd.impl.privileged.StopCommand;
import main.java.live.astrono.astronobot.bot.events.ButtonEvent;
import main.java.live.astrono.astronobot.bot.rolereact.RoleReactHandler;
import main.java.live.astrono.astronobot.bot.tasks.TaskRegistry;
import main.java.live.astrono.astronobot.bot.verification.VerifyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AstronoBot {
    private static final AstronoBot INSTANCE = new AstronoBot();
    public static final Gson GSON = new Gson();
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final CommandManager commandManager = new CommandManager();
    private final TaskRegistry taskRegistry = new TaskRegistry();

    public static Map<Integer, Long> tickets = new HashMap<>();
    public static Integer ticket = 0;

    public static Config config;
    private static JDA jda;

    private long startupTime;

    public AstronoBot() {}

    public static void main(String[] args) {
        try {
            INSTANCE.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws InterruptedException, LoginException, IOException {
        config = GSON.fromJson(Files.readString(Path.of("config.json")), Config.class);

        // Unspecific Commands
        commandManager.addCommand(new HelpCommand());
        commandManager.addCommand(new InfoCommand());
        commandManager.addCommand(new PingCommand());

        // Fun Commands
        commandManager.addCommand(new GarfieldCommand());

        // Info Commands
        commandManager.addCommand(new SeenCommand());
        commandManager.addCommand(new PlayerCommand());
        commandManager.addCommand(new DiscordBoostersCommand());
        commandManager.addCommand(new UUIDCommand());
        commandManager.addCommand(new StaffListCommand());

        // Moderation Commands
        commandManager.addCommand(new MuteCommand());
        commandManager.addCommand(new UnmuteCommand());
        commandManager.addCommand(new PurgeCommand());

        // Administration Commands
        commandManager.addCommand(new QueryCommand());
        commandManager.addCommand(new EchoCommand());
        commandManager.addCommand(new MessageTemplateCommand());

        commandManager.addCommand(new DisableCommand());
        commandManager.addCommand(new EnableCommnd());

        commandManager.addCommand(new StopCommand());
        commandManager.addCommand(new StatusCommand());

        String token = config.token;
        if (token == null) {
            token = System.getenv("DISCORD_TOKEN");
        }

        jda = JDABuilder.createDefault(token)
                .addEventListeners(commandManager,
                        new ButtonEvent(), new VerifyListener(), new RoleReactHandler(),
                        new ListenerAdapter() {
                    @Override
                    public void onReady(@NotNull ReadyEvent event) {
                        startupTime = System.currentTimeMillis();
                    }
                })
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.playing("Astrono Network!" + (config.isDev ? " (DEV)": "")))
                .build();

        jda.awaitReady();
        commandManager.registerCommands();
        taskRegistry.initialize();

        /*
        for (TextChannel textChannel : jda.getCategoryById(918917604880547840L).getTextChannels()) {
            TextChannel channel = jda.getTextChannelById(918914222249889832L);
            EmbedBuilder embedBuilder = Embeds.info();
            embedBuilder.setTitle(textChannel.getName() + " was automatically closed due to a bot restart.");

            embedBuilder.setDescription(textChannel.getName() + " was closed.");

            // Adds the messages to the messageBuilder object
            StringBuilder stringBuilder = new StringBuilder();

            // Iterates through the message history and appends the values to the MessageBuilder.
            for (Message m : textChannel.getIterableHistory()) {
                stringBuilder.insert(0,
                        String.format("[%s] (%s): %s",
                                m.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME),
                                m.getAuthor().getName(),
                                m.getContentRaw())
                );
                if (!m.getAttachments().isEmpty()) {
                    for (Message.Attachment a : m.getAttachments()) {
                        stringBuilder.insert(0,
                                String.format(" [ATTACHMENT: %s ]\n",
                                        a.getProxyUrl())
                        );
                    }
                } else {
                    stringBuilder.insert(0,
                            "\n"
                    );
                }
            }

            File file = ExternalFileUtil.generateFile("ticket_log.txt");
            Files.writeString(file.toPath(), stringBuilder.toString(), StandardOpenOption.WRITE);

            channel.sendMessageEmbeds(embedBuilder.build())
                    .addFile(file)
                    .queue();

            textChannel.delete().queue();
        }

         */
    }

    public static JDA getJda() {
        return jda;
    }

    public static Config getConfig() {
        return config;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public long getStartupTime() {
        return startupTime;
    }

    public static AstronoBot getInstance() {
        return INSTANCE;
    }
}
