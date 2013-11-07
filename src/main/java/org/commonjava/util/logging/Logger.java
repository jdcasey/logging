/*******************************************************************************
 * Copyright 2011 John Casey
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.commonjava.util.logging;

import org.apache.log4j.Level;

public final class Logger
{

    public enum LogLevel
    {
        TRACE( Level.TRACE ), DEBUG( Level.DEBUG ), INFO( Level.INFO ), ERROR( Level.ERROR ), WARN( Level.WARN );

        private Level lvl;

        LogLevel( final Level lvl )
        {
            this.lvl = lvl;
        }

        public Level log4jLevel()
        {
            return lvl;
        }
    }

    private static ListenerSupport listenerSupport = ListenerSupport.get();

    private final String loggerName;

    public Logger( final Class<?> clazz )
    {
        this.loggerName = clazz.getName();
    }

    public Logger( final String name )
    {
        this.loggerName = name;
    }

    public Logger( final org.slf4j.Logger logger )
    {
        this.loggerName = logger.getName();
    }

    public static void registerListener( final LogListener listener )
    {
        listenerSupport.addListener( listener );
    }

    public static void deregisterListener( final LogListener listener )
    {
        listenerSupport.removeListener( listener );
    }

    public Logger debug( final String format, final Object... params )
    {
        listenerSupport.message( LogLevel.DEBUG, loggerName, format, null, params );
        return this;
    }

    public Logger debug( final String format, final Throwable error, final Object... params )
    {
        listenerSupport.message( LogLevel.DEBUG, loggerName, format, error, params );
        return this;
    }

    public Logger error( final String format, final Object... params )
    {
        listenerSupport.message( LogLevel.ERROR, loggerName, format, null, params );
        return this;
    }

    public Logger error( final String format, final Throwable error, final Object... params )
    {
        listenerSupport.message( LogLevel.ERROR, loggerName, format, error, params );
        return this;
    }

    public Logger info( final String format, final Object... params )
    {
        listenerSupport.message( LogLevel.INFO, loggerName, format, null, params );
        return this;
    }

    public Logger info( final String format, final Throwable error, final Object... params )
    {
        listenerSupport.message( LogLevel.INFO, loggerName, format, error, params );
        return this;
    }

    public Logger trace( final String format, final Object... params )
    {
        listenerSupport.message( LogLevel.TRACE, loggerName, format, null, params );
        return this;
    }

    public Logger trace( final String format, final Throwable error, final Object... params )
    {
        listenerSupport.message( LogLevel.TRACE, loggerName, format, error, params );
        return this;
    }

    public Logger warn( final String format, final Object... params )
    {
        listenerSupport.message( LogLevel.WARN, loggerName, format, null, params );
        return this;
    }

    public Logger warn( final String format, final Throwable error, final Object... params )
    {
        listenerSupport.message( LogLevel.WARN, loggerName, format, error, params );
        return this;
    }

}
