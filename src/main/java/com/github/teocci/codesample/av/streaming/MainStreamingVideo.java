package com.github.teocci.codesample.av.streaming;

import com.github.teocci.codesample.av.streaming.controllers.MainController;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-26
 */
public class MainStreamingVideo extends Application
{
    private static final String TAG = LogHelper.makeLogTag(MainStreamingVideo.class);

    private MainController controller;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        controller = new MainController(stage);
//        controller.setScene(scene);

//        primaryStage.setOnCloseRequest(e -> {
//            Platform.exit();
//            System.exit(0);
//        });
    }

    @Override
    public void stop() throws Exception
    {
        LogHelper.e(TAG, "stop");
        controller.closeAllWindows();
    }
}
