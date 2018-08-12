package com.test.interview.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Cory
 */
public class JsonFileReaderTest
{

    public JsonFileReaderTest()
    {
    }

    @Test
    public void testReadLineReturnsNull() throws IOException
    {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(bufferedReader.readLine()).thenReturn(null);
        try (JsonFileReader jsonFileReader = new JsonFileReader(bufferedReader))
        {
            assertNull(jsonFileReader.readLine());
        }
    }

    @Test
    public void testReadLine() throws IOException
    {
        String filePath = "src\\test\\resources\\inputFile.txt";

        try (
                FileInputStream fileInputStream = new FileInputStream(filePath);
                InputStreamReader fileReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                JsonFileReader jsonFileReader = new JsonFileReader(bufferedReader))
        {

            JSONObject actual = jsonFileReader.readLine();
            JSONObject expected = new JSONObject();
            expected.put("id", "scsmbstgra");
            expected.put("state", "STARTED");
            expected.put("timestamp", 1491377495212L);
            expected.put("type", "APPLICATION_LOG");
            expected.put("host", "12345");

            assertEquals(expected, actual);

            actual = jsonFileReader.readLine();
            expected = new JSONObject();
            expected.put("id", "scsmbstgrb");
            expected.put("state", "STARTED");
            expected.put("timestamp", 1491377495213L);

            assertEquals(expected, actual);

            actual = jsonFileReader.readLine();
            expected = new JSONObject();
            expected.put("id", "scsmbstgrc");
            expected.put("state", "FINISHED");
            expected.put("timestamp", 1491377495218L);

            assertEquals(expected, actual);
        }
    }

    /**
     * Test of close method, of class JsonFileReader.
     */
    @Test
    public void testClose() throws Exception
    {
        String filePath = "src\\test\\resources\\inputFile.txt";

        try (
                FileInputStream fileInputStream = new FileInputStream(filePath);
                InputStreamReader fileReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                JsonFileReader jsonFileReader = new JsonFileReader(bufferedReader))
        {
            JSONObject obj = jsonFileReader.readLine();
            assertNotNull(obj);
            jsonFileReader.close();

            obj = jsonFileReader.readLine();
            assertNull(obj);

        }
    }

}
