package com.test.interview.model;

import java.util.Objects;

/**
 *
 * @author Cory
 */
public final class EventEntry
{

    private final String id;
    private final String state;
    private volatile long timestamp;
    private final String type;
    private final String host;

    public EventEntry(String id, String state, long timestamp)
    {
        this(id, state, timestamp, null, null);
    }

    public EventEntry(String id, String state, long timestamp, String type)
    {
        this(id, state, timestamp, type, null);
    }

    public EventEntry(String id, String state, long timestamp, String type, String host)
    {
        Objects.requireNonNull(id);
        Objects.requireNonNull(state);
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = type;
        this.host = host;
    }

    public String getId()
    {
        return id;
    }

    public String getState()
    {
        return state;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public String getType()
    {
        return type;
    }

    public String getHost()
    {
        return host;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("id: ").append(id).append(" ");
        builder.append("state: ").append(state).append(" ");
        builder.append("timestamp: ").append(timestamp).append(" ");
        builder.append("type: ").append(type).append(" ");
        builder.append("host: ").append(host);
        return builder.toString();

    }

}
