package com.test.interview.reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

/**
 *
 * @author Cory
 */
public class FileReaderTest
{

    public FileReaderTest()
    {
    }

    @Test
    public void testSomeMethod() throws ParseException
    {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse("{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\",\n"
                + "\"host\":\"12345\", \"timestamp\":1491377495212}");

        JSONObject jsonObject = (JSONObject) obj;
        String id = (String) jsonObject.get("id");
        System.out.println("id = " + id);

    }

}
