package com.test.interview;

import com.test.interview.reader.JsonEventService;
import com.test.interview.db.DbCommandExecutor;
import com.test.interview.db.DbExecutor;
import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.Sql;
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
        this.dbExecutor = new DbCommandExecutor(dbUrl, sharedQueue);

        this.producer = new JsonEventService(filePath, sharedQueue);
    }

    void go()
    {
        dbExecutor.submit(new CreateEventTableSql());
        execSvc.submit(dbExecutor);

        producer.run();

        shutdown();
    }

    private void shutdown()
    {
        try
        {
            execSvc.shutdown();
            execSvc.awaitTermination(10, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.toString());
        }
    }
}
