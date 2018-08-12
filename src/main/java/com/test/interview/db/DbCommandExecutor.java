package com.test.interview.db;

import com.test.interview.db.sql.Sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Cory
 */
public class DbCommandExecutor implements DbExecutor
{

    private final Logger logger = LoggerFactory.getLogger(DbCommandExecutor.class);
    private final String url;

    public DbCommandExecutor(String url)
    {
        this.url = url;
    }

    @Override
    public void execute(Sql sql)
    {
        try (Connection con = DriverManager.getConnection(url, "SA", ""))
        {
            if (con == null)
            {
                String errorMsg = "Failed to make connection to database.";
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }
            execute(con, sql);
        }
        catch (SQLException ex)
        {
            logger.error(ex.toString());
        }
    }

    private void execute(final Connection con, Sql sql) throws SQLException
    {
        try (Statement statement = con.createStatement())
        {
            logger.info("SQL: " + sql.get());
            statement.execute(sql.get());
        }
    }
}
