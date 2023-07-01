package me.r5t0neer.mp.vs.api.evt.lst;

import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchAnyPostEvent;
import org.bukkit.plugin.Plugin;



public abstract class VPSSAPEListener extends EventListener
{
    public VPSSAPEListener(Plugin plugin) {
        super(plugin);
    }

    public abstract void onEvent(VirtualPlayerServerSwitchAnyPostEvent evt) throws Exception;
}
