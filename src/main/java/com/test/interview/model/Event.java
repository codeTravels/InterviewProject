package com.test.interview.model;

/**
 *
 * @author Cory
 */
public class Event
{

    private final String id;
    private volatile long duration;
    private final String type;
    private final String host;

    public Event(EventEntry entry1, EventEntry entry2)
    {
        //TODO maybe add some checks?

        this.id = entry1.getId();
        this.type = entry1.getType();
        this.host = entry1.getHost();

        if ("STARTED".equals(entry1.getState()))
        {
            this.duration = entry2.getTimestamp() - entry1.getTimestamp();
        }
        else
        {
            this.duration = entry1.getTimestamp() - entry2.getTimestamp();
        }
    }

    public String getId()
    {
        return id;
    }

    public long getDuration()
    {
        return duration;
    }

    public String getType()
    {
        return type;
    }

    public String getHost()
    {
        return host;
    }
}
