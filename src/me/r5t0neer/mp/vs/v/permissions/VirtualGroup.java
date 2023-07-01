package me.r5t0neer.mp.vs.v.permissions;

import java.util.Map;
import java.util.Set;



public class VirtualGroup
{
    public final VirtualRank rank;
    public final boolean def;
    // mode: allow, key is alias, value is duplicated for more aliases
    private final Map<String, CommandPermission> commandPermissions;
    private final Set<String> customPermissions;
    
    public VirtualGroup(VirtualRank rank, boolean def, Map<String, CommandPermission> commands, Set<String> permissions)
    {
        this.rank = rank;
        this.def = def;
        this.commandPermissions = commands;
        this.customPermissions = permissions;
    }
    
    public boolean containsPermissionCaseSensitive(String permission)
    {
        return this.customPermissions.contains( permission );
    }
    
    public CommandPermission hasCommandCaseSensitive(String alias)
    {
        return this.commandPermissions.get( alias );
    }
}