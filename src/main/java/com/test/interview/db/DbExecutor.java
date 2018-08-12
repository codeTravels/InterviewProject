package com.test.interview.db;

import com.test.interview.db.sql.Sql;

/**
 *
 * @author Cory
 */
public interface DbExecutor extends Runnable
{

    public void execute(Sql sql);

}
