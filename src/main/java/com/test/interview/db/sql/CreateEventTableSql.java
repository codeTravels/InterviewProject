package com.test.interview.db.sql;

/**
 *
 * @author Cory
 */
public class CreateEventTableSql implements Sql
{

    @Override
    public String get()
    {
        return "CREATE TABLE IF NOT EXISTS Events ( id varchar(100) NOT NULL, duration int NOT NULL, type varchar(100), host varchar(100), alert BOOLEAN, PRIMARY KEY (id))";
    }
}
