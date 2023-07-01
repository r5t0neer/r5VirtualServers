package me.r5t0neer.mp.vs.lst;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import me.r5t0neer.mp.vs.util.MessageUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;



public class MotdListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMotdEvent(PaperServerListPingEvent evt)
    {
        evt.setMaxPlayers( 20150825 );//0801,0116,20140923,20120724,20110220?,20130330
        
        evt.setMotd( MessageUtils.colorize( "&fFun&f&lPlay &7| No Knobs, Just Works" +
                                                    "\n&7★·.·´¯`·.·★ [Releasing Soon ᵃᵇᶜ] ★·.·´¯`·.·★" ) );
        evt.setProtocolVersion( -1 );
        if(evt.getClient().getProtocolVersion() == 754)
            evt.setVersion( MessageUtils.colorize( "&l&cPowerMC &a1.16.4-5 &f"+ evt.getNumPlayers() +"&8/&6♾" ) );
        else evt.setVersion( MessageUtils.colorize( "&l&eWejdz na wersji &a1.16.4-5 &f"+ evt.getNumPlayers() +"&8/&6♾" ) );
    }
}
