package com.github.teocci.codesample.av.streaming.media;

import javafx.scene.control.Slider;

import javax.sound.sampled.FloatControl;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-14
 */
public class VolumeSlider
{
    private Slider volume;

    public VolumeSlider(final FloatControl volumeControl)
    {
        volume = new Slider(
                (int) volumeControl.getMinimum() * 100,
                (int) volumeControl.getMaximum() * 100,
                (int) volumeControl.getValue() * 100
        );

        volume.valueProperty().addListener((ov, old_val, new_val) -> {
            float val = (float) volume.getValue() / 100;
            volumeControl.setValue(val);
            System.out.println(
                    "Setting volume of " + volumeControl.toString() +
                            " to " + val);
        });
    }

    public void setXY(int x, int y) {
        volume.setLayoutX(x);
        volume.setLayoutY(y);
    }

    public Slider getVolume()
    {
        return volume;
    }
}