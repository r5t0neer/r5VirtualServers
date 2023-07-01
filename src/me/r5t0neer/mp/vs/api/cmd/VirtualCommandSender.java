package me.r5t0neer.mp.vs.api.cmd;

import me.r5t0neer.mp.vs.v.VirtualServer;
import org.bukkit.command.CommandSender;

public class VirtualCommandSender
{
    public final VirtualServer server;
    public final CommandSender sender;

    public VirtualCommandSender(VirtualServer server, CommandSender sender) {
        this.server = server;
        this.sender = sender;
    }
}
