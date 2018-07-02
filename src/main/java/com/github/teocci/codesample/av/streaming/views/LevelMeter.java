package com.github.teocci.codesample.av.streaming.views;

import javafx.scene.shape.Rectangle;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-21
 */
public class LevelMeter extends Rectangle
{
    private int meterWidth = 10;

    private float amp = 0f;
    private float peak = 0f;

    public void setAmplitude(float amp) {
        this.amp = Math.abs(amp);
//        repaint();
    }

    public void setPeak(float peak) {
        this.peak = Math.abs(peak);
//        repaint();
    }

    public void setMeterWidth(int meterWidth) {
        this.meterWidth = meterWidth;
    }
}
