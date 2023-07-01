package me.r5t0neer.mp.vs.lst;

import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;



public class PlayerQuitListener implements Listener
{
    private final VirtualServerNetwork vsn;
    
    public PlayerQuitListener(VirtualServerNetwork vsn)
    {
        this.vsn = vsn;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent evt)
    {
        vsn.onPlayerQuit( evt.getPlayer() );
    }
}
