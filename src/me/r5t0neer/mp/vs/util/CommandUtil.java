package me.r5t0neer.mp.vs.util;

import java.util.ArrayList;
import java.util.List;

public class CommandUtil
{
    /*
     * pass cmdString without /
     * @return String[2]
     * */
    public static String[] getCommandNameAndArgsString(String cmdString)
    {
        String[] parts = cmdString.split(" ", 2);

        if(parts.length == 1)
        {
            String[] parts2 = new String[2];
            parts2[0] = parts[0];
            parts2[1] = "";

            return parts2;
        }
        else return parts;
    }

    public static List<String> getSmartArgs(String argsString)
    {
        List<String> args = new ArrayList<>();

        StringBuffer arg = new StringBuffer();
        boolean cancel = false;
        boolean quotes = false;

        for(int i=0;i<argsString.length();++i)
        {
            char c = argsString.charAt(i);

            if(c == ' ')
            {
                if(!quotes)
                {
                    // next arg
                    args.add(arg.toString());
                    arg = new StringBuffer();
                }
                else arg.append(c);
            }
            else if(c == '"' || c == '\'')
            {
                if(!quotes)
                {
                    if(!cancel) quotes = true;
                    else arg.append(c);
                }
                else
                {
                    if(!cancel) quotes = false;
                    else arg.append(c);
                }
            }
            else if(c == '\\')
            {
                if(!cancel) cancel = true;
                else arg.append('\\');
            }
            else
            {
                cancel = false;
                arg.append(c);
            }
        }

        args.add(arg.toString());

        return args;
    }
}
