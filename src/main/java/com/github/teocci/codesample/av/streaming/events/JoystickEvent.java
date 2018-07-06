package com.github.teocci.codesample.av.streaming.events;

import com.github.teocci.codesample.av.streaming.enums.JoystickEventEnum;
import com.github.teocci.codesample.av.streaming.enums.JoystickLevelEnum;
import com.github.teocci.codesample.av.streaming.enums.JoystickQuadrantEnum;

import javafx.event.EventTarget;
import javafx.scene.input.InputEvent;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class JoystickEvent extends InputEvent
{
    private static final long serialVersionUID = 2231266847422883646L;

    private transient double x;
    private transient double y;

    private transient int angle;
    private transient int strength;

    private transient JoystickQuadrantEnum quadrant;
    private transient JoystickLevelEnum joystickLevel;

    public JoystickEvent(
            Object source,
            EventTarget target,
            JoystickEventEnum eventType,
            double x,
            double y,
            int angle,
            int strength,
            JoystickQuadrantEnum quadrant,
            JoystickLevelEnum joystickLevel
    )
    {
        super(source, target, eventType.getEventType());
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.strength = strength;
        this.quadrant = quadrant;
        this.joystickLevel = joystickLevel;
    }

    public final double getX()
    {
        return x;
    }

    public final double getY()
    {
        return y;
    }

    public int getAngle()
    {
        return angle;
    }

    public int getStrength()
    {
        return strength;
    }

    public final JoystickQuadrantEnum getQuadrant()
    {
        return quadrant;
    }

    public final JoystickLevelEnum getJoystickLevel()
    {
        return joystickLevel;
    }

    @Override
    public String toString()
    {
        return "JoystickEvent{" + "x=" + x + ", y=" + y +
                ", quadrant=" + quadrant +
                ", joystickLevel=" + joystickLevel + '}';
    }
}