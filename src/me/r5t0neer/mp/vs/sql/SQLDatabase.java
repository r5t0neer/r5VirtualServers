package me.r5t0neer.mp.vs.sql;

import java.sql.Connection;
import java.sql.DriverManager;



public class SQLDatabase
{
    private Connection c;
    
    private String connString, usr, pwd, dbName;
    
    public SqlQVirtualServer virtualServer;
    public SqlQPlayer player;
    public SqlQVirtualPlayer virtualPlayer;
    public SqlQHubNPC hubNPC;
    
    public SQLDatabase() throws Exception
    {
        connString = "jdbc:mysql://127.0.0.1:3306";
        usr = "virtualservers";
        pwd = "xyz";
        dbName = usr;
        
        virtualServer = new SqlQVirtualServer( this );
        player = new SqlQPlayer( this );
        virtualPlayer = new SqlQVirtualPlayer( this );
        hubNPC = new SqlQHubNPC( this );
    
        init();
    }
    
    private void init() throws Exception
    {
        c = DriverManager.getConnection( connString, usr, pwd );
        
        createDatabaseIfNotExists( c );
        
        virtualServer.createTablesIfNotExist();
        player.createTablesIfNotExist();
        virtualPlayer.createTablesIfNotExist();
        hubNPC.createTablesIfNotExist();
    }
    
    private void createDatabaseIfNotExists(Connection c) throws Exception
    {
        c.prepareStatement( "CREATE DATABASE IF NOT EXISTS "+ dbName ).executeUpdate();
        c.setCatalog( dbName );
    }
    
    public Connection getConnection() throws Exception
    {
        try {
            if(!c.isValid( 2 ))
            {
                init();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return c;
    }
}
