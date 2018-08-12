package com.test.interview.reader;

import com.test.interview.db.sql.PoisonPillSql;
import com.test.interview.db.sql.Sql;
import java.util.concurrent.BlockingQueue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Cory
 */
public class JsonEventServiceTest
{

    @Test
    public void testEventEntry() throws InterruptedException
    {
        BlockingQueue queue = mock(BlockingQueue.class);
        JsonEventService sut = new JsonEventService("src\\test\\resources\\eventPair.txt", queue);

        sut.run();

        ArgumentCaptor<Sql> captor = ArgumentCaptor.forClass(Sql.class);
        verify(queue, times(2)).put(captor.capture());
        String insertSql = captor.getAllValues().get(0).get();
        assertEquals("INSERT INTO Events VALUES ('scsmbstgra', 5, 'APPLICATION_LOG', '12345', true)", insertSql);
        insertSql = captor.getAllValues().get(1).get();
        assertEquals(PoisonPillSql.MESSAGE, insertSql);
    }

    @Test
    public void testEventEntrySwapped() throws InterruptedException
    {
        BlockingQueue queue = mock(BlockingQueue.class);
        JsonEventService sut = new JsonEventService("src\\test\\resources\\eventPair_swapped.txt", queue);

        sut.run();

        ArgumentCaptor<Sql> captor = ArgumentCaptor.forClass(Sql.class);
        verify(queue, times(2)).put(captor.capture());
        String insertSql = captor.getAllValues().get(0).get();
        assertEquals("INSERT INTO Events VALUES ('scsmbstgra', 5, 'APPLICATION_LOG', '12345', true)", insertSql);
        insertSql = captor.getAllValues().get(1).get();
        assertEquals(PoisonPillSql.MESSAGE, insertSql);
    }

}
