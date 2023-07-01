package me.r5t0neer.mp.vs.sql;

import java.sql.Connection;



public abstract class SqlQ
{
    private SQLDatabase db;
    
    SqlQ(SQLDatabase db)
    {
        this.db = db;
    }
    
    Connection c() throws Exception { return db.getConnection(); }
    
    void createTableIfNotExists(Connection c, String suffix) throws Exception
    {
        c.createStatement().executeUpdate( "CREATE TABLE IF NOT EXISTS "+ suffix );
    }
    
    public abstract void createTablesIfNotExist() throws Exception;
}
