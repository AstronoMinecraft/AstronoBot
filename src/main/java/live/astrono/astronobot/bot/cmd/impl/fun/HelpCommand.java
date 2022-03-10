package main.java.live.astrono.astronobot.bot.cmd.impl.fun;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.bot.cmd.CommandManager;
import main.java.live.astrono.astronobot.sys.auth.Auth;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.menu.MultiSelectorBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.*;

public class HelpCommand implements BotCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Gets information about the commands provided by this bot.";
    }

    @Override
    public CPermission requiredPermission() {
        return Permissions.USER;
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription());
    }

    @Override
    public Category getCommandCategory() {
        return Category.OTHER;
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        String helpInfo = null;
        if (helpInfo == null) {

            Map<Category, EmbedBuilder> categories = new LinkedHashMap<>();
            MultiSelectorBuilder selector = new MultiSelectorBuilder();
            selector.setUser(event.getMember().getIdLong());
            selector.setChannel(event.getChannel().getIdLong());

            EmbedBuilder homeBuilder = new EmbedBuilder();
            homeBuilder.setDescription("Commands that are available to you are listed in the pages below. To select a page, react to the message. Any additional questions may be forwarded to TechStreet");
            homeBuilder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            //homeBuilder.setFooter("Your permissions: " + PermissionHandler.getPermission(event.getMember()));
            selector.addPage("Home", homeBuilder, true);

            // Initialize the pages in advance so we can get a nice order.
            categories.put(Category.PLAYER_STATISTICS, new EmbedBuilder());
            categories.put(Category.GENERAL_STATISTICS, new EmbedBuilder());
            categories.put(Category.OTHER, new EmbedBuilder());
            categories.put(Category.MODERATION, new EmbedBuilder());
            categories.put(Category.ADMINISTRATION, new EmbedBuilder());

            List<BotCommand> commandList = new ArrayList<>(CommandManager.commandMap.values());
            commandList.sort(Comparator.comparing(BotCommand::getName));
            for (BotCommand command : commandList) {
                Category category = command.getCommandCategory();
                int perm = Auth.getUserPermission(event.getMember()).index;

                if (perm >= command.requiredPermission().index) {
                    EmbedBuilder embedBuilder = categories.get(category);
                    embedBuilder.addField("/" + command.getName(), command.getDescription(), false);
                }

            }
            // Remove pages that have nothing you have access to.
            categories.entrySet().removeIf((entry) -> entry.getValue().getFields().size() == 0);
            for (Map.Entry<Category, EmbedBuilder> entries : categories.entrySet()) {
                EmbedBuilder embedBuilder = entries.getValue();
                Category category = entries.getKey();
                embedBuilder.setDescription(category.getDescription());
                selector.addPage(category.getName(), embedBuilder, category.getEmoji());
            }
            selector.build().send(event);
        } else {
            BotCommand command = CommandManager.commandMap.get(helpInfo);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Command Information");
            builder.addField("Name", command.getName(), false);
            builder.addField("Description", command.getDescription(), false);
            //builder.addField("Argument", FormatUtil.displayArguments(context), true);
            builder.addField("Category", command.getCommandCategory().toString(), true);
            //builder.addField("Role Required", String.format("<@&%s>", command.requiredPermission())), true);

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
