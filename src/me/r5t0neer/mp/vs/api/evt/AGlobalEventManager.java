package me.r5t0neer.mp.vs.api.evt;

import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchAnyEvent;
import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchAnyPostEvent;
import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchEvent;
import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchForcedEvent;
import me.r5t0neer.mp.vs.api.evt.lst.*;
import me.r5t0neer.mp.vs.api.evt.srv.PlayerSpawnTeleportEvent;
import me.r5t0neer.mp.vs.log.EngineLogger;
import me.r5t0neer.mp.vs.log.Level;
import org.jetbrains.annotations.NotNull;

import java.util.SortedMap;
import java.util.TreeMap;

public class AGlobalEventManager
{
    private final EngineLogger logger;

    private final SortedMap<ListenerPriority, VPSSEListener> vpsseListeners = new TreeMap<>();
    private final SortedMap<ListenerPriority, VPSSFEListener> vpssfeListeners = new TreeMap<>();
    private final SortedMap<ListenerPriority, VPSSAEListener> vpssaeListeners = new TreeMap<>();
    private final SortedMap<ListenerPriority, VPSSAPEListener> vpssapeListeners = new TreeMap<>();

    public AGlobalEventManager(EngineLogger logger) {
        this.logger = logger;
    }

    public void registerVPSSEListener(ListenerPriority priority, VPSSEListener listener) {vpsseListeners.put(priority, listener);}
    public void registerVPSSFEListener(ListenerPriority priority, VPSSFEListener listener) {vpssfeListeners.put(priority, listener);}
    public void registerVPSSAEListener(ListenerPriority priority, VPSSAEListener listener) {vpssaeListeners.put(priority, listener);}
    public void registerVPSSAPEListener(ListenerPriority priority, VPSSAPEListener listener) {vpssapeListeners.put(priority, listener);}

    public boolean fireEvent(@NotNull IEvent evt)
    {
        if(evt instanceof VirtualPlayerServerSwitchEvent e)
        {
            for(VPSSEListener listener : vpsseListeners.values())
                try {
                    listener.onEvent(e);
                }
                catch (Exception exc)
                {
                    logger.log(Level.LISTENER, "Could not pass VPSSE event to plugin "+ listener.pluginName);
                    logger.log(Level.LISTENER, exc);
                }
            return e.cancel;
        }

        if(evt instanceof VirtualPlayerServerSwitchForcedEvent e)
        {
            for(VPSSFEListener listener : vpssfeListeners.values())
                try {
                    listener.onEvent(e);
                }
                catch (Exception exc)
                {
                    logger.log(Level.LISTENER, "Could not pass VPSSFE event to plugin "+ listener.pluginName);
                    logger.log(Level.LISTENER, exc);
                }
            return false;
        }
    
        if(evt instanceof VirtualPlayerServerSwitchAnyEvent e)
        {
            for(VPSSAEListener listener : vpssaeListeners.values())
                try {
                    listener.onEvent(e);
                }
                catch (Exception exc)
                {
                    logger.log(Level.LISTENER, "Could not pass VPSSAE event to plugin "+ listener.pluginName);
                    logger.log(Level.LISTENER, exc);
                }
            return false;
        }
    
        if(evt instanceof VirtualPlayerServerSwitchAnyPostEvent e)
        {
            for(VPSSAPEListener listener : vpssapeListeners.values())
                try {
                    listener.onEvent(e);
                }
                catch (Exception exc)
                {
                    logger.log(Level.LISTENER, "Could not pass VPSSAPE event to plugin "+ listener.pluginName);
                    logger.log(Level.LISTENER, exc);
                }
            return false;
        }

        return false;
    }
}
