package com.test.interview.db;

import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Cory
 */
public class DbCommandExecutor implements DbExecutor
{

    private final Logger logger = LoggerFactory.getLogger(DbCommandExecutor.class);
    private final Properties properties;
    private final String url;
    private final BlockingQueue<Sql> queue;

    public DbCommandExecutor(String url, BlockingQueue<Sql> queue)
    {
        properties = new Properties();
        properties.put("user", "SA");
        properties.put("password", "");
        this.url = url;
        this.queue = queue;
    }

    @Override
    public void run()
    {
        logger.info("Starting DB SQL Executor");
        while (true)
        {

            try (Connection con = DriverManager.getConnection(url, properties);
                    Statement statement = con.createStatement();)
            {
                Sql sql = queue.take();
                if (PoisonPillSql.MESSAGE.equals(sql.get()))
                {
                    logger.debug("POISON_PILL received. Stopping work on DB.");
                    queue.put(new PoisonPillSql()); // Putting here for the other instances
                    break;
                }

                logger.trace("SQL: " + sql.get());
                statement.execute(sql.get());
            }
            catch (SQLException | InterruptedException ex)
            {
                logger.error(ex.toString());
            }
        }
    }

    @Override
    public void execute(Sql sql)
    {
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
