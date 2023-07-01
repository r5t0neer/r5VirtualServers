package me.r5t0neer.mp.vs.lst;

import me.r5t0neer.mp.vs.api.cmd.GlobalCommandExecutor;
import me.r5t0neer.mp.vs.api.cmd.VirtualCommandExecutor;
import me.r5t0neer.mp.vs.api.cmd.VirtualCommandSender;
import me.r5t0neer.mp.vs.log.EngineLogger;
import me.r5t0neer.mp.vs.log.Level;
import me.r5t0neer.mp.vs.util.CommandUtil;
import me.r5t0neer.mp.vs.v.VirtualPlayer;
import me.r5t0neer.mp.vs.v.VirtualServer;
import me.r5t0neer.mp.vs.v.VirtualServerNetwork;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



public class PlayerCommandPreprocessListener implements Listener
{
    private final EngineLogger logger;
    private final VirtualServerNetwork vsn;
    private final Map<String, GlobalCommandExecutor> executorMap = new HashMap<>();
    private final Map<String, Map<VirtualServer, VirtualCommandExecutor>> virtualExecutorMap = new HashMap<>();

    public PlayerCommandPreprocessListener(EngineLogger logger, VirtualServerNetwork vsn)
    {
        this.logger = logger;
        this.vsn = vsn;
    }
    
    public void setExecutor(String[] aliases, GlobalCommandExecutor executor)
    {
        for(String alias : aliases)
        {
            GlobalCommandExecutor overridden = executorMap.put(alias.toLowerCase(), executor);
            if(overridden != null)
                logger.log(Level.COMMAND, "Command '"+ alias +"' from plugin "+ overridden.pluginName +" is overridden by plugin "+ executor.pluginName);
        }
    }

    public void setVirtualExecutor(String[] aliases, VirtualCommandExecutor executor, VirtualServer server)
    {
        for(String alias : aliases)
        {
            alias = alias.toLowerCase();
            Map<VirtualServer, VirtualCommandExecutor> executors = virtualExecutorMap.get(alias);
            if(executors == null) {
                virtualExecutorMap.put(alias, new HashMap<>());
                executors = virtualExecutorMap.get(alias);
            }

            VirtualCommandExecutor overridden = executors.put(server, executor);
            if(overridden != null)
                server.getLogger().log(Level.COMMAND, "Command '"+ alias +"' from plugin "+ overridden.pluginName +" is overridden by plugin "+ executor.pluginName);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPCPEvent(PlayerCommandPreprocessEvent evt)
    {
        if(!evt.getMessage().startsWith( "/" )) return;
        
        String[] parts = CommandUtil.getCommandNameAndArgsString( evt.getMessage().substring( 1 ) );
        String cmdName = parts[0].toLowerCase();

        if(cmdName.equalsIgnoreCase( "register" ) ||
           cmdName.equalsIgnoreCase( "login" ))
        {
            return;
        }
        
        VirtualServer server = vsn.plrToVSrvMap.get(evt.getPlayer());
        if(server != null)
        {
            Map<VirtualServer, VirtualCommandExecutor> vem = virtualExecutorMap.get(cmdName);
            if(vem != null)
            {
                VirtualCommandExecutor vce = vem.get( server );
                if(vce!=null)
                {
                    server.getLogger().log( Level.COMMAND, "Plr " + evt.getPlayer().getName() + " " + evt.getMessage() );
        
                    try
                    {
                        vce.handle(
                                new VirtualCommandSender( server, evt.getPlayer() ),
                                parts[ 1 ]
                        );
                    }
                    catch(Exception e)
                    {
                        server.getLogger().log( Level.COMMAND, e );
                        evt.getPlayer().sendMessage( "Internal error while executing this command. See console." );
                    }
        
                    evt.setCancelled( true );
                    return;
                }
            }
        }

        GlobalCommandExecutor gce = executorMap.get( cmdName );
        if(gce != null)
        {
            logger.log(Level.COMMAND, "Plr "+ evt.getPlayer().getName() +" exe on srv "+ (server != null ? server.getName() : "?null?") +" "+ evt.getMessage());
            if(server != null)
                logger.log( Level.COMMAND, "Plr "+ evt.getPlayer().getName() +" "+ evt.getMessage() );

            try
            {
                gce.handle( evt.getPlayer(), parts[ 1 ] );
            }
            catch(Exception e)
            {
                logger.log( Level.COMMAND, e );
                evt.getPlayer().sendMessage( "Internal error while executing this command. See console." );
            }
            
            evt.setCancelled( true );
            return;
        }

        //if(server != null)
        
        if(!evt.getPlayer().isOp())
        {
            if(server != null)
            {
                server.getLogger().log( Level.COMMAND, "Plr " + evt.getPlayer().getName() + " unsupp /" + parts[ 0 ] );
            }
            else
            {
                logger.log( Level.COMMAND, "Plr " + evt.getPlayer().getName() + " unsupp /" + parts[0] );
            }
            
            evt.getPlayer().sendMessage( "Unsupported command." );
            evt.setCancelled( true );
        }
    }
}
