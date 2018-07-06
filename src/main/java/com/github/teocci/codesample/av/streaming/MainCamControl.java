package com.github.teocci.codesample.av.streaming;

import com.github.teocci.codesample.av.streaming.controllers.MainCamController;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class MainCamControl extends Application
{
    private static final String TAG = LogHelper.makeLogTag(MainCamControl.class);

    private MainCamController controller;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        controller = new MainCamController(stage);
    }
}
