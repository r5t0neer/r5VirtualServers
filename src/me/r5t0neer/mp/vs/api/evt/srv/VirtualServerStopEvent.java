package me.r5t0neer.mp.vs.api.evt.srv;

import me.r5t0neer.mp.vs.api.evt.IEvent;
import me.r5t0neer.mp.vs.v.VirtualServer;

public class VirtualServerStopEvent implements IEvent
{
    public final VirtualServer server;

    public VirtualServerStopEvent(VirtualServer server) {
        this.server = server;
    }
}
