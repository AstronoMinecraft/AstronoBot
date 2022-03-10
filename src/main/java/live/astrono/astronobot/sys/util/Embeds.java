package main.java.live.astrono.astronobot.sys.util;

import main.java.live.astrono.astronobot.sys.auth.permisson.CPermission;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

public class Embeds {
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull EmbedBuilder permissioned(@NotNull CPermission permission) {
        return success()
                .setFooter("Permissions: " + permission.id);
    }

    @Contract(value = "-> new", pure = true)
    public static @NotNull EmbedBuilder success() {
        return new EmbedBuilder()
                .setColor(Colors.SUCCESS)
                .setTimestamp(OffsetDateTime.now());
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull EmbedBuilder error(@NotNull CPermission permission) {
        return permissioned(permission)
                .setTitle("Error!")
                .setColor(Colors.ERROR)
                .setTimestamp(OffsetDateTime.now());
    }

    public static @NotNull EmbedBuilder error() {
        return new EmbedBuilder()
                .setTitle("Error!")
                .setColor(Colors.ERROR)
                .setTimestamp(OffsetDateTime.now());
    }

    @Contract(value = "-> new", pure = true)
    public static @NotNull EmbedBuilder info() {
        return new EmbedBuilder()
                .setColor(Colors.INFO)
                .setTimestamp(OffsetDateTime.now());
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull EmbedBuilder commandError(@NotNull CPermission permission) {
        return error(permission)
                .setDescription("An unknown error has occurred while executing your command.");
    }
}
