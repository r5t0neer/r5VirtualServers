package me.r5t0neer.mp.vs.log;

public abstract class ConsoleCommandListener
{
    // todo tcp stream & write
    
    public abstract void onReceived(int uid, String uName, String cmdString);
}
