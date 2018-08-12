package com.test.interview;

import com.test.interview.db.DbCommandExecutor;
import com.test.interview.db.DbExecutor;
import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.Sql;
import com.test.interview.reader.JsonEventService;
import java.util.ArrayList;
import java.util.List;
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
    private final List<DbExecutor> dbExecutors;
    private final JsonEventService jsonEventService;
    private final ExecutorService execSvc;

    public App(String filePath)
    {

        dbExecutors = new ArrayList<>();
        int numDbWorkers = 5;
        execSvc = Executors.newFixedThreadPool(numDbWorkers);
        String dbUrl = "jdbc:hsqldb:file:hsqldb\\demodb";
        BlockingQueue<Sql> sharedQueue = new LinkedBlockingQueue<>();
        for (int i = 0; i < numDbWorkers; i++)
        {
            this.dbExecutors.add(new DbCommandExecutor(dbUrl, sharedQueue));
        }

        this.jsonEventService = new JsonEventService(filePath, sharedQueue);
    }

    void go()
    {
        logger.info("Starting app...");
        initializeDatabaseTable();
        startDbExecutors();

        jsonEventService.run();

        shutdown();
    }

    private void initializeDatabaseTable()
    {
        logger.info("Execute create table SQL");
        dbExecutors.get(0).execute(new CreateEventTableSql());
    }

    private void startDbExecutors()
    {
        dbExecutors.forEach((dbExecutor) ->
        {
            execSvc.submit(dbExecutor);
        });
    }

    private void shutdown()
    {
        logger.info("Shutting down...");
        try
        {
            execSvc.shutdown();
            execSvc.awaitTermination(2, TimeUnit.MINUTES);
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.toString());
        }
        logger.info("Shutdown complete...");
    }
}
