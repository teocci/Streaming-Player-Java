package com.github.teocci.codesample.av.streaming.handlers;

import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import com.github.teocci.codesample.av.streaming.utils.MoveCalculatorUtil;

import javafx.beans.property.DoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class PovEventHandler
{
    private static final String TAG = LogHelper.makeLogTag(PovEventHandler.class);

    private static final double MULTIPLIER = 2 / 3;

    public void bindPovToCanvas(Circle pov, DoubleProperty povCenterXProperty, DoubleProperty povCenterYProperty)
    {
        pov.setCenterX(povCenterXProperty.get());
        pov.setCenterY(povCenterYProperty.get());
//        LogHelper.e(TAG, "bindPovToCanvas(" + pov.getCenterX() + ", " + pov.getCenterY() + ")");
    }

    public void draggedPov(MouseEvent e, Circle pov, DoubleProperty radiusProperty, DoubleProperty povCenterXProperty,
                           DoubleProperty povCenterYProperty)
    {
        DoubleProperty povRadius = pov.radiusProperty();

        double mouseX = e.getX();
        double mouseY = e.getY();

        double povCenterX = povCenterXProperty.get();
        double povCenterY = povCenterYProperty.get();

//        LogHelper.e(TAG, "mouse(" + mouseX + ", " + mouseY + ")");
//        LogHelper.e(TAG, "povCenter(" + povCenterX + ", " + povCenterY + ")");

        double radius = radiusProperty.get() - povRadius.get();

        if (MoveCalculatorUtil.isCircleArea(radius, mouseX, mouseY, povCenterX, povCenterY)) {
            double x = MoveCalculatorUtil.resetToCenterX(mouseX, povCenterXProperty.get());
            double y = MoveCalculatorUtil.resetToCenterY(mouseY, povCenterYProperty.get());

            double angle = MoveCalculatorUtil.determineAngle(x, y);

            pov.setCenterX(determinePointXOnCircleCircumference(angle, radius, povCenterX));
            pov.setCenterY(determinePointYOnCircleCircumference(angle, radius, povCenterY));
            return;
        }

        pov.setCenterX(mouseX);
        pov.setCenterY(mouseY);
    }

    public double determinePointXOnCircleCircumference(double angle, double radius, double center)
    {
        return Math.sin(angle) * radius + center;
    }

    public double determinePointYOnCircleCircumference(double angle, double radius, double center)
    {
        return Math.cos(angle) * radius + center;
    }
}
