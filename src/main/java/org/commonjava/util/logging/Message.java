package org.commonjava.util.logging;

public class Message
{

    enum Sev
    {
        TRACE, DEBUG, INFO, WARN, ERROR;
    }

    private Sev level;

    private String format;

    private Throwable error;

    private Object[] params;

    public String getFormat()
    {
        return format;
    }

    public Throwable getError()
    {
        return error;
    }

    public Object[] getParams()
    {
        return params;
    }

    public Message withFormat( final String format )
    {
        this.format = format;
        return this;
    }

    public Message withError( final Throwable error )
    {
        this.error = error;
        return this;
    }

    public Message withParams( final Object... params )
    {
        this.params = params;
        return this;
    }

    public Sev getSeverity()
    {
        return level;
    }

    public Message withSeverity( final Sev level )
    {
        this.level = level;
        return this;
    }

}
