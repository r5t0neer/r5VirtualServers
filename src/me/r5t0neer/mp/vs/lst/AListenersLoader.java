package me.r5t0neer.mp.vs.lst;

import me.r5t0neer.mp.vs.cmd.NPCTeleportsGCE;
import me.r5t0neer.mp.vs.log.EngineLogger;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;



public class AListenersLoader
{
    public final PlayerCommandPreprocessListener pcpl;
    
    public AListenersLoader(Plugin plugin, VirtualServerNetwork vsn, EngineLogger engineLogger, NPCTeleportsGCE npcTeleportsGCE)
    {
        pcpl = new PlayerCommandPreprocessListener(engineLogger, vsn);
    
        PluginManager pm = Bukkit.getPluginManager();
        
        pm.registerEvents( new PlayerJoinListener( vsn, npcTeleportsGCE ), plugin );
        pm.registerEvents( new PlayerQuitListener( vsn ), plugin );
        pm.registerEvents( pcpl, plugin );
        pm.registerEvents( new PlayerPreLoginListener(vsn), plugin );
        pm.registerEvents( new PlayerTeleportListener( vsn ), plugin );
        pm.registerEvents( new MotdListener(), plugin );
        pm.registerEvents( new SimpleChatListener(), plugin );
    }
}
