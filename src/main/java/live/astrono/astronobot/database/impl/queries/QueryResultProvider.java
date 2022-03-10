package main.java.live.astrono.astronobot.database.impl.queries;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryResultProvider {
    ResultSet execute(@NotNull Connection connection) throws SQLException;
}
