package com.github.teocci.codesample.av.streaming.interfaces;

import javafx.scene.image.Image;

import javax.sound.sampled.FloatControl;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-14
 */
public interface GrabberListener
{
    void onMediaGrabbed(int width, int height);

    void onImageProcessed(Image image);

    void onPlaying();

    void onStop();

    void onGainControl(FloatControl gainControl);

    void onAudioSpectrum(float amplitude, float magnitude);

    void onError(Exception e);
}
