package me.r5t0neer.mp.vs.api.evt.lst;

import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchAnyEvent;
import org.bukkit.plugin.Plugin;



public abstract class VPSSAEListener extends EventListener
{
    public VPSSAEListener(Plugin plugin) {
        super(plugin);
    }

    public abstract void onEvent(VirtualPlayerServerSwitchAnyEvent evt) throws Exception;
}
