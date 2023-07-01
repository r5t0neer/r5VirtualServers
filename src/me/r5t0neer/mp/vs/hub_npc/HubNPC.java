package me.r5t0neer.mp.vs.hub_npc;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.r5t0neer.mp.vs.sql.SQLDatabase;
import me.r5t0neer.mp.vs.sql.entry.SqlHubNPCEntry;
import me.r5t0neer.mp.vs.util.MessageUtils;
import me.r5t0neer.mp.vs.v.VirtualServer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import java.util.UUID;



public class HubNPC
{
    public final int id;
    public final UUID uuid;
    public String name, texture, signature;
    public Location loc;
    public VirtualServer vs;
    
    public EntityPlayer ep;
    
    public HubNPC(String name, Location loc, VirtualServer vs, SQLDatabase db) throws Exception
    {
        this.uuid = UUID.randomUUID();
        this.name = MessageUtils.colorize( name );
        
        db.hubNPC.insertNPC( new SqlHubNPCEntry(
                0,
                uuid.toString(),
                this.name,
                vs.id,
                loc.getWorld().getName(),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getYaw(),
                loc.getPitch(),
                "", ""
            )
        );
        
        SqlHubNPCEntry entry = db.hubNPC.getLastNPC();
        if(entry == null) throw new Exception("Failed retrieving inserted Hub NPC on SQL.");
        
        this.id = entry.id;
        this.loc = loc;
        this.vs = vs;
        
        init();
    }
    
    public HubNPC(SqlHubNPCEntry entry, VirtualServerNetwork vsn)
    {
        this.id = entry.id;
        this.uuid = UUID.fromString( entry.uuid );
        this.name = entry.name;
        this.loc = new Location(
                Bukkit.getWorld(entry.world),
                entry.x, entry.y, entry.z,
                entry.yaw, entry.pitch
        );
        this.vs = vsn.getServer( entry.vsrv_id );
        this.texture = entry.texture;
        this.signature = entry.signature;
        
        init();
    }
    
    private void init()
    {
        MinecraftServer s = ( (CraftServer) Bukkit.getServer() ).getServer();
        WorldServer ws = ( (CraftWorld) this.loc.getWorld() ).getHandle();
        GameProfile gp = new GameProfile( this.uuid, this.name );
        PlayerProfile pp = Bukkit.createProfile( UUID.fromString( this.uuid.toString() ), "test" );
        
    
        if(!this.texture.isEmpty() && !this.signature.isEmpty())
        {
            pp.setProperty( new ProfileProperty( "textures", this.texture, this.signature ) );
            
            /*System.out.println("skin for npc hub "+ this.name);
            System.out.println("texture: "+ this.texture);
            System.out.println("signature: "+ this.signature);*/
            gp.getProperties().put( "textures", new Property( "textures", this.texture, this.signature ) );
        }
        
        this.ep = new EntityPlayer( s, ws, gp, new PlayerInteractManager( ws ) );
        
        //this.ep.getBukkitEntity().getPlayer().setPlayerListName( "" );
        this.ep.setLocation( this.loc.getX(),this.loc.getY(),this.loc.getZ(),this.loc.getYaw(),this.loc.getPitch() );
    }
}
