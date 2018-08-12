package com.test.interview.db;

import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Cory
 */
public class DbCommandExecutorTest
{

    @Test
    public void testCreatingAndInsertingIntoInMemoryDB() throws SQLException
    {
        EventEntry entry1 = new EventEntry("test3", "STARTED", 0);
        EventEntry entry2 = new EventEntry("test3", "FINISHED", 1);
        Event event = new Event(entry1, entry2);
        String imMemoryDbUrl = "jdbc:hsqldb:mem:mymemdb";
        LinkedBlockingQueue<Sql> queue = new LinkedBlockingQueue<>();
        DbCommandExecutor executor = new DbCommandExecutor(imMemoryDbUrl, queue);
        queue.add(new CreateEventTableSql());
        queue.add(new InsertIntoEventTableSql(event));
        queue.add(new PoisonPillSql());
        executor.run();
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
