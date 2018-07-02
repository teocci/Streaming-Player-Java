package com.github.teocci.codesample.av.streaming.utils;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-28
 */
public class Utils
{
    public static String getCharForNumber(int i)
    {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 'A' - 1)) : null;
    }
}
