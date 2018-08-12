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
        EventEntry entry1 = new EventEntry("test3", "STARTED", 0);
        EventEntry entry2 = new EventEntry("test3", "FINISHED", 1);
        Event event = new Event(entry1, entry2);
        String imMemoryDbUrl = "jdbc:hsqldb:mem:mymemdb";
        DbSqlWorker worker = new DbSqlWorker(imMemoryDbUrl, new CreateEventTableSql());
        worker.run();
        worker = new DbSqlWorker(imMemoryDbUrl, new InsertIntoEventTableSql(event));
        worker.run();
        try (Connection con = DriverManager.getConnection(imMemoryDbUrl, "SA", "");
                Statement createStatement = con.createStatement();
                ResultSet result = createStatement.executeQuery("SELECT * FROM EVENTS");)

        {

            result.next();

            assertEquals("test3", result.getString(1));
            assertEquals("1", result.getString(2));
            assertEquals("null", result.getString(3));
            assertEquals("null", result.getString(4));
            assertEquals("FALSE", result.getString(5));

        }

    }

}
