package me.r5t0neer.mp.vs.api.cmd;

import me.r5t0neer.mp.vs.lst.AListenersLoader;
import me.r5t0neer.mp.vs.lst.PlayerCommandPreprocessListener;



public class GlobalCommandManager
{
    private final PlayerCommandPreprocessListener listener;
    
    public GlobalCommandManager(AListenersLoader loader)
    {
        this.listener = loader.pcpl;
    }
    
    public void setPlayerExecutor(String[] aliases, GlobalCommandExecutor executor)
    {
        listener.setExecutor( aliases, executor );
    }
}
