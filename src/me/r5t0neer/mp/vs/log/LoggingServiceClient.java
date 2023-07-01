package me.r5t0neer.mp.vs.log;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;



public class LoggingServiceClient
{
    private final int port;
    private final EngineLogger engineLogger;
    private final List<ConsoleCommandListener> listeners = new ArrayList<>();

    public LoggingServiceClient(int port)
    {
        this.port = port;
        this.engineLogger = new EngineLogger();
    }
    
    // todo receive remote console commands

    public EngineLogger getEngineLogger() {return engineLogger;}

    public @Nullable ServerLogger getServerLogger(String srvName)
    {
        return new ServerLogger(srvName);
    }
    
    public void registerConsoleCommandListener(ConsoleCommandListener listener)
    {
        listeners.add( listener );
    }
}
