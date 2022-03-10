package main.java.live.astrono.astronobot.database;

import main.java.live.astrono.astronobot.AstronoBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                AstronoBot.config.mysql_url,
                AstronoBot.config.mysql_username,
                AstronoBot.config.mysql_password
        );
    }
}
