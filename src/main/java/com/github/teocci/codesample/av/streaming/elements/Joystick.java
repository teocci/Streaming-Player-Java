package com.github.teocci.codesample.av.streaming.elements;

import com.github.teocci.codesample.av.streaming.controllers.JoystickController;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import static com.github.teocci.codesample.av.streaming.utils.Config.IMAGE_HANDLER;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class Joystick extends Group
{
    private static final String TAG = LogHelper.makeLogTag(Joystick.class);

    public static final int MULTIPLIER = 2;
    public static final int POV_RADIUS_DIVIDER = 3;

    private Canvas canvas;
    private Circle pov;

    private IntegerProperty levelsProperty;
    private DoubleProperty radiusProperty;

    public Joystick(final double radius, final int levelNumber)
    {
        levelsProperty = new SimpleIntegerProperty(levelNumber);
        radiusProperty = new SimpleDoubleProperty(radius);

        pov = new Circle(0, Color.YELLOW);
        pov.setFill(new ImagePattern(new Image(IMAGE_HANDLER)));
//        int size = calculateSize(radius);
//        LogHelper.e(TAG, "size: " + size);
//        canvas = new Canvas(size, size);

        canvas = new Canvas(radius, radius);
        getChildren().addAll(canvas, pov);
        new JoystickController(this);
    }

    public static double calculateSize(double radius)
    {
        return calculateRadius(radius) * MULTIPLIER;
    }

    public static double calculateRadius(double radius)
    {
        return (radius + radius / POV_RADIUS_DIVIDER);
    }

    public Circle getPov()
    {
        return pov;
    }

    public Canvas getCanvas()
    {
        return canvas;
    }

    public int getLevels()
    {
        return levelsProperty.get();
    }

    public double getRadius()
    {
        return radiusProperty.get();
    }

    public DoubleProperty radiusProperty()
    {
        return radiusProperty;
    }

    public IntegerProperty levelsProperty()
    {
        return levelsProperty;
    }
}