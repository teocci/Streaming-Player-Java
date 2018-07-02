package com.github.teocci.codesample.av.streaming.models;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-27
 */
public class WindowInfo
{
    public String id;
    public String title;
    private String source;

    public WindowInfo(String id, String title)
    {
        this.id = id;
        this.title = title;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }
}
