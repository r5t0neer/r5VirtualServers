package me.r5t0neer.mp.vs.hub_npc;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.r5t0neer.mp.vs.cmd.NPCTeleportsGCE;
import me.r5t0neer.mp.vs.v.VirtualServer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class PacketReader
{
    final NPCTeleportsGCE npct;
    final VirtualServerNetwork vsn;
    final Plugin plugin;
    
    Channel channel;
    public static Map<UUID, Channel> channels = new HashMap<>();
    
    public PacketReader(NPCTeleportsGCE npct, VirtualServerNetwork vsn, Plugin plugin)
    {
        this.npct = npct;
        this.vsn = vsn;
        this.plugin = plugin;
    }
    
    public void inject(Player plr)
    {
        CraftPlayer cplr = (CraftPlayer) plr;
        channel = cplr.getHandle().playerConnection.networkManager.channel;
        channels.put(plr.getUniqueId(), channel);
        
        if(channel.pipeline().get( "PacketInjector" ) != null)
            return;
        
        channel.pipeline().addAfter( "decoder", "PacketInjector", new MessageToMessageDecoder<>()
        {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, Object packet, List<Object> arg) throws Exception
            {
                arg.add( packet );
                readPacket( plr, (Packet<?>) packet );
            }
        } );
    }
    
    public void readPacket(Player plr, Packet<?> packet)
    {
        if(packet.getClass().getSimpleName().equalsIgnoreCase( "PacketPlayInUseEntity" ))
        {
            if(getValue( packet, "action" ).toString().equalsIgnoreCase( "ATTACK" )) return;
            if(getValue( packet, "d" ).toString().equalsIgnoreCase( "OFF_HAND" )) return;
            if(getValue( packet, "action" ).toString().equalsIgnoreCase( "INTERACT_AT" )) return;
            
            int id = (int) getValue( packet, "a" );
            
            if(getValue( packet, "action" ).toString().equalsIgnoreCase( "INTERACT" ))
            {
                for( Map.Entry<Integer, HubNPC> entry : npct.npcList.idToNPC.entrySet())
                {
                    if(entry.getKey() == id)
                    {
                        Bukkit.getServer().getScheduler().runTask( Bukkit.getPluginManager().getPlugins()[0], () -> vsn.switchPlayer( vsn.plrToVPlrMap.get(plr), entry.getValue().vs, false, true ));
                    }
                }
            }
        }
    }
    
    private Object getValue(Object inst, String name)
    {
        Object res = null;
        
        try {
            Field field = inst.getClass().getDeclaredField( name );
            field.setAccessible( true );
            
            res = field.get( inst );
    
            field.setAccessible( false );
            return res;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return res;
    }
}
