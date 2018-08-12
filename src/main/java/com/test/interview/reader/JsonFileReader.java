package com.test.interview.reader;

import java.io.BufferedReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Cory
 */
public class JsonFileReader implements AutoCloseable
{

    private final Logger logger = LoggerFactory.getLogger(JsonFileReader.class);
    private final BufferedReader reader;
    private final JSONParser parser;

    public JsonFileReader(BufferedReader reader)
    {
        this.parser = new JSONParser();
        this.reader = reader;

    }

    public JSONObject readLine() throws IOException
    {
        try
        {
            String readLine = reader.readLine();
            Object obj = null;
            if (readLine != null)
            {
                obj = parser.parse(readLine);
            }
            return (JSONObject) obj;
        }
        catch (IOException | ParseException ex)
        {
            logger.error(ex.toString());
            close();
        }
        return null;
    }

    @Override
    public void close() throws IOException
    {
        reader.close();
    }
}
