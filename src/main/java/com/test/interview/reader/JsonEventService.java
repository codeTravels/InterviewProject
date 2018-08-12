package com.test.interview.reader;

import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class reads JSON entries from the file path and generates SQL events
 * that are put on the BlockingQueue.
 *
 * @author Cory
 */
public class JsonEventService implements Runnable

{

    private final Logger logger = LoggerFactory.getLogger(JsonEventService.class);
    private final Map<String, EventEntry> map = new ConcurrentHashMap<>();
    private final String filePath;
    private final BlockingQueue<Sql> sharedQueue;
    private final JsonEventEntryFactory factory = new JsonEventEntryFactory();

    public JsonEventService(String filePath, BlockingQueue<Sql> sharedQueue)
    {
        this.filePath = filePath;
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run()
    {
        logger.info("Starting to read the file " + filePath);
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
            logger.info("Finished reading the file.");
        }
        catch (IOException | InterruptedException ex)
        {
            logger.error(ex.toString());
        }
        finally
        {
            insertPoisonPill();
        }

    }

    private void saveEntry(EventEntry entry) throws InterruptedException
    {
        EventEntry savedEntry = map.putIfAbsent(entry.getId(), entry);

        if (savedEntry != null)
        {
            Event event = new Event(savedEntry, entry);
            sharedQueue.put(new InsertIntoEventTableSql(event));
            map.remove(savedEntry.getId());
        }
    }

    private void insertPoisonPill()
    {
        try
        {
            logger.debug("Inserting Poison Pill");
            sharedQueue.put(new PoisonPillSql());
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.toString());
        }
    }
}
