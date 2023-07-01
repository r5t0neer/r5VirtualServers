package me.r5t0neer.mp.vs.api.evt.g;

import me.r5t0neer.mp.vs.api.evt.IEvent;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServer;



public class VirtualPlayerServerSwitchAnyPostEvent extends VirtualPlayerCancelableEvent implements IEvent
{
    public final VirtualServer source;
    public final VirtualServer target;
    public final boolean forced;

    public VirtualPlayerServerSwitchAnyPostEvent(VirtualPlayer vp, VirtualServer source, VirtualServer target, boolean forced) {
        super(vp);
        this.source = source;
        this.target = target;
        this.forced = forced;
    }
}
