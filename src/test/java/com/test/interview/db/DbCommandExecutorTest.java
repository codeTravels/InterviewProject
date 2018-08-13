package com.test.interview.db;

import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Cory
 */
public class DbCommandExecutorTest
{

    @Test
    public void testDbSqlWorkersAreSubmittedToExecutorService()
    {
        // Setup
        EventEntry entry1 = new EventEntry("test", "STARTED", 0);
        EventEntry entry2 = new EventEntry("test", "FINISHED", 1);
        Event event = new Event(entry1, entry2);
        String imMemoryDbUrl = "jdbc:hsqldb:mem:mymemdb";
        LinkedBlockingQueue<Sql> queue = new LinkedBlockingQueue<>();
        ExecutorService execSvc = mock(ExecutorService.class);
        DbCommandExecutor executor = new DbCommandExecutor(execSvc, imMemoryDbUrl, queue);
        queue.add(new CreateEventTableSql());
        queue.add(new InsertIntoEventTableSql(event));
        queue.add(new PoisonPillSql());

        // Execute
        executor.run();

        // Verify
        ArgumentCaptor<DbSqlWorker> captor = ArgumentCaptor.forClass(DbSqlWorker.class);
        verify(execSvc, times(3)).submit(captor.capture()); //2 times for SQL commands, 1 time for notify complete
    }

    @Test(timeout = 5000)
    public void testDbExecutorNotifiesOnTaskCompletion() throws InterruptedException
    {
        String imMemoryDbUrl = "jdbc:hsqldb:mem:mymemdb";
        LinkedBlockingQueue<Sql> queue = new LinkedBlockingQueue<>();
        ExecutorService execSvc = Executors.newSingleThreadExecutor();
        DbCommandExecutor executor = new DbCommandExecutor(execSvc, imMemoryDbUrl, queue);
        queue.add(new PoisonPillSql());

        // Execute
//        executor.run();
        Executors.newSingleThreadScheduledExecutor()
                .schedule(executor, 50, TimeUnit.MILLISECONDS);

        // Verify
        assertFalse(executor.isDone());
        while (!executor.isDone())
        {
            synchronized (executor)
            {
                executor.wait(10);
            }
        }
        executor.shutdown();
    }

    @Test
    public void testDbExecutorShutdown() throws InterruptedException
    {
        // Setup
        String imMemoryDbUrl = "";
        LinkedBlockingQueue<Sql> queue = new LinkedBlockingQueue<>();
        ExecutorService execSvc = mock(ExecutorService.class);
        DbCommandExecutor executor = new DbCommandExecutor(execSvc, imMemoryDbUrl, queue);

        // Execute
        executor.shutdown();

        // Verify
        verify(execSvc).shutdown();
        verify(execSvc).awaitTermination(5, TimeUnit.SECONDS);
    }
}
