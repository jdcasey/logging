package org.commonjava.util.logging;

import java.util.List;

public interface LogListener
{

    void newMessages( List<LogEntry> pending );

}
