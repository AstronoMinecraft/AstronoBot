package main.java.live.astrono.astronobot.bot.tasks.sys;

public interface OneTimeTask extends Runnable {
    long getExecution();
}