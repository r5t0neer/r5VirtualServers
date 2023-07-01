package me.r5t0neer.mp.vs.api.evt.srv;

import me.r5t0neer.mp.vs.api.evt.CancelableEvent;
import me.r5t0neer.mp.vs.v.VirtualServer;

public class VirtualServerCancelableEvent extends CancelableEvent
{
    public final VirtualServer server;

    public VirtualServerCancelableEvent(VirtualServer server) {
        this.server = server;
    }
}
