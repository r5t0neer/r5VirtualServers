package me.r5t0neer.mp.vs.api.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


public abstract class GlobalCommandExecutor extends CommandExecutor
{
    public GlobalCommandExecutor(Plugin plugin) {
        super(plugin);
    }

    public abstract void handle(CommandSender sender, String argsString) throws Exception;
}
