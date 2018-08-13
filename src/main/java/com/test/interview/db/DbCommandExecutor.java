package com.test.interview.db;

import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
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
    private final ExecutorService execSvc;
    private boolean isDone = false;

    public DbCommandExecutor(ExecutorService execSvc, String url, BlockingQueue queue)
    {
        this.execSvc = execSvc;
        this.url = url;
        this.queue = queue;
    }

    @Override
    public void run()
    {
        logger.info("Starting DB SQL Executor");
        while (true)
        {
            try
            {
                Sql sql = queue.take();
                if (PoisonPillSql.MESSAGE.equals(sql.get()))
                {
                    logger.debug("POISON_PILL received. Stopping work on DB.");
                    break;
                }
                execSvc.submit(new DbSqlWorker(url, sql));
            }
            catch (InterruptedException ex)
            {
                logger.error(ex.toString());
            }
        }
        notifyWorkFinished();

    }

    private void notifyWorkFinished()
    {
        execSvc.submit(() ->
        {
            synchronized (DbCommandExecutor.this)
            {
                logger.info("Finished executing tasks. Notifying complete.");
                DbCommandExecutor.this.isDone = true;
                DbCommandExecutor.this.notifyAll();
            }
        });
    }

    @Override
    public void execute(Sql sql)
    {
        new DbSqlWorker(url, sql).run();
    }

    @Override
    public void shutdown()
    {
        try
        {
            execSvc.shutdown();
            execSvc.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.toString());
        }
        logger.info("Shutdown complete...");
    }

    @Override
    public boolean isDone()
    {
        return isDone;
    }
}
