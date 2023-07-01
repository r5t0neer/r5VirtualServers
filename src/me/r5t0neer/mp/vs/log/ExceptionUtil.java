package me.r5t0neer.mp.vs.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;



public class ExceptionUtil
{
    public static String convertStackTraceToString(Throwable throwable)
    {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw))
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
        catch (IOException ioe)
        {
            throw new IllegalStateException(ioe);
        }
    }
}
