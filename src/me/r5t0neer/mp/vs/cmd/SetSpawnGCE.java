package me.r5t0neer.mp.vs.cmd;

import me.r5t0neer.mp.vs.api.cmd.GlobalCommandExecutor;
import me.r5t0neer.mp.vs.v.VirtualServer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetSpawnGCE extends GlobalCommandExecutor
{
    private final VirtualServerNetwork vsn;

    public SetSpawnGCE(VirtualServerNetwork vsn, Plugin plugin) {
        super(plugin);
        this.vsn = vsn;
    }

    @Override
    public void handle(CommandSender sender, String argsString) throws Exception {
        Player plr = (Player) sender;
        VirtualServer server = vsn.plrToVSrvMap.get(plr);
        if(server.setSpawn(plr.getLocation()))
            plr.sendMessage("Spawn changed for server "+ server.getName());
        else plr.sendMessage("This world does not belong to the server.");
    }
}
