package com.test.interview;

import com.test.interview.db.DbExecutor;
import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import com.test.interview.reader.JsonEventEntryFactory;
import com.test.interview.reader.JsonFileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        dbExecutor.execute(new CreateEventTableSql());
    }

    void go()
    {
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
            dbExecutor.execute(new InsertIntoEventTableSql(event));
            map.remove(savedEntry.getId());
        }
    }
}
