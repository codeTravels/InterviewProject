package com.test.interview;

/**
 *
 * @author Cory
 */
public class MainTest
{

    public MainTest()
    {
    }

//    @Test
    public void testMainTwo()
    {
        Main.main(new String[]
        {
            "--file", "src\\main\\resources\\inputFile.txt"
        });
    }

}
