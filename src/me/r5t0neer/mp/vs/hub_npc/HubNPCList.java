package me.r5t0neer.mp.vs.hub_npc;

import me.r5t0neer.mp.vs.sql.SQLDatabase;
import me.r5t0neer.mp.vs.sql.entry.SqlHubNPCEntry;
import me.r5t0neer.mp.vs.v.VirtualServer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;



public class HubNPCList
{
    private final SQLDatabase db;
    
    public final Map<Integer, HubNPC> idToNPC = new HashMap<>();
    
    public HubNPCList(SQLDatabase db, VirtualServerNetwork vsn) throws Exception
    {
        this.db = db;
        
        for( SqlHubNPCEntry entry : db.hubNPC.listNPCs() )
        {
            HubNPC hubNPC = new HubNPC( entry, vsn );
            idToNPC.put( hubNPC.ep.getId(), hubNPC );
        }
    }
    
    public HubNPC createNPC(Location loc, String name, VirtualServer vs) throws Exception
    {
        HubNPC npc = new HubNPC( name, loc, vs, db );
        idToNPC.put( npc.ep.getId(), npc );
        return npc;
    }
    
    public void removeNPC(int id)// throws Exception
    {
        idToNPC.remove( id );
        //db.hubNPC.removeNPC( id );
    }
}
