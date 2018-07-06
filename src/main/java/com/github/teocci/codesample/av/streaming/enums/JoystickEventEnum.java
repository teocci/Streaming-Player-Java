package com.github.teocci.codesample.av.streaming.enums;

import com.github.teocci.codesample.av.streaming.events.JoystickEvent;

import javafx.event.EventType;
import javafx.scene.input.InputEvent;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public enum JoystickEventEnum
{
    UNDEFINED(1, new EventType<>(InputEvent.ANY, "POV")),
    POV_MOVED(2, new EventType<>(InputEvent.ANY, "POV_MOVED")),
    QUADRANT_CHANGED(2, new EventType<>(InputEvent.ANY, "POV_QUADRANT_CHANGED")),
    LEVEL_CHANGED(3, new EventType<>(InputEvent.ANY, "POV_LEVEL_CHANGED"));

    private final int id;
    private final EventType<JoystickEvent> eventType;

    JoystickEventEnum(int id, EventType<JoystickEvent> eventType)
    {
        this.id = id;
        this.eventType = eventType;
    }

    public int getId()
    {
        return id;
    }

    public EventType<JoystickEvent> getEventType()
    {
        return eventType;
    }

    @Override
    public String toString()
    {
        return "JoystickEventEnum{" + "id=" + id + ", eventType=" + eventType + '}';
    }
}
