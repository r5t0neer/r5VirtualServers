package me.r5t0neer.mp.vs.sql;

import me.r5t0neer.mp.vs.sql.entry.SqlHubNPCEntry;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class SqlQHubNPC extends SqlQ
{
    SqlQHubNPC(SQLDatabase db)
    {
        super( db );
    }
    
    @Override
    public void createTablesIfNotExist() throws Exception
    {
        Connection conn = c();
        createTableIfNotExists( conn, "hub_npc(" +
                                              "id INT AUTO_INCREMENT NOT NULL," +
                                              "uuid CHAR(36) NOT NULL," +
                                              "name VARCHAR(32) NOT NULL," +
                                              "vsrv_id INT NOT NULL," +
                                              "w VARCHAR(32) NOT NULL," +
                                              "x DOUBLE NOT NULL," +
                                              "y DOUBLE NOT NULL," +
                                              "z DOUBLE NOT NULL," +
                                              "yaw FLOAT NOT NULL," +
                                              "pitch FLOAT NOT NULL," +
                                              "texture VARCHAR(255) NOT NULL," +
                                              "signature VARCHAR(255) NOT NULL," +
                                              "PRIMARY KEY(id)" +
                                              ") ENGINE=InnoDB" );
    }
    
    public void insertNPC(SqlHubNPCEntry entry) throws Exception
    {
        Connection conn = c();
        PreparedStatement ps = conn.prepareStatement( "INSERT INTO hub_npc (uuid,name,vsrv_id,w,x,y,z,yaw,pitch,texture,signature) VALUES (?,?,"
                                                              + entry.vsrv_id +",?,"+
                                                              entry.x + ","
                                                              + entry.y +","
                                                              + entry.z +","
                                                              + entry.yaw +","
                                                              + entry.pitch +",?,?)" );
        ps.setString( 1, entry.uuid );
        ps.setString( 2, entry.name );
        ps.setString( 3, entry.world );
        ps.setString( 4, entry.texture );
        ps.setString( 5, entry.signature );
        ps.executeUpdate();
    }
    
    public @Nullable SqlHubNPCEntry getLastNPC() throws Exception
    {
        ResultSet rs = c().createStatement().executeQuery( "SELECT * FROM hub_npc ORDER BY id DESC LIMIT 1" );
        if(rs.next())
        {
            return new SqlHubNPCEntry(
                    rs.getInt( 1 ),
                    rs.getString( 2 ),
                    rs.getString( 3 ),
                    rs.getInt( 4 ),
                    rs.getString( 5 ),
                    rs.getDouble( 6 ),
                    rs.getDouble( 7 ),
                    rs.getDouble( 8 ),
                    rs.getFloat( 9 ),
                    rs.getFloat( 10 ),
                    rs.getString( 11 ),
                    rs.getString( 12 )
            );
        }
        else return null;
    }
    
    public List<SqlHubNPCEntry> listNPCs() throws Exception
    {
        List<SqlHubNPCEntry> entries = new ArrayList<>();
        
        ResultSet rs = c().createStatement().executeQuery( "SELECT * FROM hub_npc" );
        while(rs.next())
        {
            entries.add( new SqlHubNPCEntry(
        
                    rs.getInt( 1 ),
                    rs.getString( 2 ),
                    rs.getString( 3 ),
                    rs.getInt( 4 ),
                    rs.getString( 5 ),
                    rs.getDouble( 6 ),
                    rs.getDouble( 7 ),
                    rs.getDouble( 8 ),
                    rs.getFloat( 9 ),
                    rs.getFloat( 10 ),
                    rs.getString( 11 ),
                    rs.getString( 12 )
                    
            ) );
        }
        
        return entries;
    }
    
    public void removeNPC(int id) throws Exception
    {
        c().createStatement().executeUpdate( "DELETE FROM hub_npc WHERE id=" + id );
    }
    
    public void removeNPCServer(int vsrv_id) throws Exception
    {
        c().createStatement().executeUpdate( "DELETE FROM hub_npc WHERE vsrv_id=" + vsrv_id );
    }
}
