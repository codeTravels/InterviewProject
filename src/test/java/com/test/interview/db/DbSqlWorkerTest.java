package com.test.interview.db;

import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Cory
 */
public class DbSqlWorkerTest
{

    public DbSqlWorkerTest()
    {
    }

    @Test
    public void testCreatingAndInsertingIntoInMemoryDB() throws SQLException
    {
        // Setup
        EventEntry entry1 = new EventEntry("test", "STARTED", 0);
        EventEntry entry2 = new EventEntry("test", "FINISHED", 1);
        Event event = new Event(entry1, entry2);
        String imMemoryDbUrl = "jdbc:hsqldb:mem:mymemdb";

        // Execute
        DbSqlWorker worker = new DbSqlWorker(imMemoryDbUrl, new CreateEventTableSql());
        worker.run();
        worker = new DbSqlWorker(imMemoryDbUrl, new InsertIntoEventTableSql(event));
        worker.run();

        // Verify
        Properties properties = new Properties();
        properties.put("user", "SA");
        properties.put("password", "");
        try (Connection con = DriverManager.getConnection(imMemoryDbUrl, properties);
                Statement createStatement = con.createStatement();
                ResultSet result = createStatement.executeQuery("SELECT * FROM EVENTS");)

        {

            result.next();

            assertEquals("test", result.getString(1));
            assertEquals("1", result.getString(2));
            assertEquals("null", result.getString(3));
            assertEquals("null", result.getString(4));
            assertEquals("FALSE", result.getString(5));

        }

    }

}
