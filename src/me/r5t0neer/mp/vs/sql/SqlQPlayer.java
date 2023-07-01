package me.r5t0neer.mp.vs.sql;

import me.r5t0neer.mp.vs.sql.entry.SqlPlayerEntry;
import me.r5t0neer.mp.vs.util.UuidUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class SqlQPlayer extends SqlQ
{
    public SqlQPlayer(SQLDatabase db)
    {
        super( db );
    }
    
    @Override
    public void createTablesIfNotExist() throws Exception
    {
        Connection c = c();
        createTableIfNotExists( c, "plr(" +
                                           "id INT NOT NULL AUTO_INCREMENT," +
                                           "uuid BINARY(16) NOT NULL UNIQUE," +
                                           "name VARCHAR(16) NOT NULL UNIQUE," +
                                           "PRIMARY KEY(id)" +
                                           ") ENGINE=InnoDB" );
    }
    
    public void insert(Player plr) throws Exception
    {
        PreparedStatement ps = c().prepareStatement( "INSERT INTO plr (uuid,name) VALUES (?,?)" );
        ps.setBytes( 1, UuidUtils.toBytes( plr.getUniqueId() ) );
        ps.setString( 2, plr.getName() );
        ps.executeUpdate();
    }
    
    public @Nullable SqlPlayerEntry get(String name) throws Exception
    {
        PreparedStatement ps = c().prepareStatement( "SELECT id,uuid,name FROM plr WHERE name=?" );
        ps.setString( 1, name );
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            return new SqlPlayerEntry(
                    rs.getInt( 1 ),
                    rs.getBytes( 2 ),
                    rs.getString( 3 )
            );
        }
        else return null;
    }
}
