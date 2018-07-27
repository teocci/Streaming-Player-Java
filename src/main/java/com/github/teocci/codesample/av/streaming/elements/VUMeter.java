package com.github.teocci.codesample.av.streaming.elements;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static javafx.scene.effect.BlurType.TWO_PASS_BOX;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-22
 */
public class VUMeter extends Parent
{
    private final Color BASE_BAR_COLOR = Color.web("#689f38");
    private final Color AMP_BAR_COLOR = Color.web("#fbc02d");
    private final Color PEEK_BAR_COLOR = Color.web("#cf0f0f");

    private final Color SHADOW_COLOR = Color.web("#b10000");

    private Rectangle[] bars = new Rectangle[20];
    private DoubleProperty value = new SimpleDoubleProperty(0)
    {
        @Override
        protected void invalidated()
        {
            super.invalidated();
            double lastBar = get() * bars.length;
            for (int i = 0; i < bars.length; i++) {
                bars[i].setVisible(i < lastBar);
//                System.out.println("bars[" + i + "] ? " + lastBar + "(" + (i < lastBar) + ")");
            }
        }
    };

    public VUMeter()
    {
        for (int i = 0; i < bars.length; i++) {
            bars[i] = new Rectangle(26, 2);
            bars[i].setFill(BASE_BAR_COLOR);
            if (i > 11) {
                bars[i].setFill(AMP_BAR_COLOR);
                if (i > 15) {
                    bars[i].setFill(PEEK_BAR_COLOR);
                }
            }
            bars[i].setX(-13);
            bars[i].setY(1 - (i * 4));
        }
        getChildren().addAll(bars);
//        setEffect(DropShadowBuilder.create().blurType(TWO_PASS_BOX).radius(10).spread(0.4).color(SHADOW_COLOR).build());
    }

    public void setValue(double v) { value.set(v); }

    public double getValue() { return value.get(); }

    public DoubleProperty valueProperty() { return value; }
}
