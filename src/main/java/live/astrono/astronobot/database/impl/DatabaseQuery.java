package main.java.live.astrono.astronobot.database.impl;

import main.java.live.astrono.astronobot.database.DatabaseManager;
import main.java.live.astrono.astronobot.database.impl.queries.QueryResultProvider;
import main.java.live.astrono.astronobot.database.impl.result.DatabaseFuture;
import main.java.live.astrono.astronobot.database.impl.result.DatabaseResult;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseQuery {
    
    private QueryResultProvider queryProvider = null;
    
    public DatabaseQuery query(@NotNull QueryResultProvider queryProvider) {
        this.queryProvider = queryProvider;
        return this;
    }
    
    public DatabaseFuture compile() {
        try {
            Connection connection = DatabaseManager.getConnection();
            return new DatabaseFuture(connection, new DatabaseResult(queryProvider.execute(connection)));
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to execute SQL !" + e.getMessage());
        }
    }
}