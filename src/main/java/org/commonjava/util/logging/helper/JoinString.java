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
package org.commonjava.util.logging.helper;

import java.util.Arrays;
import java.util.Collection;

public class JoinString
{

    private final Collection<?> items;

    private final String joint;

    public JoinString( final String joint, final Collection<?> items )
    {
        this.items = items;
        this.joint = joint;
    }

    public JoinString( final String joint, final Object... items )
    {
        this.items = Arrays.asList( items );
        this.joint = joint;
    }

    @Override
    public String toString()
    {
        if ( items == null || items.isEmpty() )
        {
            return "-NONE-";
        }
        else
        {
            final StringBuilder sb = new StringBuilder();
            for ( final Object item : items )
            {
                if ( sb.length() < 1 )
                {
                    sb.append( joint );
                }
                sb.append( item );
            }

            return sb.toString();
        }
    }

}