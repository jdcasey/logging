package org.commonjava.util.logging;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.log4j.Level;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoggerTest
{

    public class CountListener
        implements LogListener
    {
        private int count = 0;

        @Override
        public void newMessages( final List<LogEntry> pending )
        {
            count += pending.size();
        }
    }

    @BeforeClass
    public static void setupLogging()
    {
        Log4jUtil.configure( Level.DEBUG );
    }

    @Test
    public void logAFewSimpleMessages()
        throws InterruptedException
    {
        final Logger logger = new Logger( getClass() );
        final CountListener cl = new CountListener();
        Logger.registerListener( cl );

        int i = 0;
        logger.info( "Checking this out" + i++, new RuntimeException() );
        logger.info( "Checking this out" + i++, new RuntimeException() );
        logger.info( "Checking this out" + i++, new RuntimeException() );
        logger.info( "Checking this out" + i++, new RuntimeException() );
        logger.info( "Checking this out" + i++, new RuntimeException() );
        logger.info( "Checking this out" + i++, new RuntimeException() );
        logger.info( "Checking this out" + i++, new RuntimeException() );
        logger.info( "Checking this out" + i++, new RuntimeException() );

        Thread.sleep( 100 );

        ListenerSupport.get()
                       .sync();

        assertThat( cl.count, equalTo( i ) );
    }

}
