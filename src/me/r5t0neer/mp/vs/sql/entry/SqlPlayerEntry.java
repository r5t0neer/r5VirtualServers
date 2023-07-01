package me.r5t0neer.mp.vs.sql.entry;

public class SqlPlayerEntry
{
    public final int id;
    public final byte[] uuidBytes;
    public final String name;
    
    public SqlPlayerEntry(int id, byte[] uuidBytes, String name)
    {
        this.id = id;
        this.uuidBytes = uuidBytes;
        this.name = name;
    }
}
