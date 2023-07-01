package me.r5t0neer.mp.vs.log;

import java.util.logging.Logger;

public class ServerLogger
{
    private final Logger logger;

    ServerLogger(String srvName)
    {
        logger = Logger.getLogger( "VS | "+ srvName );
    }

    public void log(Level level, String msg)
    {
        logger.log( Level.toJLevel( level ), msg );
    }

    public void log(Level level, Exception e)
    {
        logger.log( Level.toJLevel( level ), ExceptionUtil.convertStackTraceToString( e ) );
    }
}
