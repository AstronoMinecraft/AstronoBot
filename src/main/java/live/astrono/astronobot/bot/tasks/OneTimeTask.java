package main.java.live.astrono.astronobot.bot.tasks;

public interface OneTimeTask extends Runnable {
    long getExecution();
}