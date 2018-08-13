package com.test.interview.db;

import com.test.interview.db.sql.Sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
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
        Properties properties = new Properties();
        properties.put("user", "SA");
        properties.put("password", "");
        try (Connection con = DriverManager.getConnection(url, properties);
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
