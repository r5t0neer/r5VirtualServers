package me.r5t0neer.mp.vs.sql;

import me.r5t0neer.mp.vs.sql.entry.SqlVirtualServerEntry;
import me.r5t0neer.mp.vs.v.VirtualServer;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class SqlQVirtualServer extends SqlQ
{
    SqlQVirtualServer(SQLDatabase db)
    {
        super( db );
    }
    
    @Override
    public void createTablesIfNotExist() throws Exception
    {
        Connection c = c();
        createTableIfNotExists( c, "srv(" +
                                        "id INT NOT NULL AUTO_INCREMENT," +
                                        "name VARCHAR(32) NOT NULL UNIQUE," +
                                        "active TINYINT(1) NOT NULL," +
                                        "s_w VARCHAR(32) NOT NULL," +
                                        "s_x DOUBLE NOT NULL," +
                                        "s_y DOUBLE NOT NULL," +
                                        "s_z DOUBLE NOT NULL," +
                                        "s_yaw FLOAT NOT NULL," +
                                        "s_pitch FLOAT NOT NULL," +
                                        "PRIMARY KEY(id)" +
                                        ") ENGINE=InnoDB" );
    }
    
    public void insert(VirtualServer vs) throws Exception
    {
        Location spawn = vs.getSpawn();
        
        PreparedStatement ps = c().prepareStatement( "INSERT INTO srv (name,active,s_w,s_x,s_y,s_z,s_yaw,s_pitch) VALUES (?,1,?,"+ spawn.getX() +","+ spawn.getY() +","+ spawn.getZ() +","+ spawn.getYaw() +","+ spawn.getPitch() +")" );
        ps.setString( 1, vs.getName() );
        ps.setString( 2, spawn.getWorld().getName() );
        ps.executeUpdate();
    }
    
    public @Nullable SqlVirtualServerEntry get(String name) throws Exception
    {
        PreparedStatement ps = c().prepareStatement( "SELECT id,active,s_w,s_x,s_y,s_z,s_yaw,s_pitch FROM srv WHERE name=?" );
        ps.setString( 1, name );
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            return new SqlVirtualServerEntry(
                    rs.getInt( 1 ),
                    name,
                    rs.getBoolean( 2 ),
                    rs.getString( 3 ),
                    rs.getDouble( 4 ),
                    rs.getDouble( 5 ),
                    rs.getDouble( 6 ),
                    rs.getFloat( 7 ),
                    rs.getFloat( 8 )
            );
        }
        else return null;
    }

    public void setActive(VirtualServer server) throws Exception
    {
        c().createStatement().executeUpdate("UPDATE srv SET active="+ (server.isActive() ? 1 : 0) +" WHERE id="+ server.id);
    }
    
    public void setSpawn(VirtualServer server, Location l) throws Exception
    {
        PreparedStatement ps = c().prepareStatement( "UPDATE srv SET s_w=?,s_x="+ l.getX() +",s_y="+ l.getY() +",s_z="+ l.getZ() +",s_yaw="+ l.getYaw() +",s_pitch="+ l.getPitch() +" WHERE id="+ server.id );
        ps.setString( 1, l.getWorld().getName() );
        ps.executeUpdate();
    }
}
