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
public class DbSqlWorker implements Runnable
{

    private final Logger logger = LoggerFactory.getLogger(DbSqlWorker.class);
    private final String url;
    private final Sql sql;

    public DbSqlWorker(String url, Sql sql)
    {
        this.url = url;
        this.sql = sql;
    }

    @Override
    public void run()
    {
        try (Connection con = DriverManager.getConnection(url, "SA", "");
                Statement statement = con.createStatement();)
        {
            logger.trace("SQL: " + sql.get());
            statement.execute(sql.get());
        }
        catch (SQLException ex)
        {
            logger.error(ex.toString());
        }
    }

}
