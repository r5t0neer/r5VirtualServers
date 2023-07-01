package me.r5t0neer.mp.vs.sql.entry;



public class SqlHubNPCEntry
{
    public final int id, vsrv_id;
    public final String uuid,name,world,texture,signature;
    public final double x,y,z;
    public final float yaw,pitch;
    
    public SqlHubNPCEntry(int id, String uuid, String name, int vsrv_id, String world, double x, double y, double z, float yaw, float pitch, String texture, String signature)
    {
        assert uuid.length() == 36;
        
        this.id = id;
        this.vsrv_id = vsrv_id;
        this.uuid = uuid;
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.texture = texture;
        this.signature = signature;
    }
}
