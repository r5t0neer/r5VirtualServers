package me.r5t0neer.mp.vs;

import me.r5t0neer.mp.vs.api.VirtualApi;
import me.r5t0neer.mp.vs.api.evt.lst.ListenerPriority;
import me.r5t0neer.mp.vs.cmd.*;
import me.r5t0neer.mp.vs.log.EngineLogger;
import me.r5t0neer.mp.vs.log.LoggingServiceClient;
import me.r5t0neer.mp.vs.lst.AListenersLoader;
import me.r5t0neer.mp.vs.lst.HubJoinListener;
import me.r5t0neer.mp.vs.sql.SQLDatabase;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;



public class VirtualServersPlugin extends JavaPlugin
{
    /*
    todo vanilla commands (restrict players in commands - kill, tp, ...), trusted players, server thread id onLoad()
         peak avoidance with tp scheduling,
         but it should go more abstract & advanced -> monitor current throughput and take decisions on it
         1. cost of teleport - bandwidth peak per player, actually wider graph of peak to minimize delays
     */
    
    @Override
    public void onEnable()
    {
        super.onEnable();
        
        try
        {
            LoggingServiceClient ls = new LoggingServiceClient(5567);
            EngineLogger engineLogger = ls.getEngineLogger();

            SQLDatabase db = new SQLDatabase();
            VirtualServerNetwork vsn = new VirtualServerNetwork( this, ls, db );
            
            NPCTeleportsGCE npcTeleportsGCE = new NPCTeleportsGCE(this, vsn, db );
            
            AListenersLoader all = new AListenersLoader( this, vsn, engineLogger, npcTeleportsGCE );
            VirtualApi api = new VirtualApi(engineLogger, all, vsn);
    
            api.gem.registerVPSSAPEListener( ListenerPriority.MONITOR, new HubJoinListener( this, npcTeleportsGCE ) );
            
            api.gcm.setPlayerExecutor( new String[]{"hub"}, new HubGCE(vsn,this) );
            api.gcm.setPlayerExecutor(new String[]{"spawn","lobby"},new SpawnGCE(vsn,this));
            api.gcm.setPlayerExecutor(new String[]{"setspawn"},new SetSpawnGCE(vsn,this));
            api.gcm.setPlayerExecutor(new String[]{"npct"},npcTeleportsGCE);
            api.gcm.setPlayerExecutor(new String[]{"list","online"},new ListGCE( this, vsn ) );
            
            vsn.fixPlayersPresence();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }
}
