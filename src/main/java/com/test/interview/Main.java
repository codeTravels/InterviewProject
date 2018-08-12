package com.test.interview;

import com.test.interview.db.DbCommandExecutor;
import java.util.Arrays;
import java.util.List;

public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String dbUrl = "jdbc:hsqldb:hsql://localhost/testdb";
        App app = new App(new DbCommandExecutor(dbUrl), parseFilePath(args));
        app.go();
    }

    private static String parseFilePath(String[] args)
    {
        List<String> argList = Arrays.asList(args);
        int fileIndex = argList.indexOf("--file") + 1;
        if (fileIndex > 0)
        {
            return argList.get(fileIndex);
        }
        return "";

    }

}
