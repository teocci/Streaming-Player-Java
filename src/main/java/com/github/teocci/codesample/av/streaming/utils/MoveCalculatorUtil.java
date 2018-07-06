package com.github.teocci.codesample.av.streaming.utils;

/**
 * Movement calculation on the pane
 * <p>
 * eq: (x-j)^2 + (y-k)^2 = r^2
 * <p>
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class MoveCalculatorUtil
{
    public static double resetToCenterX(double x, double center)
    {
        return x - center;
    }

    public static double resetToCenterY(double y, double center)
    {
        return center - y;
    }

    public static double determineAngle(double x, double y)
    {
        double rad = Math.atan2(y, x);
        return rad + Math.PI / 2;
    }

    public static boolean isCircleArea(double radius, double x, double y)
    {
        return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5) < radius;
    }

    public static boolean isCircleArea(double radius, double x, double y, double j, double k)
    {
        return Math.pow(Math.pow(x - j, 2) + Math.pow(y - k, 2), 0.5) > radius;
    }

    // Private Method
    private static double determineRadius(double x, double y)
    {
        return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
    }
}
