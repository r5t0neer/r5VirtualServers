package me.r5t0neer.mp.vs.sql;

import me.r5t0neer.mp.vs.sql.entry.SqlPlayerEntry;
import me.r5t0neer.mp.vs.sql.entry.SqlVirtualPlayerEntry;
import me.r5t0neer.mp.vs.util.UuidUtils;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class SqlQVirtualPlayer extends SqlQ
{
    public SqlQVirtualPlayer(SQLDatabase db)
    {
        super( db );
    }
    
    @Override
    public void createTablesIfNotExist() throws Exception
    {
        Connection c = c();
        createTableIfNotExists( c, "vplr(" +
                                           "id INT NOT NULL AUTO_INCREMENT," +
                                           "pid INT NOT NULL," +
                                           "sid INT NOT NULL," +
                                           "PRIMARY KEY(id)" +
                                           ") ENGINE=InnoDB" );
    
        createTableIfNotExists( c, "vplr_ql(" +
                                           "sid INT NOT NULL," +
                                           "vpid INT NOT NULL," +
                                           "w VARCHAR(32) NOT NULL," +
                                           "x DOUBLE NOT NULL," +
                                           "y DOUBLE NOT NULL," +
                                           "z DOUBLE NOT NULL," +
                                           "yaw FLOAT NOT NULL," +
                                           "pitch FLOAT NOT NULL" +
                                           ") ENGINE=InnoDB" );
    
        createTableIfNotExists( c, "vplr_bl(" +
                                           "sid INT NOT NULL," +
                                           "vpid INT NOT NULL," +
                                           "w VARCHAR(32) NOT NULL," +
                                           "x DOUBLE NOT NULL," +
                                           "y DOUBLE NOT NULL," +
                                           "z DOUBLE NOT NULL," +
                                           "yaw FLOAT NOT NULL," +
                                           "pitch FLOAT NOT NULL" +
                                           ") ENGINE=InnoDB" );
    }
    
    public void insert(int pid, int sid) throws Exception
    {
        c().createStatement().executeUpdate( "INSERT INTO vplr (pid,sid) VALUES ("+pid+","+sid+")" );
    }
    
    public @Nullable SqlVirtualPlayerEntry get(int pid, int sid) throws Exception
    {
        ResultSet rs = c().createStatement().executeQuery( "SELECT id,pid,sid FROM vplr WHERE pid="+ pid +" AND sid="+ sid );
        if(rs.next())
        {
            return new SqlVirtualPlayerEntry(
                    rs.getInt( 1 ),
                    rs.getInt( 2 ),
                    rs.getInt( 3 )
            );
        }
        else return null;
    }
    
    public void setQuitLocation(VirtualPlayer vp) throws Exception
    {
        Connection c = c();
        ResultSet rs = c.createStatement().executeQuery( "SELECT sid FROM vplr_ql WHERE sid="+ vp.getServer().id +" AND vpid="+ vp.id );
        Location l = vp.plr.getLocation();
        if(rs.next())
        {
            PreparedStatement ps = c.prepareStatement( "UPDATE vplr_ql SET w=?,x="+ l.getX() +",y="+ l.getY() +",z="+ l.getZ() +",yaw="+ l.getYaw() +",pitch="+l.getPitch() +" WHERE " +
                                                               "sid="+ vp.getServer().id +" AND vpid="+ vp.id );
            ps.setString( 1, l.getWorld().getName() );
            ps.executeUpdate();
        }
        else
        {
            PreparedStatement ps = c.prepareStatement( "INSERT INTO vplr_ql VALUES ("+ vp.getServer().id +","+ vp.id +",?,"+ l.getX() +","+ l.getY() +","+ l.getZ() +","+ l.getYaw() +","+l.getPitch() +")" );
            ps.setString( 1, l.getWorld().getName() );
            ps.executeUpdate();
        }
    }
    
    public @Nullable Location getQuitLocation(VirtualPlayer vp, VirtualServer target) throws Exception
    {
        ResultSet rs = c().createStatement().executeQuery( "SELECT w,x,y,z,yaw,pitch FROM vplr_ql WHERE sid="+ target.id +" AND vpid="+ vp.id );
        if(rs.next())
        {
            String w = rs.getString( 1 );
            if(target.getWorldsNames().contains( w ))
            {
                return new Location(
                        Bukkit.getWorld(w),
                        rs.getDouble( 2 ),
                        rs.getDouble( 3 ),
                        rs.getDouble( 4 ),
                        rs.getFloat( 5 ),
                        rs.getFloat( 6 )
                );
            }
            else return null;
        }
        else return null;
    }
    
    public void setBedRespawnLocation(VirtualPlayer vp) throws Exception
    {
        Location l = vp.plr.getBedSpawnLocation();
        if(l == null)
            return;
        
        Connection c = c();
        ResultSet rs = c.createStatement().executeQuery( "SELECT sid FROM vplr_bl WHERE sid="+ vp.getServer().id +" AND vpid="+ vp.id );
        if(rs.next())
        {
            PreparedStatement ps = c.prepareStatement( "UPDATE vplr_bl SET w=?,x="+ l.getX() +",y="+ l.getY() +",z="+ l.getZ() +",yaw="+ l.getYaw() +",pitch="+l.getPitch() +" WHERE " +
                                                               "sid="+ vp.getServer().id +" AND vpid="+ vp.id );
            ps.setString( 1, l.getWorld().getName() );
            ps.executeUpdate();
        }
        else
        {
            PreparedStatement ps = c.prepareStatement( "INSERT INTO vplr_bl VALUES ("+ vp.getServer().id +","+ vp.id +",?,"+ l.getX() +","+ l.getY() +","+ l.getZ() +","+ l.getYaw() +","+l.getPitch() +")" );
            ps.setString( 1, l.getWorld().getName() );
            ps.executeUpdate();
        }
    }
    
    public @Nullable Location getBedSpawnLocation(VirtualPlayer vp, VirtualServer target) throws Exception
    {
        ResultSet rs = c().createStatement().executeQuery( "SELECT w,x,y,z,yaw,pitch FROM vplr_bl WHERE sid="+ target.id +" AND vpid="+ vp.id );
        if(rs.next())
        {
            String w = rs.getString( 1 );
            if(target.getWorldsNames().contains( w ))
            {
                return new Location(
                        Bukkit.getWorld(w),
                        rs.getDouble( 2 ),
                        rs.getDouble( 3 ),
                        rs.getDouble( 4 ),
                        rs.getFloat( 5 ),
                        rs.getFloat( 6 )
                );
            }
            else return null;
        }
        else return null;
    }
}
