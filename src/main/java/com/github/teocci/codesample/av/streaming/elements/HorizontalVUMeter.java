package com.github.teocci.codesample.av.streaming.elements;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-22
 */
public class HorizontalVUMeter extends Parent
{
    private final int BARS = 48;

    private final Color BASE_BAR_COLOR = Color.web("#dedfe0");
    private final Color LOW_AMP_BAR_COLOR = Color.web("#689f38");
    private final Color HIGH_AMP_BAR_COLOR = Color.web("#fbc02d");
    private final Color PEEK_BAR_COLOR = Color.web("#cf0f0f");

    private final Color SHADOW_COLOR = Color.web("#b10000");

    private Rectangle[] bars = new Rectangle[BARS];
    private DoubleProperty value = new SimpleDoubleProperty(0)
    {
        @Override
        protected void invalidated()
        {
            super.invalidated();
            double lastBar = get() * bars.length;
            for (int i = 0; i < bars.length; i++) {
                updateFill(i, i < lastBar);
//                bars[i].setVisible(i < lastBar);
//                System.out.println("bars[" + i + "] ? " + lastBar + "(" + (i < lastBar) + ")");
            }
        }
    };

    private void updateFill(int index, boolean color)
    {
        if (color) {
            switch (range(index)) {
                case 0:
                    bars[index].setFill(LOW_AMP_BAR_COLOR);
                    break;
                case 1:
                    bars[index].setFill(HIGH_AMP_BAR_COLOR);
                    break;
                case 2:
                    bars[index].setFill(PEEK_BAR_COLOR);
                    break;
            }
        } else {
            bars[index].setFill(BASE_BAR_COLOR);
        }
    }

    private int range(int num)
    {
        if ((BARS * 0.6) < num && num < (BARS * 0.8))
            return 1;
        if ((BARS * 0.8) <= num && num < BARS)
            return 2;
        return 0;
    }

    public void setValue(double v) { value.set(v); }

    public double getValue() { return value.get(); }

    public DoubleProperty valueProperty() { return value; }

    public HorizontalVUMeter()
    {
        for (int i = 0; i < bars.length; i++) {
            bars[i] = new Rectangle(2, 18);
            bars[i].setFill(BASE_BAR_COLOR);

            bars[i].setX(1 + (i * 4));
            bars[i].setY(-9);
        }
        setTranslateX(4);
        getChildren().addAll(bars);
//        setEffect(DropShadowBuilder.create().blurType(TWO_PASS_BOX).radius(10).spread(0.4).color(SHADOW_COLOR).build());
    }
}
