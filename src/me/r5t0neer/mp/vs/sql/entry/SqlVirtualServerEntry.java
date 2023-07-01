package me.r5t0neer.mp.vs.sql.entry;

public class SqlVirtualServerEntry
{
    public final int id;
    public final boolean active;
    public final String name, sWName;
    public final double sX,sY,sZ;
    public final float sYaw,sPitch;
    
    public SqlVirtualServerEntry(int id, String name, boolean active, String sWName, double sX, double sY, double sZ, float sYaw, float sPitch)
    {
        this.id = id;
        this.active = active;
        this.name = name;
        this.sWName = sWName;
        this.sX = sX;
        this.sY = sY;
        this.sZ = sZ;
        this.sYaw = sYaw;
        this.sPitch = sPitch;
    }
}
