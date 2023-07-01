package me.r5t0neer.mp.vs.api.evt.g;

import me.r5t0neer.mp.vs.api.evt.IEvent;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServer;

// fired on virtual server stop
public class VirtualPlayerServerSwitchForcedEvent extends VirtualPlayerEvent implements IEvent
{
    public final VirtualServer target;

    public VirtualPlayerServerSwitchForcedEvent(VirtualPlayer vp, VirtualServer target) {
        super(vp);
        this.target = target;
    }
}
