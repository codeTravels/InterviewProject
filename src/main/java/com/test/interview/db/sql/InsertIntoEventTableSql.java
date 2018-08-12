package com.test.interview.db.sql;

import com.test.interview.model.Event;

/**
 *
 * @author Cory
 */
public class InsertIntoEventTableSql implements Sql
{

    private final Event event;

    public InsertIntoEventTableSql(Event event)
    {
        this.event = event;
    }

    @Override
    public String get()
    {
//        return "INSERT INTO Events (id, duration, type, host,alert) VALUES ('test',8,'','',false)";
        StringBuilder builder = new StringBuilder("INSERT INTO Events VALUES (");
        builder.append("'").append(event.getId()).append("', ");
        builder.append(event.getDuration()).append(", ");
        builder.append("'").append(event.getType()).append("', ");
        builder.append("'").append(event.getHost()).append("', ");
        builder.append(alert());
        builder.append(")");
        return builder.toString();
    }

    private boolean alert()
    {
        return event.getDuration() > 4;
    }
}
