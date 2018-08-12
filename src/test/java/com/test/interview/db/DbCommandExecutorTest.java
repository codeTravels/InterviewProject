package com.test.interview.db;

import com.test.interview.db.sql.CreateEventTableSql;
import com.test.interview.db.sql.InsertIntoEventTableSql;
import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import com.test.interview.model.Event;
import com.test.interview.model.EventEntry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
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
        EventEntry entry1 = new EventEntry("test3", "STARTED", 0);
        EventEntry entry2 = new EventEntry("test3", "FINISHED", 1);
        Event event = new Event(entry1, entry2);
        String imMemoryDbUrl = "jdbc:hsqldb:mem:mymemdb";
        LinkedBlockingQueue<Sql> queue = new LinkedBlockingQueue<>();
        ExecutorService execSvc = mock(ExecutorService.class);
        DbCommandExecutor executor = new DbCommandExecutor(execSvc, imMemoryDbUrl, queue);
        queue.add(new CreateEventTableSql());
        queue.add(new InsertIntoEventTableSql(event));
        queue.add(new PoisonPillSql());
        executor.run();

        ArgumentCaptor<DbSqlWorker> captor = ArgumentCaptor.forClass(DbSqlWorker.class);
        verify(execSvc, times(2)).submit(captor.capture());
    }
}
