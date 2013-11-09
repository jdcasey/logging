package org.commonjava.util.logging;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.commonjava.util.logging.Logger.LogLevel;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

public class ListenerSupport
{

    private final ExecutorService executor;

    private final List<LogEntry> buffer = new ArrayList<>();

    private final Set<LogListener> listeners = new LinkedHashSet<>();

    private static ListenerSupport instance;

    public static ListenerSupport get()
    {
        if ( instance == null )
        {
            instance = new ListenerSupport( 1, 500L );
        }

        return instance;
    }

    private ListenerSupport( final int serverCount, final long sleep )
    {
        executor = Executors.newFixedThreadPool( serverCount, new ThreadFactory()
        {
            int counter = 0;

            @Override
            public Thread newThread( final Runnable runnable )
            {
                final Thread t = new Thread( runnable );
                t.setDaemon( true );
                t.setName( "log-listener-" + counter++ );
                t.setPriority( 9 );

                return t;
            }
        } );

        addListener( new LoggerListener() );

        for ( int i = 0; i < serverCount; i++ )
        {
            executor.execute( new LogServer( buffer, listeners, sleep ) );
        }
    }

    public void addListener( final LogListener listener )
    {
        synchronized ( listeners )
        {
            listeners.add( listener );
        }
    }

    public void removeListener( final LogListener listener )
    {
        synchronized ( listeners )
        {
            listeners.remove( listener );
        }
    }

    public void message( final LogLevel level, final String loggerName, final String format, final Object... params )
    {
        final StackTraceElement frame = Thread.currentThread()
                                              .getStackTrace()[3];

        synchronized ( buffer )
        {
            buffer.add( new LogEntry( frame, level, loggerName, format, null, params ) );
            buffer.notifyAll();
        }
    }

    public void message( final LogLevel level, final String loggerName, final String format, final Throwable error, final Object... params )
    {
        final StackTraceElement frame = Thread.currentThread()
                                              .getStackTrace()[3];

        synchronized ( buffer )
        {
            buffer.add( new LogEntry( frame, level, loggerName, format, error, params ) );
            buffer.notifyAll();
        }
    }

    private static final class LogServer
        implements Runnable
    {

        private final List<LogEntry> buffer;

        private final Set<LogListener> listeners;

        private final long sleep;

        public LogServer( final List<LogEntry> buffer, final Set<LogListener> listeners, final long sleep )
        {
            this.buffer = buffer;
            this.listeners = listeners;
            this.sleep = sleep;
        }

        @Override
        public void run()
        {
            while ( true )
            {
                List<LogEntry> pending;
                synchronized ( buffer )
                {
                    while ( buffer.isEmpty() )
                    {
                        buffer.notifyAll();

                        try
                        {
                            buffer.wait( sleep );
                        }
                        catch ( final InterruptedException e )
                        {
                            Thread.currentThread()
                                  .interrupt();
                            return;
                        }
                    }

                    pending = new ArrayList<>( buffer );
                    buffer.clear();
                }

                if ( !pending.isEmpty() )
                {
                    List<LogListener> listeners;
                    synchronized ( this.listeners )
                    {
                        listeners = new ArrayList<>( this.listeners );
                    }

                    for ( final LogListener listener : listeners )
                    {
                        listener.newMessages( pending );
                    }
                }

                synchronized ( buffer )
                {
                    buffer.notifyAll();
                }
            }
        }

    }

    public class LoggerListener
        implements LogListener
    {
        @Override
        public void newMessages( final List<LogEntry> pending )
        {
            final String oldName = Thread.currentThread()
                                         .getName();
            for ( final LogEntry entry : pending )
            {
                Thread.currentThread()
                      .setName( entry.getThreadName() );

                final LocationAwareLogger logger = (LocationAwareLogger) LoggerFactory.getLogger( entry.getLoggerName() );

                logger.log( null, entry.getClassName(), entry.getLevel()
                                                             .slf4jLevel(), entry.formatMessage(), null, entry.getError() );
            }
            Thread.currentThread()
                  .setName( oldName );
        }

    }

    public void sync()
        throws InterruptedException
    {
        synchronized ( buffer )
        {
            while ( !buffer.isEmpty() )
            {
                buffer.notifyAll();
                buffer.wait();
            }
        }
    }

}
