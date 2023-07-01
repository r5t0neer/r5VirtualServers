package me.r5t0neer.mp.vs.lst;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.r5t0neer.mp.vs.util.MessageUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;



public class SimpleChatListener implements Listener
{
    @EventHandler
    public void onAChatPostEvt(AsyncPlayerChatEvent evt)
    {
        evt.setMessage( MessageUtils.colorize( evt.getMessage() ) );
    }
}
