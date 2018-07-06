package com.github.teocci.codesample.av.streaming.views;

import com.github.teocci.codesample.av.streaming.controllers.MainCamController;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class MainCamView
{
    private static final String TAG = LogHelper.makeLogTag(MainView.class);

    private MainCamController controller;

    private Stage primaryStage;
    private Scene scene;

    private StackPane root = new StackPane();

    public MainCamView(MainCamController controller, Stage stage)
    {
        this.controller = controller;

        primaryStage = stage;
        primaryStage.setTitle("Main Stream Video");

        scene = new Scene(root, 1030, 600);
        scene.getStylesheets().add("css/style.css");

        initElements();
        initHandlers();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initElements()
    {

    }

    private void initHandlers()
    {
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
