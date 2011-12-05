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
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;

public class Log4jUtil
{

    public static void configure( final Level level )
    {
        configure( level, Collections.<String, Level> emptyMap() );
    }

    // TODO: Level map doesn't work (yet)
    public static void configure( final Level level, final Map<String, Level> customLevels )
    {
        final Configurator log4jConfigurator = new Configurator()
        {
            @Override
            public void doConfigure( final URL notUsed, final LoggerRepository repo )
            {
                final ConsoleAppender cAppender = new ConsoleAppender( new SimpleLayout() );
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

                for ( final Map.Entry<String, Level> customEntry : customLevels.entrySet() )
                {
                    final String key = customEntry.getKey();
                    final Logger logger = repo.getLogger( key );
                    logger.setLevel( customEntry.getValue() );
                }
            }
        };

        log4jConfigurator.doConfigure( null, LogManager.getLoggerRepository() );
    }

}
