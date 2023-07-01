package me.r5t0neer.mp.vs.api.evt;

import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchEvent;
import me.r5t0neer.mp.vs.api.evt.lst.ListenerPriority;
import me.r5t0neer.mp.vs.api.evt.lst.VPSSEListener;
import me.r5t0neer.mp.vs.api.evt.lst.VPSTEListener;
import me.r5t0neer.mp.vs.api.evt.lst.VSSEListener;
import me.r5t0neer.mp.vs.api.evt.srv.PlayerSpawnTeleportEvent;
import me.r5t0neer.mp.vs.api.evt.srv.VirtualServerStopEvent;
import me.r5t0neer.mp.vs.log.EngineLogger;
import me.r5t0neer.mp.vs.log.Level;
import me.r5t0neer.mp.vs.v.VirtualServer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class AServerEventManager
{
    private final EngineLogger logger;

    private final Map<VirtualServer, ServerListeners> serverListenersMap = new HashMap<>();

    public AServerEventManager(EngineLogger logger) {
        this.logger = logger;
    }

    public void registerVPSTEListener(ListenerPriority priority, VPSTEListener listener, VirtualServer server) {getOrInsert(server).vpsteListeners.put(priority, listener);}
    public void registerVSSEListener(ListenerPriority priority, VSSEListener listener, VirtualServer server) {getOrInsert(server).vsseListeners.put(priority, listener);}

    private ServerListeners getOrInsert(VirtualServer server)
    {
        ServerListeners sl = serverListenersMap.get(server);
        if(sl != null) return sl;
        else
        {
            serverListenersMap.put(server, new ServerListeners());
            return serverListenersMap.get( server );
        }
    }

    public boolean fireEvent(@NotNull IEvent evt, @NotNull VirtualServer server)
    {
        if(evt instanceof PlayerSpawnTeleportEvent e)
        {
            for(VPSTEListener listener : getOrInsert(server).vpsteListeners.values())
                try {
                    listener.onEvent(e);
                }
                catch (Exception exc)
                {
                    logger.log(Level.LISTENER, "Could not pass VPSTE event to plugin "+ listener.pluginName +" on server "+ server.getName());
                    logger.log(Level.LISTENER, exc);
                }
            return e.cancel;
        }

        if(evt instanceof VirtualServerStopEvent e)
        {
            for(VSSEListener listener : getOrInsert(server).vsseListeners.values())
                try {
                    listener.onEvent(e);
                }
                catch (Exception exc)
                {
                    logger.log(Level.LISTENER, "Could not pass VSSE event to plugin "+ listener.pluginName +" on server "+ server.getName());
                    logger.log(Level.LISTENER, exc);
                }
            return false;
        }

        return false;
    }
}
