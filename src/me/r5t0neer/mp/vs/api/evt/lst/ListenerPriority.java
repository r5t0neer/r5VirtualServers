package me.r5t0neer.mp.vs.api.evt.lst;

public enum ListenerPriority
{
    LOWEST(0),
    LOW(63),
    MEDIUM(127),
    HIGH(169),
    HIGHEST(211),
    MONITOR(255);

    public final int level;

    ListenerPriority(int level)
    {
        this.level = level;
    }
}
