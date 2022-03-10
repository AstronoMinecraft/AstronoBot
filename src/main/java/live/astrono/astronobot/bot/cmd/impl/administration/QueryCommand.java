package main.java.live.astrono.astronobot.bot.cmd.impl.administration;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.database.impl.DatabaseQuery;
import main.java.live.astrono.astronobot.database.impl.queries.BasicQuery;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryCommand implements BotCommand {

    @Override
    public String getName() {
        return "query";
    }

    @Override
    public String getDescription() {
        return "Executes given query.";
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
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.STRING, "query", "The MySQL command to execute.", true);
    }
    
    @SuppressWarnings("LanguageMismatch")
    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        String query = event.getOption("query").getAsString();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("SQL Result");
        
        try {
            new DatabaseQuery()
                    .query(new BasicQuery(query))
                    .compile()
                    .run((set) -> {
                        int width = set.getResult().getMetaData().getColumnCount();
                        List<String> objects = new ArrayList<>();
                        for (ResultSet resultSet : set) {
                            HashMap<String, String> entries = new HashMap<>();
                            for (int i = 1; i <= width; i++) {
                                String columnName = resultSet.getMetaData().getColumnName(i);
                                entries.put(columnName, String.valueOf(resultSet.getObject(i)));
                            }
                            objects.add(StringUtil.asciidocStyle(entries));
                        }
                        
                        for (int i = 0; i < objects.size(); i++) {
                            if (i > 25) {
                                break;
                            }
                            builder.addField("Row: " + (i + 1), String.format("```asciidoc\n%s```", objects.get(i)), false);
                        }
                        
                        event.replyEmbeds(builder.build())
                                .queue();
                    });
        } catch (IllegalStateException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String sStackTrace = sw.toString();
            builder.setTitle("Query failed! " + e.getClass().getName());
            event.replyEmbeds(builder.build()).queue();
            event.getChannel().sendMessage(String.format("```%s```", sStackTrace.length() >= 1500 ? sStackTrace.substring(0, 1500) : sStackTrace)).queue();
        }
    }
}
