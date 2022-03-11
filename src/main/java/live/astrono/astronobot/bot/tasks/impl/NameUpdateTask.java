package main.java.live.astrono.astronobot.bot.tasks.impl;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.bot.tasks.sys.LoopingTask;
import main.java.live.astrono.astronobot.sys.UsernameUtil;

import java.util.concurrent.TimeUnit;

public class NameUpdateTask implements LoopingTask {
    
    @Override
    public long getInitialStart() {
        return 0;
    }
    
    @Override
    public long getNextLoop() {
        return TimeUnit.MINUTES.toMillis(10);
    }
    
    @Override
    public void run() {
        System.out.println("Updating usernames on the discord...");
        UsernameUtil.updateGuild(AstronoBot.getJda().getGuildById(AstronoBot.getConfig().guilds.get(0)));
        System.out.println("Updated usernames on the discord!");
    }
}