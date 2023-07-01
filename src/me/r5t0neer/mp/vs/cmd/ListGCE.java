package me.r5t0neer.mp.vs.cmd;

import me.r5t0neer.mp.vs.api.cmd.GlobalCommandExecutor;
import me.r5t0neer.mp.vs.util.MessageUtils;
import me.r5t0neer.mp.vs.v.VirtualServer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;



public class ListGCE extends GlobalCommandExecutor
{
    private final VirtualServerNetwork vsn;
    
    public ListGCE(Plugin plugin, VirtualServerNetwork vsn)
    {
        super( plugin );
        this.vsn = vsn;
    }
    
    @Override
    public void handle(CommandSender sender, String argsString) throws Exception
    {
        sender.sendMessage( MessageUtils.colorize( "&7Siec obsluguje w tej chwili &f"+ vsn.plrToVPlrMap.size() +" graczy" ) );
        
        if(sender instanceof Player plr)
        {
            sender.sendMessage( MessageUtils.colorize( "&8W tym &f" + vsn.plrToVSrvMap.get(plr).onlinePlayersCount() + " graczy &8na serwerze na którym przebywasz" ) );
        }
        else
        {
            for( VirtualServer vs : vsn.servers )
            {
                sender.sendMessage( MessageUtils.colorize( (vs.isActive() ? "&a" : "&cOFFLINE ") + vs.getName() + " &7-> &f" + vs.onlinePlayersCount() +" graczy" ) );
            }
        }
    }
}
