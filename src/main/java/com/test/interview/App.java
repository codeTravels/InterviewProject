package com.test.interview;

import com.test.interview.db.DbExecutor;
import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import com.test.interview.reader.JsonEventEntryFactory;
import com.test.interview.reader.JsonFileReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    public App(DbExecutor dbExecutor, String filePath)
    {
        this.dbExecutor = dbExecutor;
        this.factory = new JsonEventEntryFactory();
        this.filePath = filePath;
        dbExecutor.offer(new CreateEventTableSql());
    }

    void go()
    {
        try (
                FileReader fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                JsonFileReader jsonFileReader = new JsonFileReader(bufferedReader))
        {
            EventEntry next = factory.next(jsonFileReader);
            while (next != null)
            {
                saveEntry(next);
                next = factory.next(jsonFileReader);
            }
        }
        catch (IOException ex)
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
            dbExecutor.offer(new InsertIntoEventTableSql(event));
            map.remove(savedEntry.getId());
        }
    }
}
