package me.r5t0neer.mp.vs.api.evt.g;

import me.r5t0neer.mp.vs.api.evt.CancelableEvent;
import me.r5t0neer.mp.vs.v.VirtualPlayer;

public class VirtualPlayerCancelableEvent extends CancelableEvent
{
    public final VirtualPlayer vp;

    public VirtualPlayerCancelableEvent(VirtualPlayer vp) {
        this.vp = vp;
    }
}
