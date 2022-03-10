package main.java.live.astrono.astronobot.sys.util;

public class CommandUtil {
    public static <T> T requireArgument(T object) {
        if (object == null) throw new CommandException("Invalid command arguments");
        return object;
    }
}
