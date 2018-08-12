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
    private final JsonEventService producer;
    private final ExecutorService execSvc = Executors.newSingleThreadExecutor();

    public App(String filePath)
    {
        String dbUrl = "jdbc:hsqldb:file:hsqldb\\demodb";
        BlockingQueue<Sql> sharedQueue = new LinkedBlockingQueue<>();
        ExecutorService dbExecSvc = Executors.newFixedThreadPool(5);
        this.dbExecutor = new DbCommandExecutor(dbExecSvc, dbUrl, sharedQueue);

        this.producer = new JsonEventService(filePath, sharedQueue);
    }

    void go()
    {
        initializeDatabaseTable();
        execSvc.submit(dbExecutor);

        producer.run();

        shutdown();
    }

    private void initializeDatabaseTable()
    {
        dbExecutor.submit(new CreateEventTableSql());
    }

    private void shutdown()
    {
        try
        {
            dbExecutor.shutdown();
            execSvc.shutdown();
            execSvc.awaitTermination(10, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.toString());
        }
    }
}
