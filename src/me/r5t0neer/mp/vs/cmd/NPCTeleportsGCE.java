package me.r5t0neer.mp.vs.cmd;

import com.mojang.authlib.GameProfile;
import me.r5t0neer.mp.vs.api.cmd.GlobalCommandExecutor;
import me.r5t0neer.mp.vs.hub_npc.HubNPC;
import me.r5t0neer.mp.vs.hub_npc.HubNPCList;
import me.r5t0neer.mp.vs.hub_npc.PacketReader;
import me.r5t0neer.mp.vs.sql.SQLDatabase;
import me.r5t0neer.mp.vs.util.CommandUtil;
import me.r5t0neer.mp.vs.util.MessageUtils;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;



public class NPCTeleportsGCE extends GlobalCommandExecutor
{
    private final VirtualServerNetwork vsn;
    private final PacketReader pr;
    private final SQLDatabase db;
    public final HubNPCList npcList;
    
    public NPCTeleportsGCE(Plugin plugin, VirtualServerNetwork vsn, SQLDatabase db) throws Exception
    {
        super( plugin );
        this.vsn = vsn;
        this.db = db;
        this.pr = new PacketReader( this, vsn, plugin );
        this.npcList = new HubNPCList( db, vsn );
    }
    
    @Override
    public void handle(CommandSender sender, String argsString) throws Exception
    {
        if(sender.isOp())
        {
            List<String> args = CommandUtil.getSmartArgs( argsString );
            
            if(args.size() > 0)
            {
                if(args.get( 0 ).equalsIgnoreCase( "create" ))
                {
                    if(args.size() != 3)
                    {
                        sender.sendMessage( "Missing arguments" );
                    }
                    else
                    {
                        Player p = (Player) sender;
    
                        VirtualPlayer vp = vsn.plrToVPlrMap.get( p );
                        
                        if(vp.getServer().isDefault())
                        {
                            VirtualServer target = vsn.getServerIgnoreCase( args.get( 2 ) );
                            if(target != null)
                            {
                                HubNPC npc = npcList.createNPC( p.getLocation(), args.get( 1 ), target );
                                onNPCCreated( npc.ep );
                                sender.sendMessage( MessageUtils.colorize( "&aCreated NPC "+ npc.name +"&a for server &8"+ npc.vs.getName() ) );
                            }
                            else sender.sendMessage( "Server does not exist." );
                        }
                        else sender.sendMessage( "Not a Hub." );
                    }
                }
                else if(args.get( 0 ).equalsIgnoreCase( "remove" ))
                {
                    VirtualServer vs = vsn.getServerIgnoreCase( args.get( 1 ) );
                    if(vs != null)
                    {
                        List<Integer> ids = new ArrayList<>();
                        npcList.idToNPC.forEach( (id, npc) -> {
                            if(npc.vs.id == vs.id)
                                ids.add( id );
                        } );
                        ids.forEach( (id) -> {
                            onNPCRemoved( npcList.idToNPC.get( id ) );
                            npcList.removeNPC( id );
                        } );
                        
                        db.hubNPC.removeNPCServer( vs.id );
                        sender.sendMessage( MessageUtils.colorize( "&cRemoved all NPCs for server " + vs.getName() ) );
                    }
                    else sender.sendMessage( "No such server" );
                }
                else
                {
                    sender.sendMessage( "/npct create '<colored name>' <server_name>\n" +
                                                "/npct remove <server_name>");
                }
            }
            else
            {
                sender.sendMessage( "/npct create '<colored name>' <server_name>\n" +
                                            "/npct remove <server_name>");
            }
        }
    }
    
    public void onPlayerJoin(Player plr)
    {
        PlayerConnection p_conn = (( CraftPlayer ) plr).getHandle().playerConnection;
        
        for( HubNPC npc : npcList.idToNPC.values() )
        {
            p_conn.sendPacket( new PacketPlayOutPlayerInfo( PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc.ep) );
            p_conn.sendPacket( new PacketPlayOutNamedEntitySpawn(npc.ep) );
            p_conn.sendPacket( new PacketPlayOutEntityHeadRotation(npc.ep, (byte)(npc.loc.getYaw() * 256 / 360)) );
            p_conn.sendPacket( new PacketPlayOutPlayerInfo( PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.ep) );
        }
        
        pr.inject( plr );
    }
    
    public void onNPCCreated(EntityPlayer npc)
    {
        PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo( PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
        PacketPlayOutNamedEntitySpawn ppones =  new PacketPlayOutNamedEntitySpawn(npc);
        PacketPlayOutEntityHeadRotation ppoehr = new PacketPlayOutEntityHeadRotation(npc, (byte)(npc.yaw * 256 / 360));
        PacketPlayOutPlayerInfo ppopi2 = new PacketPlayOutPlayerInfo( PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc);
        
        vsn.defaultServer.forEachPlayer( (vplr) -> {
            PlayerConnection p_conn = (( CraftPlayer ) vplr.plr).getHandle().playerConnection;
            p_conn.sendPacket( ppopi );
            p_conn.sendPacket( ppones );
            p_conn.sendPacket( ppoehr );
            p_conn.sendPacket( ppopi2 );
        } );
    }
    
    public void onNPCRemoved(HubNPC npc)
    {
        PacketPlayOutEntityDestroy ppoed = new PacketPlayOutEntityDestroy( npc.ep.getId() );
        PacketPlayOutPlayerInfo ppopi2 = new PacketPlayOutPlayerInfo( PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.ep );
        vsn.defaultServer.forEachPlayer( (vplr) -> {
            PlayerConnection p_conn = ( (CraftPlayer) vplr.plr ).getHandle().playerConnection;
            p_conn.sendPacket( ppoed );
            p_conn.sendPacket( ppopi2 );
        } );
    }
    
    public void onPlayerQuit(Player plr)
    {
        PlayerConnection p_conn = ( (CraftPlayer) plr ).getHandle().playerConnection;
        List<Integer> ids = new ArrayList<>();
        for(HubNPC npc : npcList.idToNPC.values())
        {
            ids.add( npc.ep.getId() );
            p_conn.sendPacket( new PacketPlayOutPlayerInfo( PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.ep) );
        }
        
        int[] ret = new int[ids.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = ids.get( i );
        }
        
        p_conn.sendPacket( new PacketPlayOutEntityDestroy( ret ) );
    }
}
