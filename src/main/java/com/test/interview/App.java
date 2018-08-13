package com.test.interview;

import com.test.interview.db.DbCommandExecutor;
import com.test.interview.db.DbExecutor;
import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.Sql;
import com.test.interview.reader.JsonEventService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Cory
 */
public class App
{

    private final Logger logger = LoggerFactory.getLogger(App.class);
    private final DbExecutor dbExecutor;
    private final JsonEventService jsonEventService;
    private final ExecutorService execSvc = Executors.newSingleThreadExecutor();

    public App(String filePath)
    {

        ExecutorService dbExecSvc = Executors.newFixedThreadPool(10);
        String dbUrl = "jdbc:hsqldb:file:hsqldb\\demodb";
        BlockingQueue<Sql> sharedQueue = new LinkedBlockingQueue<>(1000);
        this.dbExecutor = new DbCommandExecutor(dbExecSvc, dbUrl, sharedQueue);

        this.jsonEventService = new JsonEventService(filePath, sharedQueue);
    }

    void go()
    {
        logger.info("Starting app...");
        initializeDatabaseTable();
        startDbExecutor();

        jsonEventService.run();

        waitForDbExecutor();

        shutdown();
    }

    private void initializeDatabaseTable()
    {
        logger.info("Execute create table SQL");
        dbExecutor.execute(new CreateEventTableSql());
    }

    private void startDbExecutor()
    {
        execSvc.submit(dbExecutor);
    }

    private void waitForDbExecutor()
    {
        while (!dbExecutor.isDone())
        {
            logger.info("DbExecutor is working...");
            synchronized (dbExecutor)
            {
                try
                {
                    dbExecutor.wait(5000);
                }
                catch (InterruptedException ex)
                {
                    logger.error(ex.toString());

                }
            }
        }
    }

    private void shutdown()
    {
        logger.info("Shutting down...");
        try
        {
            dbExecutor.shutdown();
            execSvc.shutdown();
            execSvc.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.toString());
        }
        logger.info("Shutdown complete...");
    }
}
