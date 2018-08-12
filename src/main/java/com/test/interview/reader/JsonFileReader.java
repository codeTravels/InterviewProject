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

    public JSONObject readLine()
    {
        JSONObject obj = null;
        try
        {
            String readLine = reader.readLine();
            if (readLine != null)
            {
                obj = (JSONObject) parser.parse(readLine);
            }
        }
        catch (IOException | ParseException ex)
        {
            logger.error(ex.toString());
            close();
        }
        return obj;
    }

    @Override
    public void close()
    {

        try
        {
            reader.close();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
