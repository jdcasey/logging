package org.commonjava.util.logging;

import java.text.MessageFormat;
import java.util.IllegalFormatException;

import org.commonjava.util.logging.Logger.LogLevel;

public final class LogEntry
{
    private static long SER_SEED = 0;

    private final long serial;

    private final LogLevel level;

    private final String loggerName;

    private final String format;

    private final Throwable error;

    private final Object[] params;

    private String message;

    public LogEntry( final LogLevel level, final String loggerName, final String format, final Throwable error, final Object... params )
    {
        this.error = error;
        this.params = params;
        serial = SER_SEED++;
        this.level = level;
        this.loggerName = loggerName;
        this.format = format;
    }

    public long getSerial()
    {
        return serial;
    }

    public LogLevel getLevel()
    {
        return level;
    }

    public String getLoggerName()
    {
        return loggerName;
    }

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

    public synchronized String formatMessage()
    {
        if ( message == null )
        {
            String out = format;
            if ( params == null || params.length < 1 )
            {
                return out;
            }

            try
            {
                out = String.format( out, params );
            }
            catch ( final IllegalFormatException e )
            {
                try
                {
                    out = MessageFormat.format( out, params );
                }
                catch ( final IllegalArgumentException e1 )
                {
                    out = format;
                }
            }

            message = out;
        }

        return message;
    }

    @Override
    public String toString()
    {
        return String.format( "[%s] %s", level, formatMessage() );
    }

}
