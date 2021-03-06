package main.java.live.astrono.astronobot.bot.tasks.sys;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public interface MidnightTask extends LoopingTask {
    @Override
    default long getInitialStart() {
        LocalDateTime now = LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId());
        LocalDateTime nextRun = now.withHour(22).withMinute(0).withSecond(0);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }
        
        return Duration.between(now, nextRun).toMillis();
    }
    
    @Override
    default long getNextLoop() {
        return TimeUnit.DAYS.toMillis(1);
    }
}
