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

import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;

public class Log4jUtil
{

    public static void configure( final Level level )
    {
        configure( level, "%5p [%l] - %m%n" );
    }

    // TODO: Level map doesn't work (yet)
    public static void configure( final Level level, final String pattern )
    {
        final Configurator log4jConfigurator = new Configurator()
        {
            @Override
            public void doConfigure( final URL notUsed, final LoggerRepository repo )
            {
                final Layout layout = new PatternLayout( pattern );
                final ConsoleAppender cAppender = new ConsoleAppender( layout );
                cAppender.setThreshold( Level.ALL );

                repo.setThreshold( level );
                repo.getRootLogger()
                    .removeAllAppenders();
                repo.getRootLogger()
                    .setLevel( level );
                repo.getRootLogger()
                    .addAppender( cAppender );

                @SuppressWarnings( "unchecked" )
                final List<Logger> loggers = Collections.list( repo.getCurrentLoggers() );

                for ( final Logger logger : loggers )
                {
                    logger.setLevel( level );
                }

                @SuppressWarnings( "unchecked" )
                final List<Category> cats = Collections.list( repo.getCurrentCategories() );
                for ( final Category cat : cats )
                {
                    cat.setLevel( level );
                }
            }
        };

        log4jConfigurator.doConfigure( null, LogManager.getLoggerRepository() );
    }

}
