package me.r5t0neer.mp.vs.api.evt.lst;

import org.bukkit.plugin.Plugin;

public abstract class EventListener
{
    public final String pluginName;

    public EventListener(Plugin plugin) {this.pluginName=plugin.getName();}
}
