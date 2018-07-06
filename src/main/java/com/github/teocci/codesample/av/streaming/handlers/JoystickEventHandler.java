package com.github.teocci.codesample.av.streaming.handlers;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.teocci.codesample.av.streaming.elements.Joystick;
import com.github.teocci.codesample.av.streaming.enums.JoystickLevelEnum;
import com.github.teocci.codesample.av.streaming.events.JoystickEvent;

import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class JoystickEventHandler
{
    private static final String TAG = LogHelper.makeLogTag(JoystickEventHandler.class);

    private static final int ANGLE_360 = 360;
    private static final int MULTIPLIER = 2;

    /**
     * Ratio use to define the size of the pov
     */
    private static final double RATIO_SIZE_POV = 0.30;

    /**
     * Ratio use to define the size of border (as the distance from the center)
     */
    private static final double RATIO_SIZE_PIVOT = 0.70;

    public void onMouseDragged(Circle pov, DoubleProperty povCenterXProperty, DoubleProperty povCenterYProperty)
    {
        pov.setCenterX(povCenterXProperty.get());
        pov.setCenterY(povCenterYProperty.get());
    }

    public void setOnZoom(ZoomEvent e, DoubleProperty radiusProperty)
    {
        radiusProperty.set(radiusProperty.get() * e.getZoomFactor());
    }

    public void drawTargetLevel(JoystickEvent e, Canvas canvas, DoubleProperty radiusProperty, Map<Integer, IntegerProperty> levels)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawJoystick(canvas, radiusProperty, levels, levels.size());

        double position = Joystick.calculateRadius(radiusProperty.get()) - levels.get(getJoystickLevel(e.getJoystickLevel())).get();
        int size = levels.get(getJoystickLevel(e.getJoystickLevel())).get() * MULTIPLIER;

        // Quadrant painter
        gc.setFill(Color.web("#4FA4A9"));
        gc.fillArc(
                position,
                position,
                size,
                size,
                e.getQuadrant().getStartAngle(),
                e.getQuadrant().getAngleExtend(),
                ArcType.ROUND
        );
        drawLevels(canvas, radiusProperty, levels, e.getJoystickLevel().getLevel());
        drawPivot(canvas, levels, radiusProperty, 0, 0, levels.size());
    }

    public void drawJoystick(Canvas canvas, DoubleProperty radiusProperty, Map<Integer, IntegerProperty> levels, int levelNumber)
    {
//        LogHelper.e(TAG, "drawJoystick()");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawSquare(canvas, 0, 0, canvas.getWidth(), canvas.getHeight());
        drawLevels(canvas, radiusProperty, levels, levelNumber);
        drawPivot(canvas, levels, radiusProperty, 0, 0, levelNumber);
    }

    public void drawSquare(Canvas canvas, Map<Integer, IntegerProperty> levels, double x, double y, double sizeX, double sizeY)
    {
//        LogHelper.e(TAG, "drawSquare()");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, sizeX, sizeY);
    }

    public void drawPivot(Canvas canvas, Map<Integer, IntegerProperty> levels, DoubleProperty radiusProperty, double x, double y, int levelNumber)
    {
        if (levelNumber == 1) {
            LogHelper.e(TAG, "drawPivot(levelNumber) -> (" + levelNumber + ")");
            GraphicsContext gc = canvas.getGraphicsContext2D();
            Stream<Map.Entry<Integer, IntegerProperty>> sortedLevels = levels
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey() < levelNumber)
                    .sorted((l, r) -> Integer.compare(l.getValue().get(), r.getValue().get()));

            Optional<Map.Entry<Integer, IntegerProperty>> level = sortedLevels.findFirst();
            LogHelper.e(TAG, "drawPivot(level) -> (" + level.get() + ")");

            if (level.isPresent() && level.get().getKey() == 0) {
                double size = level.get().getValue().get() * MULTIPLIER * RATIO_SIZE_PIVOT;
                double position = Joystick.calculateRadius(radiusProperty.get()) - size / MULTIPLIER;
                LogHelper.e(TAG, "(position, size) -> (" + position + ", " + size + ")");
                gc.setStroke(Color.web("#818181"));
                gc.strokeArc(position, position, size, size, 0, ANGLE_360, ArcType.CHORD);

                gc.setFill(Color.web("#2F2F2F"));
                gc.fillOval(position, position, size, size);
            }
        }
    }

    public void drawLevels(Canvas canvas, DoubleProperty radiusProperty, Map<Integer, IntegerProperty> levels, int levelNumber)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Stream<Map.Entry<Integer, IntegerProperty>> sortedLevels = levels
                .entrySet()
                .stream()
                .filter(e -> e.getKey() < levelNumber)
                .sorted((l, r) -> Integer.compare(r.getValue().get(), l.getValue().get()));

        sortedLevels.forEach(i -> {
            switch (i.getKey()) {
                case 0:
                    if (levels.size() == 1) {
                        gc.setFill(Color.web("#E9E9E9"));
                    } else {
                        gc.setFill(Color.DARKGREY);
                    }
                    break;
                case 1:
                    gc.setFill(Color.ROYALBLUE);
                    break;
                case 2:
                    gc.setFill(Color.STEELBLUE);
                    break;
                case 3:
                    gc.setFill(Color.SKYBLUE);
                    break;
            }
            double position = Joystick.calculateRadius(radiusProperty.get()) - i.getValue().get();

//            LogHelper.e(TAG, "position: " + position + " level" + i.getKey());
            int size = i.getValue().get() * MULTIPLIER;
            gc.fillOval(position, position, size, size);

            gc.setStroke(Color.web("#CACACA"));
            gc.strokeArc(position, position, size, size, 0, ANGLE_360, ArcType.CHORD);
        });
    }

    // Private Methods
    private int getJoystickLevel(JoystickLevelEnum levelEnum)
    {
        return levelEnum.getLevel();
    }
}
