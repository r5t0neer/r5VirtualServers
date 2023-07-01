package me.r5t0neer.mp.vs.api.evt;

import me.r5t0neer.mp.vs.api.evt.lst.ListenerPriority;
import me.r5t0neer.mp.vs.api.evt.lst.VPSTEListener;
import me.r5t0neer.mp.vs.api.evt.lst.VSSEListener;

import java.util.SortedMap;
import java.util.TreeMap;

public class ServerListeners
{
    public final SortedMap<ListenerPriority, VPSTEListener> vpsteListeners = new TreeMap<>();
    public final SortedMap<ListenerPriority, VSSEListener> vsseListeners = new TreeMap<>();
}
