package me.r5t0neer.mp.vs.api.evt.lst;

import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchEvent;
import org.bukkit.plugin.Plugin;

public abstract class VPSSEListener extends EventListener
{
    public VPSSEListener(Plugin plugin) {
        super(plugin);
    }

    public abstract void onEvent(VirtualPlayerServerSwitchEvent evt) throws Exception;
}
