package com.github.teocci.codesample.av.streaming.exceptions;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class JoystickException extends RuntimeException
{
    public JoystickException(String message)
    {
        super(message);
    }

    public JoystickException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
