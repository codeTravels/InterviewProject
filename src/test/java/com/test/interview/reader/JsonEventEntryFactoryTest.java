/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.interview.reader;

import com.test.interview.model.EventEntry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Cory
 */
public class JsonEventEntryFactoryTest
{

    public JsonEventEntryFactoryTest()
    {
    }

    /**
     * Test of next method, of class JsonEventEntryFactory.
     */
    @Test
    public void testNext() throws IOException
    {
        String filePath = "src\\test\\resources\\inputFile.txt";

        try (
                FileReader fileReader = new FileReader(filePath);
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
//            while (entry != null)
//            {
//                entry = eventEntryFactory.next();
//                System.out.println("entry = " + entry);
//
//            }
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
