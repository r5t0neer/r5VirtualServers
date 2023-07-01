package me.r5t0neer.mp.vs.lst;

import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerPreLoginListener implements Listener
{
    private final VirtualServerNetwork vsn;

    public PlayerPreLoginListener(VirtualServerNetwork vsn) {
        this.vsn = vsn;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public synchronized void onPPLEvent(AsyncPlayerPreLoginEvent evt)
    {
        if(!vsn.defaultServer.isActive())
        {
            evt.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("Default server is inactive"));
        }
        else if(vsn.defaultServer.isFull())
        {
            // todo check bypass permissions
            evt.disallow( AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text( "Server is full, try again later" ) );
        }
    }
}
