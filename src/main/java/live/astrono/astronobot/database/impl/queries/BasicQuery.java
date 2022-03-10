package main.java.live.astrono.astronobot.database.impl.queries;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicQuery implements QueryResultProvider {
    
    private final String query;
    private final PreparedStatementProvider provider;
    
    public BasicQuery(@NotNull @Language("SQL") String query) {
        this.query = query;
        this.provider = null;
    }
    
    public BasicQuery(@NotNull @Language("SQL") String query, @Nullable PreparedStatementProvider provider) {
        this.query = query;
        this.provider = provider;
    }
    
    @Override
    public ResultSet execute(@NotNull Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setQueryTimeout(10);
        
        if (provider != null) {
            provider.prepare(statement);
        }
        
        if (statement.execute()) {
            ResultSet set = statement.getResultSet();
            return set;
        } else {
            statement.close();
            connection.close();
            return null;
        }
    }
    
    public interface PreparedStatementProvider {
        void prepare(@NotNull PreparedStatement statement) throws SQLException;
    }
}