package main.java.live.astrono.astronobot.sys.util.menu;

import main.java.live.astrono.astronobot.sys.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiSelector {
    
    private final MultiSelectorPage[] pages;
    private final long channel;
    private final long user;
    
    public MultiSelector(long channel, long user, List<MultiSelectorPage> pages) {
        this.pages = pages.toArray(new MultiSelectorPage[0]);
        this.channel = channel;
        this.user = user;
    }
    
    public void send(SlashCommandEvent slashCommandEvent) {
        for (MultiSelectorPage page : pages) {
            EmbedBuilder pageBuilder = page.getPage();
            pageBuilder.setTitle(page.getName());
        }
        
        Map<String, MultiSelectorPage> pageMap = new HashMap<>(pages.length);
        List<Button> buttons = new ArrayList<>();
        for (MultiSelectorPage page : pages) {
            if (page.isHidden()) {
                continue;
            }
            
            Button button = Button.secondary(getButtonKey(page), page.getName());
            if (page.getCustomEmote() != null) {
                button = button.withEmoji(Emoji.fromUnicode(page.getCustomEmote()));
            }
            
            pageMap.put(button.getId(), page);
            buttons.add(button);
        }

        slashCommandEvent.replyEmbeds(pages[0].getPage().build())
                .addActionRows(Util.of(buttons))
                .queue((message) -> {
                    message.retrieveOriginal().queue((msg) -> {
                        ButtonHandler.addListener(user, msg, event -> {
                            event.deferEdit().queue();
                            message.editOriginalEmbeds(pageMap.get(event.getComponentId()).getPage().build()).queue();
                        }, true);
                    });
                });
    }
    
    private String getButtonKey(MultiSelectorPage page) {
        return page.getName() + "-BUTTON";
    }
}