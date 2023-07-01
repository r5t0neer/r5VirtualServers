package me.r5t0neer.mp.vs.api.evt.lst;

import me.r5t0neer.mp.vs.api.evt.srv.VirtualServerStopEvent;
import org.bukkit.plugin.Plugin;

public abstract class VSSEListener extends EventListener
{
    public VSSEListener(Plugin plugin) {
        super(plugin);
    }

    public abstract void onEvent(VirtualServerStopEvent evt) throws Exception;
}
