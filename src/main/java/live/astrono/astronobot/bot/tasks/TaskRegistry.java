package main.java.live.astrono.astronobot.bot.tasks;

import main.java.live.astrono.astronobot.AstronoBot;
import main.java.live.astrono.astronobot.bot.tasks.impl.NameUpdateTask;
import main.java.live.astrono.astronobot.bot.tasks.impl.ServerBoosterTask;
import main.java.live.astrono.astronobot.bot.tasks.sys.LoopingTask;
import main.java.live.astrono.astronobot.bot.tasks.sys.OneTimeTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskRegistry {
    
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    
    public void initialize() {
        if (AstronoBot.config.isDev) return;
        
        register(
                new NameUpdateTask(),
                new ServerBoosterTask()
        );
    }
    
    private void register(LoopingTask... tasks) {
        for (LoopingTask task : tasks) {
            scheduledExecutorService.scheduleAtFixedRate(task,
                    task.getInitialStart(),
                    task.getNextLoop(),
                    TimeUnit.MILLISECONDS);
        }
    }
    
    public void schedule(Runnable runnable, long timeMs) {
        scheduledExecutorService.schedule(runnable, timeMs, TimeUnit.MILLISECONDS);
    }
    
    public void schedule(OneTimeTask task) {
        schedule(task, task.getExecution());
    }
    
}