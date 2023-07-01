package me.r5t0neer.mp.vs.api;

import me.r5t0neer.mp.vs.api.cmd.GlobalCommandManager;
import me.r5t0neer.mp.vs.api.evt.AGlobalEventManager;
import me.r5t0neer.mp.vs.api.evt.AServerEventManager;
import me.r5t0neer.mp.vs.log.EngineLogger;
import me.r5t0neer.mp.vs.lst.AListenersLoader;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.jetbrains.annotations.Nullable;

public class VirtualApi {
    private static VirtualApi INSTANCE = null;

    public final AGlobalEventManager gem;
    public final GlobalCommandManager gcm;
    public final AServerEventManager sem;
    public final VirtualServerNetwork vsn;

    public VirtualApi(EngineLogger logger, AListenersLoader all, VirtualServerNetwork vsn)
    {
        gem = new AGlobalEventManager(logger);
        gcm = new GlobalCommandManager(all);
        sem = new AServerEventManager(logger);
        this.vsn = vsn;

        if(INSTANCE != null)
            return;

        INSTANCE = this;
    }

    public static @Nullable VirtualApi inst() {return INSTANCE;}
}
