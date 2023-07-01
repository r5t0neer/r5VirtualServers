package me.r5t0neer.mp.vs.lst;

import me.r5t0neer.mp.vs.api.evt.g.VirtualPlayerServerSwitchAnyPostEvent;
import me.r5t0neer.mp.vs.api.evt.lst.VPSSAPEListener;
import me.r5t0neer.mp.vs.cmd.NPCTeleportsGCE;
import org.bukkit.plugin.Plugin;



public class HubJoinListener extends VPSSAPEListener
{
    private final NPCTeleportsGCE npct;
    
    public HubJoinListener(Plugin plugin, NPCTeleportsGCE npct)
    {
        super( plugin );
        this.npct = npct;
    }
    
    @Override
    public void onEvent(VirtualPlayerServerSwitchAnyPostEvent evt) throws Exception
    {
        if(!evt.target.isDefault())
        {
            if(evt.source.isDefault())
                npct.onPlayerQuit( evt.vp.plr );
        }
        else npct.onPlayerJoin( evt.vp.plr );
    }
}
