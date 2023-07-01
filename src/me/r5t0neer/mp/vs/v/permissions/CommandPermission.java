package me.r5t0neer.mp.vs.v.permissions;

import java.util.List;
import java.util.regex.Pattern;



public class CommandPermission
{
    private final ArgNode args;
    
    public CommandPermission(ArgNode args)
    {
        this.args = args;
    }
    
    public boolean hasAccessToArgsString(List<String> args)
    {
        if(args.isEmpty())
            return true;
        
        ArgNode node = this.args;
        int i=0;
        
        if(node.matchPattern.matcher( args.get( i ) ).matches())
        {
            boolean found;
            String arg;
            ArgNode subNode;
            int j, sz;
            
            while(true)
            {
                ++i;
                found = false;
                arg = args.get( i );
                
                for( j=0, sz = node.subArgs.size(); j < sz; ++j )
                {
                    subNode = node.subArgs.get(j);
                    
                    if(subNode.matchPattern.matcher( arg ).matches())
                    {
                        found = true;
                        node = subNode;
                        break;
                    }
                }
                
                if(!found)
                {
                    return node.subArgs.isEmpty();
                }
            }
        }
        
        return false;
    }
    
    static class ArgNode
    {
        public final Pattern matchPattern;
        public final List<ArgNode> subArgs;// null = all, empty = none
    
        ArgNode(String matchPattern, List<ArgNode> subArgs)
        {
            this.matchPattern = Pattern.compile( matchPattern );
            this.subArgs = subArgs;
        }
    }
}
