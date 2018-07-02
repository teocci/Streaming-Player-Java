package com.github.teocci.codesample.av.streaming.media;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import javax.sound.sampled.FloatControl;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-14
 */
public class VolumeSlider
{
    private VBox control = new VBox(5);

    private float min;
    private float max;

    public VolumeSlider(final FloatControl volumeControl)
    {
        min = volumeControl.getMinimum();
        max = volumeControl.getMaximum();

        Slider volume = new Slider(toPercentage(min), toPercentage(max), toPercentage(volumeControl.getValue()));
        volume.setMaxWidth(200);
        volume.setMinWidth(200);

        volume.setShowTickLabels(true);
        volume.setShowTickMarks(true);

        volume.setBlockIncrement(10);

        Label label = new Label("Value: " + (int) volume.getValue());

        control.getChildren().addAll(label, volume);

        volume.valueProperty().addListener((ov, old_val, new_val) -> {
            int raw = (int) volume.getValue();
            System.out.println("raw: " + raw);
            float val = toValue(raw);
            System.out.println("toValue: " + val);
            volumeControl.setValue(val);
            label.setText("Value: " + raw);
            System.out.println("Setting volume of " + volumeControl.toString() + " to " + val);
        });
    }

    private int toPercentage(float val)
    {
//        System.out.println("val: " + val + " min: " + min + " max: " + max);
//        System.out.println("val - min: " + (val - min) + " max - min: " + (max - min));
        return (int) ((val - min) / (max - min) * 100);
    }


    private float toValue(int val)
    {
        return ((val * (max - min) / 100 + min));
    }

    public void setXY(int x, int y)
    {
        control.setLayoutX(x);
        control.setLayoutY(y);
    }

    public VBox getControl()
    {
        return control;
    }
}