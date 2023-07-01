package me.r5t0neer.mp.vs.log;

import java.util.logging.Logger;



public class EngineLogger
{
    private final Logger logger;
    
    EngineLogger()
    {
        logger = Logger.getLogger( "VS | EngineLogger" );
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
