package me.r5t0neer.mp.vs.v;

import org.bukkit.entity.Player;



public class VirtualPlayer
{
    public final int id;
    public final Player plr;
    VirtualServer server;
    
    
    public VirtualPlayer(int id, Player plr, VirtualServer server)
    {
        this.id = id;
        this.plr = plr;
        this.server = server;
    }
    
    public VirtualServer getServer() {return server;}
}
