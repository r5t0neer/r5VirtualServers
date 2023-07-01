package me.r5t0neer.mp.vs.api.evt.g;

import me.r5t0neer.mp.vs.api.evt.IEvent;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServer;

public class VirtualPlayerServerSwitchEvent extends VirtualPlayerCancelableEvent implements IEvent
{
    public final VirtualServer target;

    public VirtualPlayerServerSwitchEvent(VirtualPlayer vp, VirtualServer target) {
        super(vp);
        this.target = target;
    }
}
