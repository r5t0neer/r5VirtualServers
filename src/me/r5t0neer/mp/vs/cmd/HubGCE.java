package me.r5t0neer.mp.vs.cmd;

import me.r5t0neer.mp.vs.api.cmd.GlobalCommandExecutor;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class HubGCE extends GlobalCommandExecutor
{
    private final VirtualServerNetwork vsn;

    public HubGCE(VirtualServerNetwork vsn, Plugin plugin) {
        super(plugin);
        this.vsn = vsn;
    }

    @Override
    public void handle(CommandSender sender, String argsString) throws Exception
    {
        VirtualPlayer vp = vsn.plrToVPlrMap.get((Player)sender);
        vsn.switchPlayer(vp, vsn.defaultServer, false, true, true);
    }
}
