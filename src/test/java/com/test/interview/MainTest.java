package com.test.interview;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 *
 * @author Cory
 */
public class MainTest
{

    @Test
    @org.junit.Ignore
    public void testMain() throws IOException
    {
        File file = new File("hsqldb");
        if (file.exists())
        {
            System.out.println("deleting hsqldb directory");
            FileUtils.deleteDirectory(file);
        }
        Main.main(new String[]
        {
            "--file", "src\\test\\resources\\inputFile.txt"
        });
    }

}
