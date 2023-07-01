package me.r5t0neer.mp.vs.v.permissions;

public class VirtualRank
{
    public final ChatCompiledFormat chatPostFormat;
    public final ChatCompiledFormat chatPrivateFormat;
    public final String tabPrefix;
    
    public VirtualRank(ChatCompiledFormat chatPostFormat, ChatCompiledFormat chatPrivateFormat, String tabPrefix)
    {
        this.chatPostFormat = chatPostFormat;
        this.chatPrivateFormat = chatPrivateFormat;
        this.tabPrefix = tabPrefix;
    }
}
