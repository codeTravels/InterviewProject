package com.test.interview.db;

import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    private final String url;
    private final BlockingQueue<Sql> queue;

    public DbCommandExecutor(String url, BlockingQueue queue)
    {
        this.url = url;
        this.queue = queue;
    }

    @Override
    public void run()
    {
        while (true)
        {

            try (Connection con = DriverManager.getConnection(url, "SA", "");
                    Statement statement = con.createStatement();)
            {
                Sql sql = queue.take();
                if (PoisonPillSql.MESSAGE.equals(sql.get()))
                {
                    logger.debug("POISON_PILL received. Stopping work on DB.");
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
    public void submit(Sql sql)
    {
        try
        {
            queue.put(sql);
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.toString());
        }
    }
}
