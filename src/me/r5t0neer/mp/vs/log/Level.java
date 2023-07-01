package me.r5t0neer.mp.vs.log;

public enum Level
{
    FINEST, FINER, FINE,
    DEBUG,
    INFO, CONFIG, SUCCESS,
    LISTENER, COMMAND, CHAT,
    WARN,
    ERROR,
    FATAL;

    public static java.util.logging.Level toJLevel(Level level)
    {
        java.util.logging.Level jlevel = java.util.logging.Level.OFF;
        switch(level)
        {
            case FINEST: jlevel=java.util.logging.Level.FINEST; break;
            case FINER: jlevel=java.util.logging.Level.FINER; break;
            case FINE: jlevel=java.util.logging.Level.FINE; break;
            case DEBUG: jlevel=java.util.logging.Level.ALL; break;
            case INFO: jlevel=java.util.logging.Level.INFO; break;
            case CONFIG: jlevel=java.util.logging.Level.CONFIG; break;
            case SUCCESS: jlevel=java.util.logging.Level.INFO; break;
            case LISTENER: jlevel=java.util.logging.Level.WARNING; break;
            case COMMAND: jlevel=java.util.logging.Level.WARNING; break;
            case CHAT: jlevel=java.util.logging.Level.INFO; break;
            case WARN: jlevel=java.util.logging.Level.WARNING; break;
            case ERROR: jlevel=java.util.logging.Level.SEVERE; break;
            case FATAL: jlevel=java.util.logging.Level.SEVERE; break;
        }

        return jlevel;
    }
}
