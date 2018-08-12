package com.test.interview;

import com.test.interview.db.DbCommandExecutor;
import com.test.interview.db.DbExecutor;
import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import com.test.interview.reader.JsonEventEntryFactory;
import com.test.interview.reader.JsonFileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
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
    private final Map<String, EventEntry> map = new ConcurrentHashMap<>();
    private final JsonEventEntryFactory factory;
    private final String filePath;
    private final ExecutorService execSvc = Executors.newSingleThreadExecutor();

    public App(String filePath)
    {
        String dbUrl = "jdbc:hsqldb:file:hsqldb\\demodb";
        BlockingQueue<Sql> sharedQueue = new LinkedBlockingQueue<>();
        this.dbExecutor = new DbCommandExecutor(dbUrl, sharedQueue);
        this.factory = new JsonEventEntryFactory();
        this.filePath = filePath;
    }

    void go()
    {
        dbExecutor.submit(new CreateEventTableSql());
        execSvc.submit(dbExecutor);

        try (
                FileInputStream fileInputStream = new FileInputStream(filePath);
                InputStreamReader fileReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                JsonFileReader jsonFileReader = new JsonFileReader(bufferedReader))
        {
            EventEntry next = factory.next(jsonFileReader);
            while (next != null)
            {
                saveEntry(next);
                next = factory.next(jsonFileReader);
            }
            dbExecutor.submit(new PoisonPillSql());
            execSvc.shutdown();
            execSvc.awaitTermination(10, TimeUnit.SECONDS);
        }
        catch (IOException | InterruptedException ex)
        {
            logger.error(ex.toString());
        }

    }

    private void saveEntry(EventEntry entry)
    {
        EventEntry savedEntry = map.putIfAbsent(entry.getId(), entry);

        if (savedEntry != null)
        {
            Event event = new Event(savedEntry, entry);
            dbExecutor.submit(new InsertIntoEventTableSql(event));
            map.remove(savedEntry.getId());
        }
    }
}
