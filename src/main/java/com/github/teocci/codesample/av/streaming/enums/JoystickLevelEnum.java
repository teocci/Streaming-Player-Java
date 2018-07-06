package com.github.teocci.codesample.av.streaming.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public enum JoystickLevelEnum
{
    NONE(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3);

    private volatile static Map<Integer, JoystickLevelEnum> internalMap;

    private int level;

    JoystickLevelEnum(int l)
    {
        this.level = l;
    }

    public static JoystickLevelEnum getJoystickLevelByCode(int code)
    {
        if (internalMap == null) {
            internalMap = initMapping();
        }

        return internalMap.get(code);
    }

    // Private Methods
    private static Map<Integer, JoystickLevelEnum> initMapping()
    {
        return Arrays.asList(values()).stream().map(e -> new Map.Entry<Integer, JoystickLevelEnum>()
        {
            @Override
            public Integer getKey()
            {
                return e.getLevel();
            }

            @Override
            public JoystickLevelEnum getValue()
            {
                return e;
            }

            @Override
            public JoystickLevelEnum setValue(JoystickLevelEnum value)
            {
                return null;
            }
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int getLevel()
    {
        return level;
    }

    @Override
    public String toString()
    {
        return "JoystickLevelEnum{" + "level=" + level + '}';
    }
}
