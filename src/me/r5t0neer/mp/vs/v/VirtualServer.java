package me.r5t0neer.mp.vs.v;

import me.r5t0neer.mp.vs.api.VirtualApi;
import me.r5t0neer.mp.vs.api.evt.srv.VirtualServerStopEvent;
import me.r5t0neer.mp.vs.log.Level;
import me.r5t0neer.mp.vs.log.LoggingServiceClient;
import me.r5t0neer.mp.vs.log.ServerLogger;
import me.r5t0neer.mp.vs.sql.SQLDatabase;
import me.r5t0neer.mp.vs.sql.entry.SqlVirtualServerEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerKickEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;



public class VirtualServer
{
    private final ServerLogger logger;
    private final SQLDatabase db;

    private boolean def;
    public final int id;
    private boolean active;
    private String name;
    private int slots;
    private List<String> worldsNames;
    private Location spawn;
    List<VirtualPlayer> players = new ArrayList<>();
    
    VirtualChat chat;
    
    VirtualServer(Path dir, SQLDatabase db, LoggingServiceClient ls) throws Exception
    {
        this.name = dir.getFileName().toString();

        this.db = db;
        this.logger = ls.getServerLogger(this.name);
        
        File confFile = new File( dir.toString(), "server.yml" );
        if(!confFile.exists())
            throw new IOException("Could not find server configuration file 'server.yml' for "+ name);
    
        YamlConfiguration conf = YamlConfiguration.loadConfiguration( confFile );
        
        this.slots = conf.getInt( "slots", 20 );
        this.worldsNames = conf.getStringList( "worlds" );
        this.def = conf.getBoolean( "default", false );
        
        if(worldsNames.isEmpty()) throw new Exception("Server '"+ name +"' has no worlds configured");
        
        World w = getFirstLoadedWorld();
        
        if(w == null) throw new Exception("None configured world of '"+ name +"' server exists");
        
        SqlVirtualServerEntry entry = db.virtualServer.get( name );
        if(entry == null)
        {
            spawn = w.getSpawnLocation();
            db.virtualServer.insert( this );
            entry = db.virtualServer.get( name );
        }
        else
        {
            if(!entry.sWName.equals( w.getName() ))
            {
                w = Bukkit.getWorld( entry.sWName );
                if(w == null) throw new Exception("Spawn world for server '"+ name +"' does not exist");
            }
            
            spawn = new Location(
                    w,
                    entry.sX, entry.sY, entry.sZ,
                    entry.sYaw, entry.sPitch
            );
        }
    
        this.id = entry.id;
        this.active = entry.active;
        this.chat = new VirtualChat( this );
    }

    public ServerLogger getLogger() {return logger;}
    public boolean isDefault() {return def;}
    public boolean isActive() {return active;}
    public int getSlots() {return slots;}
    public String getName() {return name;}
    public List<String> getWorldsNames() {return worldsNames;}
    public Location getSpawn() {return spawn;}
    public boolean isFull(){return players.size()>=slots;}
    public int onlinePlayersCount() {return players.size();}
    
    private @Nullable World getFirstLoadedWorld()
    {
        for(String name : worldsNames)
        {
            World w = Bukkit.getWorld(name);
            if(w != null) return w;
        }
        
        return null;
    }

    public boolean setSpawn(Location loc)
    {
        String wName = loc.getWorld().getName();
        for(String w : worldsNames)
            if(w.equals(wName))
            {
                spawn = loc;
                try {
                    db.virtualServer.setSpawn( this, loc );
                }
                catch(Exception exc) {
                    logger.log( Level.COMMAND, "Failed to set spawn" );
                    logger.log( Level.COMMAND, exc );
                }
                return true;
            }

        return false;
    }
    
    public void forEachPlayer(Consumer<VirtualPlayer> f)
    {
        for(VirtualPlayer vplr : this.players)
            f.accept(vplr);
    }

    public void stop(VirtualServerNetwork vsn)
    {
        VirtualApi api = VirtualApi.inst();
        api.sem.fireEvent(new VirtualServerStopEvent(this), this);

        VirtualServer def = vsn.defaultServer;

        if(def.isActive())
        {
            for (VirtualPlayer vp : players)
                vsn.switchPlayer(vp, def, true, true);
        }
        else
        {
            logger.log(Level.FATAL, "Could not properly stop server - cannot switch players to default server, it's inactive. Kicking them out of servers network.");
            for(VirtualPlayer vp : players)
            {
                vp.plr.kick(Component.text("Server went down, failed to move to default server"), PlayerKickEvent.Cause.RESTART_COMMAND);
                // todo quit location
            }
        }

        players.clear();

        setActive(false);
    }
    
    public void removePlayer(VirtualPlayer vp)
    {
        int i=0;
        boolean found=false;
        for(VirtualPlayer another : this.players)
        {
            if(another.id==vp.id)
            {
                found=true;
                break;
            }
            ++i;
        }
        
        if(found)
        {
            this.players.remove( i );
        }
    }

    public void start()
    {
        // todo load plugins

        setActive(true);
    }

    private void setActive(boolean active)
    {
        this.active = active;

        try {db.virtualServer.setActive(this);}
        catch (Exception e) {logger.log(Level.FATAL, e);}
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualServer that = (VirtualServer) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {return id;}
}
