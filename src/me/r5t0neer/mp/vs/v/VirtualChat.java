package me.r5t0neer.mp.vs.v;

import me.r5t0neer.mp.vs.log.Level;



public class VirtualChat
{
    private VirtualServer server;
    
    VirtualChat(VirtualServer server)
    {
        this.server = server;
    }
    
    public void broadcast(String message)
    {
        server.getLogger().log( Level.CHAT, message );
        
        for(VirtualPlayer plr : server.players)
            plr.plr.sendMessage( message );
    }
}
