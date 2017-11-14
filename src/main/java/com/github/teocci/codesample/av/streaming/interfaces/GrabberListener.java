package com.github.teocci.codesample.av.streaming.interfaces;

import javafx.scene.image.Image;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-14
 */
public interface GrabberListener
{
    void onMediaGrabbed(int width, int height);

    void onImageProcessed(Image image);
}
