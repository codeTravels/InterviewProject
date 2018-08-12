package com.test.interview.reader;

import com.test.interview.model.EventEntry;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Cory
 */
public class JsonEventEntryFactoryTest
{

    public JsonEventEntryFactoryTest()
    {
    }

    @Test
    public void testNext_nullReturned() throws IOException
    {
        JsonFileReader jsonFileReader = mock(JsonFileReader.class);
        when(jsonFileReader.readLine()).thenReturn(null);
        JsonEventEntryFactory eventEntryFactory = new JsonEventEntryFactory();
        EventEntry entry = eventEntryFactory.next(jsonFileReader);
        assertNull(entry);
    }

    @Test
    public void testNextWithAllJsonData() throws IOException
    {
        String id = "scsmbstgra";
        String state = "STARTED";
        long timestamp = 1491377495212L;
        String type = "APPLICATION_LOG";
        String host = "12345";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("state", state);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("type", type);
        jsonObject.put("host", host);
        JsonFileReader jsonFileReader = mock(JsonFileReader.class);
        when(jsonFileReader.readLine()).thenReturn(jsonObject);
        JsonEventEntryFactory eventEntryFactory = new JsonEventEntryFactory();

        EventEntry entry = eventEntryFactory.next(jsonFileReader);

        EventEntry expected = new EventEntry(id, state, timestamp, type, host);
        assertExpectedEntryEqualsActualEntry(expected, entry);
    }

    @Test
    public void testNextWithRequiredJsonData() throws IOException
    {
        String id = "scsmbstgra";
        String state = "STARTED";
        long timestamp = 1491377495212L;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("state", state);
        jsonObject.put("timestamp", timestamp);
        JsonFileReader jsonFileReader = mock(JsonFileReader.class);
        when(jsonFileReader.readLine()).thenReturn(jsonObject);
        JsonEventEntryFactory eventEntryFactory = new JsonEventEntryFactory();

        EventEntry entry = eventEntryFactory.next(jsonFileReader);

        EventEntry expected = new EventEntry(id, state, timestamp);
        assertExpectedEntryEqualsActualEntry(expected, entry);
    }

    /**
     * Test of next method, of class JsonEventEntryFactory.
     */
    @Test
    public void testNext() throws IOException
    {
        String filePath = "src\\test\\resources\\inputFile.txt";

        try (
                FileInputStream fileInputStream = new FileInputStream(filePath);
                InputStreamReader fileReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                JsonFileReader jsonFileReader = new JsonFileReader(bufferedReader))
        {
            JsonEventEntryFactory eventEntryFactory = new JsonEventEntryFactory();
            EventEntry entry = eventEntryFactory.next(jsonFileReader);
            String id = "scsmbstgra";
            String state = "STARTED";
            long timestamp = 1491377495212L;
            String type = "APPLICATION_LOG";
            String host = "12345";
            EventEntry expected = new EventEntry(id, state, timestamp, type, host);
            assertExpectedEntryEqualsActualEntry(expected, entry);

            entry = eventEntryFactory.next(jsonFileReader);
            id = "scsmbstgrb";
            state = "STARTED";
            timestamp = 1491377495213L;
            type = null;
            host = null;
            expected = new EventEntry(id, state, timestamp, type, host);
            assertExpectedEntryEqualsActualEntry(expected, entry);

            entry = eventEntryFactory.next(jsonFileReader);
            id = "scsmbstgrc";
            state = "FINISHED";
            timestamp = 1491377495218L;
            type = null;
            host = null;
            expected = new EventEntry(id, state, timestamp, type, host);
            assertExpectedEntryEqualsActualEntry(expected, entry);
        }
    }

    private void assertExpectedEntryEqualsActualEntry(EventEntry expected,
                                                      EventEntry actual)
    {

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getTimestamp(), actual.getTimestamp());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getHost(), actual.getHost());
    }
}
