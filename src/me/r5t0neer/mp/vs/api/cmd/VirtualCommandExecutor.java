package me.r5t0neer.mp.vs.api.cmd;

import org.bukkit.plugin.Plugin;

// overrides global
public abstract class VirtualCommandExecutor extends CommandExecutor
{
    public VirtualCommandExecutor(Plugin plugin) {
        super(plugin);
    }

    public abstract void handle(VirtualCommandSender sender, String argsString) throws Exception;
}
