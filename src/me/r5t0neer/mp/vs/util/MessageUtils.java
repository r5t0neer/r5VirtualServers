package me.r5t0neer.mp.vs.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MessageUtils
{
    private static final Pattern hexPattern = Pattern.compile("<#([A-Fa-f0-9]){6}>");
    public static @NotNull String colorize(String message)
    {
        if(message == null) return "";
        
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find())
        {
            final net.md_5.bungee.api.ChatColor hexColor = net.md_5.bungee.api.ChatColor.of(
                    matcher.group().substring(1, matcher.group().length() - 1)
            );
            final String                        before   = message.substring(0, matcher.start());
            final String                        after    = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static @NotNull String gradient(@NotNull RGBColor from, @NotNull RGBColor to, String message)
    {
        if(message == null) return "";
        
        double rStep = (to.r - from.r) / (double)message.length();
        double gStep = (to.g - from.g) / (double)message.length();
        double bStep = (to.b - from.b) / (double)message.length();
        
        StringBuilder s = new StringBuilder();
        
        double r=from.r,
               g=from.g,
               b=from.b;
        
        for(int i=0;i<message.length();++i)
        {
            s.append( "§x§" );s.append( mapHexInt((((int)r) & 0xF0 )>>4) );
            s.append( '§' );  s.append( mapHexInt((((int)r) & 0x0F)) );
            s.append( '§' );  s.append( mapHexInt((((int)g) & 0xF0 )>>4) );
            s.append( '§' );  s.append( mapHexInt((((int)g) & 0x0F)) );
            s.append( '§' );  s.append( mapHexInt((((int)b) & 0xF0 )>>4) );
            s.append( '§' );  s.append( mapHexInt((((int)b) & 0x0F)) );
            s.append( message.charAt( i ) );
            
            r+=rStep;
            g+=gStep;
            b+=bStep;
        }
        
        return s.toString();
    }
    
    private static char mapHexInt(int v)
    {
        return (char)(v + (v < 10 ? 48 : 55));
    }
}
