package me.r5t0neer.mp.vs.api.evt.lst;

import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchForcedEvent;
import org.bukkit.plugin.Plugin;

public abstract class VPSSFEListener extends EventListener
{
    public VPSSFEListener(Plugin plugin) {
        super(plugin);
    }

    public abstract void onEvent(VirtualPlayerServerSwitchForcedEvent evt) throws Exception;
}
