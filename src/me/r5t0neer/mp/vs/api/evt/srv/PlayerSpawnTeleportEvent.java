package me.r5t0neer.mp.vs.api.evt.srv;

import me.r5t0neer.mp.vs.api.evt.IEvent;
import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerCancelableEvent;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServer;
import org.bukkit.entity.Player;

public class PlayerSpawnTeleportEvent extends VirtualServerCancelableEvent implements IEvent
{
    public final Player plr;

    public PlayerSpawnTeleportEvent(VirtualServer server, Player plr) {
        super(server);
        this.plr = plr;
    }
}
