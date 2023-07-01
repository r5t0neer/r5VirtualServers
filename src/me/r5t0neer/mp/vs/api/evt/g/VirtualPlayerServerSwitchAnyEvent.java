package me.r5t0neer.mp.vs.api.evt.g;

import me.r5t0neer.mp.vs.api.evt.IEvent;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServer;



public class VirtualPlayerServerSwitchAnyEvent extends VirtualPlayerCancelableEvent implements IEvent
{
    public final VirtualServer target;
    public final boolean forced;

    public VirtualPlayerServerSwitchAnyEvent(VirtualPlayer vp, VirtualServer target, boolean forced) {
        super(vp);
        this.target = target;
        this.forced = forced;
    }
}
