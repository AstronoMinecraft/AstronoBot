package main.java.live.astrono.astronobot.bot.tasks;

import main.java.live.astrono.astronobot.AstronoBot;
import net.dv8tion.jda.api.entities.TextChannel;

public interface LoopingTask extends Runnable {
    TextChannel RESPONSE_CHANNEL = AstronoBot.getJda().getTextChannelById(AstronoBot.config.logsChannel);
    
    long getInitialStart();
    
    long getNextLoop();
}