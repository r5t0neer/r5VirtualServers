package me.r5t0neer.mp.vs.lst;

import me.r5t0neer.mp.vs.v.VirtualServer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;



public class PlayerTeleportListener implements Listener
{
    private final VirtualServerNetwork vsn;
    
    public PlayerTeleportListener(VirtualServerNetwork vsn)
    {
        this.vsn = vsn;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPTEvent(PlayerTeleportEvent evt)
    {
        Location from = evt.getFrom();
        Location to = evt.getTo();
        if(!from.getWorld().equals( to.getWorld() ))
        {
            Player plr = evt.getPlayer();
            VirtualServer server = vsn.plrToVSrvMap.get( plr );
            if(server != null)// not yet fully joined the server network (PlayerJoinListener) if null
            {
                String targetWorldName = to.getWorld().getName();
                if(!server.getWorldsNames().contains( targetWorldName ))
                {
                    if(!vsn.switchPlayer( vsn.plrToVPlrMap.get( plr ), vsn.worldNameToVSrvMap.get( targetWorldName ), false, false ))
                        evt.setCancelled( true );
                }
            }
        }
    }
}
