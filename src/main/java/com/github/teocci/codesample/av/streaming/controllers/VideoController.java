package com.github.teocci.codesample.av.streaming.controllers;

import com.github.teocci.codesample.av.streaming.interfaces.GrabberListener;
import com.github.teocci.codesample.av.streaming.media.SimplePlayer;
import com.github.teocci.codesample.av.streaming.models.WindowInfo;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import com.github.teocci.codesample.av.streaming.views.VideoView;
import javafx.scene.Parent;
import javafx.scene.image.Image;

import javax.sound.sampled.FloatControl;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-26
 */
public class VideoController implements GrabberListener
{
    private static final String TAG = LogHelper.makeLogTag(VideoController.class);

    private MainController controller;
    private WindowInfo windowInfo;

    private VideoView view;

    private SimplePlayer simplePlayer;

    public VideoController(MainController controller, WindowInfo windowInfo) throws NullPointerException
    {
        if (controller == null) throw new NullPointerException("MainController is null");
        if (windowInfo == null) return;

        this.controller = controller;
        this.windowInfo =  windowInfo;

        init();
    }

    @Override
    public void onMediaGrabbed(int width, int height)
    {
        view.updateVideoFrame(width, height);
    }

    @Override
    public void onImageProcessed(Image image)
    {
//        LogHelper.e(TAG, "image: " + image);
        view.updateImage(image);
        controller.updateVideoPane(windowInfo, image);
    }

    @Override
    public void onPlaying() {}

    @Override
    public void onStop()
    {
        controller.closeWindow(windowInfo.getId(), null);
    }

    @Override
    public void onGainControl(FloatControl gainControl) {}

    @Override
    public void onAudioSpectrum(float amplitude, float magnitude) {}

    @Override
    public void onError(Exception e)
    {
        controller.closeWindow(windowInfo.getId(), e);
    }

    private void init()
    {
        view = new VideoView(this);
        simplePlayer = new SimplePlayer(windowInfo.getSource(), this);
    }

    public VideoView getView()
    {
        return view;
    }

    public Parent getViewAsParent()
    {
        return view.asParent();
    }

    public WindowInfo getWindowInfo()
    {
        return windowInfo;
    }

    public String getWindowInfoId()
    {
        return windowInfo.getId();
    }

    public void stopStream()
    {
        LogHelper.e(TAG, "stopStream");
        simplePlayer.stop();
        view.closeStage();
    }

    public void stageToFront()
    {
        view.stageToFront();
    }
}
