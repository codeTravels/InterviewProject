package com.test.interview.db.sql;

/**
 *
 * @author Cory
 */
public class PoisonPillSql implements Sql
{

    public static final String MESSAGE = "POISON_PILL";

    @Override
    public String get()
    {
        return MESSAGE;
    }
}
