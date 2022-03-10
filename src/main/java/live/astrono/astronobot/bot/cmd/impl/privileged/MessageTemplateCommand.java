package main.java.live.astrono.astronobot.bot.cmd.impl.privileged;

import main.java.live.astrono.astronobot.bot.cmd.BotCommand;
import main.java.live.astrono.astronobot.bot.cmd.Category;
import main.java.live.astrono.astronobot.sys.MessageTemplate;
import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import main.java.live.astrono.astronobot.sys.auth.permisson.Permissions;
import main.java.live.astrono.astronobot.sys.util.CommandUtil;
import main.java.live.astrono.astronobot.sys.util.Embeds;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class MessageTemplateCommand implements BotCommand {
    @Override
    public String getName() {
        return "messagetemplate";
    }

    @Override
    public String getDescription() {
        return "Creates or edits a message from a template.";
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
        OptionData templateId = new OptionData(OptionType.STRING, "template_id", "The id of the template to use.", true);
        for (MessageTemplate template : MessageTemplate.values()) {
            templateId.addChoice(template.name(), template.name());
        }

        return new CommandData(getName(), getDescription())
                .addSubcommands(
                        new SubcommandData("create", "Creates a message from a template.")
                                .addOptions(templateId)
                                .addOption(OptionType.CHANNEL, "channel", "The channel to send the message in."),
                        new SubcommandData("edit", "Edits a message from a template.")
                                .addOptions(templateId)
                                .addOption(OptionType.CHANNEL, "channel", "The channel containing the message to edit.", true)
                                .addOption(OptionType.STRING, "message", "The message to edit.", true));
    }

    @Override
    public void execute(SlashCommandEvent event, CPermission permission) {
        MessageTemplate template;
        try {
            template = MessageTemplate.valueOf(CommandUtil.requireArgument(event.getOption("template_id"))
                    .getAsString()
                    .toUpperCase());
        } catch (IllegalArgumentException e) {
            event.replyEmbeds(Embeds.error(permission).setDescription("Invalid template id.").build()).queue();
            return;
        }

        String subCommand = CommandUtil.requireArgument(event.getSubcommandName());
        if (subCommand.equals("create")) {
            OptionMapping mapping = event.getOption("channel");
            MessageChannel channel = CommandUtil.requireArgument(mapping != null ? mapping.getAsMessageChannel() : event.getChannel());
            channel.sendMessage("Queued MessageTemplate Message")
                    .queue(message -> editTemplate(event, template, message));
        }
        if (subCommand.equals("edit")) {
            CommandUtil.requireArgument(
                            CommandUtil.requireArgument(event.getOption("channel"))
                                    .getAsMessageChannel())
                    .retrieveMessageById(CommandUtil.requireArgument(event.getOption("message")).getAsString())
                    .queue(message -> editTemplate(event, template, message),
                            throwable -> event.replyEmbeds(Embeds.commandError(permission)
                                            .setDescription("Unable to find message with provided id.")
                                            .build())
                                    .queue());
        }

    }

    public void editTemplate(SlashCommandEvent event, MessageTemplate template, Message message) {
        template.consume(message);
        event.reply("Successfully edited/created message from template!")
                .setEphemeral(true)
                .queue();
    }
}
