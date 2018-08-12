package com.test.interview;

import org.junit.Test;

/**
 *
 * @author Cory
 */
public class MainTest
{

    @Test
    @org.junit.Ignore
    public void testMain()
    {
        Main.main(new String[]
        {
            "--file", "src\\test\\resources\\inputFile.txt"
        });
    }

}
