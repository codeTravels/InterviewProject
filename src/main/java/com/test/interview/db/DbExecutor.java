package com.test.interview.db;

import com.test.interview.db.sql.Sql;

/**
 *
 * @author Cory
 */
public interface DbExecutor
{

    public void execute(Sql sql);
}
