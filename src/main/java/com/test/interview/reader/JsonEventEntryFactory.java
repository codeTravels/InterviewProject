package com.test.interview.reader;

import com.test.interview.model.EventEntry;
import java.io.IOException;
import org.json.simple.JSONObject;

/**
 *
 * @author Cory
 */
public class JsonEventEntryFactory
{

    public JsonEventEntryFactory()
    {
    }

    public EventEntry next(JsonFileReader reader) throws IOException
    {
        JSONObject object = reader.readLine();
        if (object != null)
        {
            String id = (String) object.get("id");
            String state = (String) object.get("state");
            long timestamp = (Long) object.get("timestamp");
            String type = (String) object.get("type");
            String host = (String) object.get("host");

            return new EventEntry(id, state, timestamp, type, host);
        }
        return null;
    }
}
