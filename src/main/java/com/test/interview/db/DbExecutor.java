package com.test.interview.db;

import com.test.interview.db.sql.Sql;

/**
 *
 * @author Cory
 */
public interface DbExecutor extends Runnable
{

    /**
     * Executes the SQL command immediately
     *
     * @param sql command to execute
     */
    public void execute(Sql sql);

    /**
     * Stops executing new tasks and closes resources
     */
    public void shutdown();

    /**
     * Checks if the last task is completed
     *
     * @return true if the last task completed
     */
    public boolean isDone();

}
