package me.r5t0neer.mp.vs.v;

import me.r5t0neer.mp.vs.api.VirtualApi;
import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchAnyEvent;
import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchAnyPostEvent;
import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchEvent;
import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchForcedEvent;
import me.r5t0neer.mp.vs.log.Level;
import me.r5t0neer.mp.vs.log.LoggingServiceClient;
import me.r5t0neer.mp.vs.sql.SQLDatabase;
import me.r5t0neer.mp.vs.sql.entry.SqlPlayerEntry;
import me.r5t0neer.mp.vs.sql.entry.SqlVirtualPlayerEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class VirtualServerNetwork
{
    private final Plugin plugin;
    private final SQLDatabase db;
    
    public final List<VirtualServer> servers;
    public @NotNull VirtualServer defaultServer;
    
    public Map<Player, VirtualPlayer> plrToVPlrMap = new HashMap<>();
    public Map<Player, VirtualServer> plrToVSrvMap = new HashMap<>();
    public Map<String, VirtualServer> worldNameToVSrvMap = new HashMap<>();
    
    public VirtualServerNetwork(Plugin plugin, LoggingServiceClient ls, SQLDatabase db) throws Exception
    {
        this.plugin = plugin;
        this.db = db;
        this.servers = new ArrayList<>();
        this.defaultServer = null;
        
        File dir = plugin.getDataFolder();
        if(!dir.exists())
            if(!dir.mkdirs())
                throw new IOException("Could not create plugin folder "+ dir);
        
        File serversDir = new File( dir, "servers" );
        if(!serversDir.exists())
            throw new Exception("Missing servers");
        
        List<Path> dirs = Files.list(serversDir.toPath()).toList();
        for(Path p : dirs)
        {
            try
            {
                VirtualServer vs = new VirtualServer( p, db, ls );
                if(vs.isDefault())
                    defaultServer = vs;
                servers.add( vs );
                
                for(String wName : vs.getWorldsNames())
                    worldNameToVSrvMap.put( wName, vs );
            }
            catch(Exception e)
            {
                ls.getEngineLogger().log( Level.ERROR, e );
            }
        }
        
        if(servers.isEmpty()) throw new Exception("No servers configured");
        if(defaultServer == null) throw new Exception("No default server selected");
        
        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run()
            {
                List<Player> plrs = new ArrayList<>();
                plrs.addAll( plrToVPlrMap.keySet() );
                
                for(Player plr : plrs)
                    onPlayerQuit( plr );
            }
        } );
    }
    
    public void onPlayerJoin(Player plr) throws Exception
    {
        // active check is performed on PreLogin event, so no extra actions (performance)

        VirtualPlayer vp = toVirtualPlayer( plr, defaultServer );
        
        if(!worldNameToVSrvMap.get( plr.getWorld().getName() ).equals( defaultServer ))
            plr.teleport( defaultServer.getSpawn() );
        
        defaultServer.chat.broadcast( plr.getDisplayName() +" joined the server" );
        defaultServer.players.add( vp );
        
        fixPlayerPresence(plr, vp, defaultServer);
    }
    
    public void onPlayerQuit(Player plr)
    {
        plrToVSrvMap.remove(plr);
        VirtualPlayer vp = plrToVPlrMap.remove( plr );
        vp.server.players.remove( vp );
        vp.server.chat.broadcast( plr.getDisplayName() + " left the server" );
        savePlayerLocations( vp );
        vp.plr.teleport( defaultServer.getSpawn() );
    }
    
    public boolean switchPlayer(VirtualPlayer vp, VirtualServer target, boolean force, boolean performTeleport)
    {
        return switchPlayer( vp,target,force,performTeleport,false );
    }
    
    public boolean switchPlayer(VirtualPlayer vp, VirtualServer target, boolean force, boolean performTeleport, boolean spawn)
    {
        if(vp.server.getName().equals(target.getName()))
        {
            vp.plr.sendMessage("You are already on this server.");
            return false;
        }
    
        if(!target.isActive())
        {
            vp.plr.sendMessage("Server is inactive.");
            return false;
        }
        
        // todo check for permissions
        if(target.players.size() >= target.getSlots())
        {
            vp.plr.sendMessage( "Server is full." );
            return false;
        }

        if(!force) {
            if (VirtualApi.inst().gem.fireEvent(new VirtualPlayerServerSwitchEvent(vp, target)))
                return false;
        }
        else VirtualApi.inst().gem.fireEvent(new VirtualPlayerServerSwitchForcedEvent(vp, target));
        
        if(VirtualApi.inst().gem.fireEvent( new VirtualPlayerServerSwitchAnyEvent( vp, target, force ) ) && !force)
            return false;

        Location quit = null;
        if(performTeleport && !spawn)
        {
            try {
                quit = db.virtualPlayer.getQuitLocation( vp, target );
            }
            catch(Exception e)
            {
                vp.server.getLogger().log( Level.WARN, "Failed to get quit location for player "+ vp.plr.getName() );
                vp.server.getLogger().log( Level.WARN, e );
            }
        }
        
        vp.server.removePlayer( vp );
        vp.server.chat.broadcast( vp.plr.getDisplayName() +" gone to "+ target.getName() );
        savePlayerLocations( vp );
        if(performTeleport)
        {
            if(quit != null) vp.plr.teleport( quit );
            else vp.plr.teleport( target.getSpawn() );
        }
        VirtualServer source = vp.server;
        vp.server = target;
        
        fixPlayerPresence( vp.plr, vp, target );
    
        target.chat.broadcast( vp.plr.getDisplayName() +" joined the server" );
        target.players.add( vp );
    
        VirtualApi.inst().gem.fireEvent( new VirtualPlayerServerSwitchAnyPostEvent( vp, source, target, force ) );
        
        return true;
    }
    
    public void fixPlayersPresence() throws Exception
    {
        for(Player plr : Bukkit.getOnlinePlayers())
        {
            VirtualServer server = worldNameToVSrvMap.get( plr.getWorld().getName() );
            
            fixPlayerPresence(
                    plr,
                    toVirtualPlayer( plr, server ),
                    server
            );
        }
    }
    
    public @Nullable VirtualServer getServerIgnoreCase(String serverName)
    {
        for(VirtualServer vs : servers)
            if(vs.getName().equalsIgnoreCase( serverName ))
                return vs;
        
        return null;
    }
    
    public @Nullable VirtualServer getServer(int id)
    {
        for(VirtualServer vs : servers)
            if(vs.id == id)
                return vs;
        
        return null;
    }
    
    private void fixPlayerPresence(Player plr, VirtualPlayer vp, VirtualServer target)
    {
        plrToVPlrMap.put( plr, vp );
        plrToVSrvMap.put(plr,target);
    
        for(VirtualServer server : servers)
            if(!server.getName().equals( target.getName() ))
            {
                for(VirtualPlayer another : server.players)
                {
                    plr.hidePlayer( plugin, another.plr );
                    another.plr.hidePlayer( plugin, plr );
                }
            }
    
        for(VirtualPlayer another : target.players)
        {
            plr.showPlayer( plugin, another.plr );
            another.plr.showPlayer( plugin, plr );
        }
        
        try {
            plr.setBedSpawnLocation( db.virtualPlayer.getBedSpawnLocation( vp, target ) );
        }
        catch(Exception e)
        {
            plr.setBedSpawnLocation( null );
            target.getLogger().log( Level.ERROR, "Failed to set bed spawn location for player "+ plr.getName() );
            target.getLogger().log( Level.ERROR, e );
        }
    }
    
    private VirtualPlayer toVirtualPlayer(Player plr, VirtualServer target) throws Exception
    {
        SqlPlayerEntry plrEntry = db.player.get( plr.getName() );
        if(plrEntry == null)
        {
            // new player across entire network
        
            db.player.insert( plr );
            plrEntry = db.player.get( plr.getName() );
        }
    
        SqlVirtualPlayerEntry vpEntry = db.virtualPlayer.get( plrEntry.id, target.id );
        if(vpEntry == null)
        {
            // new player locally
            
            db.virtualPlayer.insert( plrEntry.id, target.id );
            vpEntry = db.virtualPlayer.get( plrEntry.id, target.id );
            plr.teleport( target.getSpawn() );
        }
        
        return new VirtualPlayer( vpEntry.id, plr, target );
    }
    
    private void savePlayerLocations(VirtualPlayer vp)
    {
        try {
            db.virtualPlayer.setQuitLocation( vp );
        }
        catch(Exception e)
        {
            vp.server.getLogger().log( Level.ERROR, "Failed to save quit location for player "+ vp.plr.getName() );
            vp.server.getLogger().log( Level.ERROR, e );
        }
        try {
            db.virtualPlayer.setBedRespawnLocation( vp );
        }
        catch(Exception e)
        {
            vp.server.getLogger().log( Level.ERROR, "Failed to save bed respawn location for player "+ vp.plr.getName() );
            vp.server.getLogger().log( Level.ERROR, e );
        }
    }
}
