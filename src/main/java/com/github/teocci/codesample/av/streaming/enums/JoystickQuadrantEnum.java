package com.github.teocci.codesample.av.streaming.enums;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public enum JoystickQuadrantEnum
{
    NONE(0, 0, 0),
    QUADRANT_I(1, 338, 45),
    QUADRANT_II(2, 23, 45),
    QUADRANT_III(3, 68, 45),
    QUADRANT_IV(4, 113, 45),
    QUADRANT_V(1, 158, 45),
    QUADRANT_VI(2, 203, 45),
    QUADRANT_VII(3, 248, 45),
    QUADRANT_VIII(4, 293, 45);

    private int quadrant;
    private int startAngle;
    private int angleExtend;

    JoystickQuadrantEnum(int quadrant, int startAngle, int angleExtend)
    {
        this.quadrant = quadrant;
        this.startAngle = startAngle;
        this.angleExtend = angleExtend;
    }

    public int getQuadrant()
    {
        return quadrant;
    }

    public int getAngleExtend()
    {
        return angleExtend;
    }

    public int getStartAngle()
    {
        return startAngle;
    }

    public static JoystickQuadrantEnum calculateQuadrant(int angle)
    {
        JoystickQuadrantEnum quadrantEnum = JoystickQuadrantEnum.NONE;

        if (angle < QUADRANT_II.getStartAngle() || angle >= QUADRANT_I.getStartAngle()) {
            quadrantEnum = JoystickQuadrantEnum.QUADRANT_I;
        }
        if (angle < QUADRANT_III.getStartAngle() && angle >= QUADRANT_II.getStartAngle()) {
            quadrantEnum = JoystickQuadrantEnum.QUADRANT_II;
        }
        if (angle < QUADRANT_IV.getStartAngle() && angle >= QUADRANT_III.getStartAngle()) {
            quadrantEnum = JoystickQuadrantEnum.QUADRANT_III;
        }
        if (angle < QUADRANT_V.getStartAngle() && angle >= QUADRANT_IV.getStartAngle()) {
            quadrantEnum = JoystickQuadrantEnum.QUADRANT_IV;
        }
        if (angle < QUADRANT_VI.getStartAngle() && angle >= QUADRANT_V.getStartAngle()) {
            quadrantEnum = JoystickQuadrantEnum.QUADRANT_V;
        }
        if (angle < QUADRANT_VII.getStartAngle() && angle >= QUADRANT_VI.getStartAngle()) {
            quadrantEnum = JoystickQuadrantEnum.QUADRANT_VI;
        }
        if (angle < QUADRANT_VIII.getStartAngle() && angle >= QUADRANT_VII.getStartAngle()) {
            quadrantEnum = JoystickQuadrantEnum.QUADRANT_VII;
        }
        if (angle < QUADRANT_I.getStartAngle() && angle >= QUADRANT_VIII.getStartAngle()) {
            quadrantEnum = JoystickQuadrantEnum.QUADRANT_VIII;
        }

        return quadrantEnum;
    }

    @Override
    public String toString()
    {
        return "QuadrantEnum{" + "quadrant=" + quadrant +
                ", startAngle=" + startAngle +
                ", angleExtend=" + angleExtend + '}';
    }
}
