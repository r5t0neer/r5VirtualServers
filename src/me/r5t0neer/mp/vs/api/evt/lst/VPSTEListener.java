package me.r5t0neer.mp.vs.api.evt.lst;

import me.r5t0neer.mp.vs.api.evt.srv.PlayerSpawnTeleportEvent;
import org.bukkit.plugin.Plugin;

public abstract class VPSTEListener extends EventListener
{
    public VPSTEListener(Plugin plugin) {
        super(plugin);
    }

    public abstract void onEvent(PlayerSpawnTeleportEvent evt) throws Exception;
}
