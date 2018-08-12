package com.test.interview.db;

import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Cory
 */
public class DbCommandExecutor implements DbExecutor, Runnable
{

    private final Logger logger = LoggerFactory.getLogger(DbCommandExecutor.class);
    private final LinkedBlockingQueue<Sql> queue;

    public DbCommandExecutor()
    {
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void offer(Sql sql)
    {
        queue.offer(sql);
        run();
    }

    @Override
    public void run()
    {
        try (Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testdb", "SA", ""))
//        try (Connection con = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", ""))
        {
            if (con == null)
            {
                logger.error("Failed to make connection to database.");
                throw new RuntimeException("Failed to make connection to database.");
            }
            do
            {

            }
            while (workingOnQueue(con));

        }
        catch (SQLException | InterruptedException ex)
        {
            logger.error(ex.toString());
        }

    }

    private boolean workingOnQueue(Connection con) throws InterruptedException, SQLException
    {
        Sql sql = queue.take();
        if (PoisonPillSql.MESSAGE.equals(sql.get()))
        {
            return false;
        }
        try (Statement statement = con.createStatement())
        {
            logger.info("SQL: " + sql.get());
            statement.execute(sql.get());
        }
        return false;
    }

    public void close()
    {
        queue.clear();
        queue.offer(new PoisonPillSql());
    }

}
