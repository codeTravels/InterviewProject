package com.test.interview;

import com.test.interview.db.DbExecutor;
import com.test.interview.db.sql.Sql;
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
public class AppTest
{

    public AppTest()
    {
    }

    @Test
    public void testCreateTableOnStart()
    {
        DbExecutor executor = mock(DbExecutor.class);
        App app = new App(executor, "src\\test\\resources\\eventPair.txt");
        ArgumentCaptor<Sql> captor = ArgumentCaptor.forClass(Sql.class);
        verify(executor).execute(captor.capture());
        String insertSql = captor.getValue().get();
        System.out.println(insertSql);

        assertEquals("CREATE TABLE IF NOT EXISTS Events ( id varchar(100) NOT NULL, duration int NOT NULL, type varchar(100), host varchar(100), alert BOOLEAN, PRIMARY KEY (id))", insertSql);
    }

    @Test
    public void testEventEntry()
    {
        DbExecutor executor = mock(DbExecutor.class);
        App app = new App(executor, "src\\test\\resources\\eventPair.txt");
        app.go();
        ArgumentCaptor<Sql> captor = ArgumentCaptor.forClass(Sql.class);
        verify(executor, times(2)).execute(captor.capture());
        String insertSql = captor.getAllValues().get(1).get();
        System.out.println(insertSql);

        assertEquals("INSERT INTO Events VALUES ('scsmbstgra', 5, 'APPLICATION_LOG', '12345', true)", insertSql);
    }

    @Test
    public void testEventEntrySwapped()
    {
        DbExecutor executor = mock(DbExecutor.class);
        App app = new App(executor, "src\\test\\resources\\eventPair_swapped.txt");
        app.go();
        ArgumentCaptor<Sql> captor = ArgumentCaptor.forClass(Sql.class);
        verify(executor, times(2)).execute(captor.capture());
        String insertSql = captor.getAllValues().get(1).get();
        System.out.println(insertSql);

        assertEquals("INSERT INTO Events VALUES ('scsmbstgra', 5, 'APPLICATION_LOG', '12345', true)", insertSql);
    }

}
