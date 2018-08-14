package com.github.teocci.codesample.av.streaming.samples;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-31
 */
public class SectionBuilder
{
    private HashMap<String, Property> properties = new HashMap<>();

    // ******************** Constructors **************************************
    protected SectionBuilder() {}

    // ******************** Methods *******************************************
    public static final SectionBuilder create()
    {
        return new SectionBuilder();
    }

    public final SectionBuilder start(final double START)
    {
        properties.put("start", new SimpleDoubleProperty(START));
        return this;
    }

    public final SectionBuilder stop(final double STOP)
    {
        properties.put("stop", new SimpleDoubleProperty(STOP));
        return this;
    }

    public final SectionBuilder text(final String TEXT)
    {
        properties.put("text", new SimpleStringProperty(TEXT));
        return this;
    }

    public final SectionBuilder icon(final Image IMAGE)
    {
        properties.put("icon", new SimpleObjectProperty<>(IMAGE));
        return this;
    }

    public final SectionBuilder styleClass(final String STYLE_CLASS)
    {
        properties.put("styleClass", new SimpleStringProperty(STYLE_CLASS));
        return this;
    }

    public final Section build()
    {
        final Section SECTION = new Section();
        for (String key : properties.keySet()) {
            if ("start".equals(key)) {
                SECTION.setStart(((DoubleProperty) properties.get(key)).get());
            } else if ("stop".equals(key)) {
                SECTION.setStop(((DoubleProperty) properties.get(key)).get());
            } else if ("text".equals(key)) {
                SECTION.setText(((StringProperty) properties.get(key)).get());
            } else if ("icon".equals(key)) {
                SECTION.setIcon(((ObjectProperty<Image>) properties.get(key)).get());
            } else if ("styleClass".equals(key)) {
                SECTION.setStyleClass(((StringProperty) properties.get(key)).get());
            }
        }
        return SECTION;
    }
}