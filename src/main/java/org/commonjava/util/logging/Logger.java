/*******************************************************************************
 * Copyright (C) 2011 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Copyright 2011 Red Hat, Inc.
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
 */

package org.commonjava.util.logging;

import java.text.MessageFormat;
import java.util.IllegalFormatException;

import org.slf4j.LoggerFactory;

public final class Logger
{

    private final org.slf4j.Logger logger;

    public Logger( final Class<?> clazz )
    {
        logger = LoggerFactory.getLogger( clazz.getName() );
    }

    public Logger( final String name )
    {
        logger = LoggerFactory.getLogger( name );
    }

    public Logger( final org.slf4j.Logger logger )
    {
        this.logger = logger;
    }

    public org.slf4j.Logger getLogger()
    {
        return logger;
    }

    public Logger debug( final String format, final Object...params )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( format( format, params ) );
        }

        return this;
    }

    public Logger debug( final String format, final Throwable error, final Object...params )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( format( format, params ), error );
        }

        return this;
    }

    public Logger error( final String format, final Object...params )
    {
        if ( logger.isErrorEnabled() )
        {
            logger.error( format( format, params ) );
        }

        return this;
    }

    public Logger error( final String format, final Throwable error, final Object...params )
    {
        if ( logger.isErrorEnabled() )
        {
            logger.error( format( format, params ), error );
        }

        return this;
    }

    public Logger info( final String format, final Object...params )
    {
        if ( logger.isInfoEnabled() )
        {
            logger.info( format( format, params ) );
        }

        return this;
    }

    public Logger info( final String format, final Throwable error, final Object...params )
    {
        if ( logger.isInfoEnabled() )
        {
            logger.info( format( format, params ), error );
        }

        return this;
    }

    public Logger trace( final String format, final Object...params )
    {
        if ( logger.isTraceEnabled() )
        {
            logger.trace( format( format, params ) );
        }

        return this;
    }

    public Logger trace( final String format, final Throwable error, final Object...params )
    {
        if ( logger.isTraceEnabled() )
        {
            logger.trace( format( format, params ), error );
        }

        return this;
    }

    public Logger warn( final String format, final Object...params )
    {
        if ( logger.isWarnEnabled() )
        {
            logger.warn( format( format, params ) );
        }

        return this;
    }

    public Logger warn( final String format, final Throwable error, final Object...params )
    {
        if ( logger.isWarnEnabled() )
        {
            logger.warn( format( format, params ), error );
        }

        return this;
    }

    private String format( final String format, final Object[] params )
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

        return out;
    }

}
