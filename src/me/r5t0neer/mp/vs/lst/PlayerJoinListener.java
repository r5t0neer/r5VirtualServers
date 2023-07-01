package me.r5t0neer.mp.vs.lst;

import me.r5t0neer.mp.vs.cmd.NPCTeleportsGCE;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;



public class PlayerJoinListener implements Listener
{
    private final VirtualServerNetwork vsn;
    private final NPCTeleportsGCE npcTeleportsGCE;
    
    public PlayerJoinListener(VirtualServerNetwork vsn, NPCTeleportsGCE npcTeleportsGCE)
    {
        this.vsn = vsn;
        this.npcTeleportsGCE = npcTeleportsGCE;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(PlayerJoinEvent evt)
    {
        evt.setJoinMessage( null );
        
        try {
            vsn.onPlayerJoin( evt.getPlayer() );
            npcTeleportsGCE.onPlayerJoin( evt.getPlayer() );
        }
        catch(Exception e)
        {
            evt.getPlayer().kick( Component.text( "VSN Join Error" ), PlayerKickEvent.Cause.PLUGIN );
            e.printStackTrace();
        }
    }
}
