package me.r5t0neer.mp.vs.api.cmd;

import org.bukkit.plugin.Plugin;

public abstract class CommandExecutor
{
    public final String pluginName;

    public CommandExecutor(Plugin plugin) {this.pluginName=plugin.getName();}
}
