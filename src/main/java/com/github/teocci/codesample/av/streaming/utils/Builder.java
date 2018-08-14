package com.github.teocci.codesample.av.streaming.utils;

import java.util.function.Consumer;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Aug-01
 */
public class Builder
{
    public static <T> T build(T node, Consumer<T> initializer)
    {
        initializer.accept(node);
        return node;
    }
}
