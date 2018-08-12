package com.test.interview.db;

import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import com.test.interview.model.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Cory
 */
public class DbCommandExecutorTest
{

    public DbCommandExecutorTest()
    {
    }

    /**
     * Test of offer method, of class DbCommandExecutor.
     */
    @Test
    public void testOffer()
    {
    }

    /**
     * Test of run method, of class DbCommandExecutor.
     */
//    @Test
    public void testRun() throws SQLException
    {
        EventEntry entry1 = new EventEntry("test3", State.STARTED.toString(), 0);
        EventEntry entry2 = new EventEntry("test3", State.FINISHED.toString(), 1);
        Event event = new Event(entry1, entry2);
        DbCommandExecutor executor = new DbCommandExecutor();
        executor.offer(new CreateEventTableSql());
        executor.offer(new InsertIntoEventTableSql(event));
        executor.offer(new PoisonPillSql());
        executor.run();
        try (Connection con = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", ""))
        {

            ResultSet result = con.createStatement().executeQuery("SELECT * FROM EVENTS");
            result.next();

            assertEquals("test3", result.getString(1));
            assertEquals("1", result.getString(2));
            assertEquals("null", result.getString(3));
            assertEquals("null", result.getString(4));
            assertEquals("FALSE", result.getString(5));

        }

    }

    /**
     * Test of close method, of class DbCommandExecutor.
     */
    @Test
    public void testClose()
    {
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    @Ignore
    public void testMain() throws SQLException
    {
        try
        {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        }
        catch (Exception e)
        {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }
        try (Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testdb", "SA", ""))
//        try (Connection con = DriverManager.getConnection("jdbc:hsqldb:file:C:\\Users\\Cory\\Downloads\\hsqldb-2.4.1\\hsqldb-2.4.1\\hsqldb\\hsqldb\\demodb", "SA", ""))
        {
            if (con == null)
            {
                Assert.fail("Problem with creating connection");
            }
            Statement statement = con.createStatement();
//            statement.execute("DROP TABLE EVENTS");
            statement.execute(new CreateEventTableSql().get());
            statement.execute("INSERT INTO Events (id, duration, type, host,alert) VALUES ('test',8,'','',false)");
        }
    }

}
