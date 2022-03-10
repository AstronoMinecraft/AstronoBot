package main.java.live.astrono.astronobot.bot.suggestions;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.sys.util.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

public class SuggestionHandler extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        for (SuggestionChannel channel : SuggestionChannel.VALUES) {
            if (event.getChannel().getIdLong() == channel.getChannelId()) {
                TextChannel discussionChannel = event.getGuild()
                        .getTextChannelById(AstronoBot.getInstance().getConfig().discussionChannel);
                if (discussionChannel == null) {
                    System.err.println("Unable to find discussion channel.");
                    return;
                }

                if (channel.voted) {
                    Map<String, String> suggestionEmojis = AstronoBot.getInstance().getConfig().suggestionEmojis;;
                    event.getMessage().addReaction(suggestionEmojis.get("upvote")).queue();
                    event.getMessage().addReaction(suggestionEmojis.get("downvote")).queue();
                }

                Member sender = Objects.requireNonNull(event.getMember());

                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Colors.SUGGESTION)
                        .setTimestamp(OffsetDateTime.now())
                        .setAuthor(sender.getEffectiveName(), null, event.getAuthor().getAvatarUrl())
                        .setTitle(String.format(":incoming_envelope: New %s posted", channel.singular.toLowerCase()),
                                event.getMessage().getJumpUrl())
                        .setDescription(event.getMessage().getContentRaw())
                        .setFooter(String.format("Author: %s (%d)",
                                event.getAuthor().getAsTag(),
                                event.getAuthor().getIdLong()));

                discussionChannel.sendMessageEmbeds(embed.build()).queue();
                break;
            }
        }
    }

    private static String fetchFile(String sURL) {
        StringBuilder file = new StringBuilder();

        try {
            URL url = new URL(sURL);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                file.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.toString();
    }

    private static String encode(String url) {
        try {
            String encodeURL = URLEncoder.encode(url, "UTF-8");
            return encodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while encoding" + e.getMessage();
        }
    }

    private static String decode(String url) {
        try {
            String prevURL="";
            String decodeURL=url;
            while(!prevURL.equals(decodeURL)) {
                prevURL=decodeURL;
                decodeURL = URLDecoder.decode(decodeURL, "UTF-8");
            }
            return decodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while decoding" + e.getMessage();
        }
    }
}
