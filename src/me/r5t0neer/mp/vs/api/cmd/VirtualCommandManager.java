package me.r5t0neer.mp.vs.api.cmd;

import me.r5t0neer.mp.vs.lst.AListenersLoader;
import me.r5t0neer.mp.vs.lst.PlayerCommandPreprocessListener;
import me.r5t0neer.mp.vs.v.VirtualServer;

public class VirtualCommandManager
{
    private final PlayerCommandPreprocessListener pcpl;

    public VirtualCommandManager(AListenersLoader all) {
        pcpl = all.pcpl;
    }

    public void setPlayerExecutor(String[] aliases, VirtualCommandExecutor executor, VirtualServer server)
    {
        pcpl.setVirtualExecutor(aliases, executor, server);
    }
}
