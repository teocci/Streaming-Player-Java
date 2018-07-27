package com.github.teocci.codesample.av.streaming.views;

import com.github.teocci.codesample.av.streaming.controllers.VideoController;
import com.github.teocci.codesample.av.streaming.models.WindowInfo;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-26
 */
public class VideoView
{
    private static final String TAG = LogHelper.makeLogTag(VideoView.class);

    private VideoController controller;

    private final WindowInfo windowInfo;

    private StackPane root = new StackPane();
    private Canvas canvas = new Canvas();

    private Stage stage = new Stage();
    private Scene scene;

    private GraphicsContext graphicsContext;

    public VideoView(VideoController controller)
    {
        this.controller = controller;
        this.windowInfo = controller.getWindowInfo();

        stage.setTitle(windowInfo.title);

        scene = new Scene(root, 1030, 600);
        scene.getStylesheets().add("css/style.css");

        graphicsContext = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        root.getChildren().add(canvas);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(e -> {
            LogHelper.e(TAG, "Stage is closing");
            controller.stopStream();
        });
    }

    public Parent asParent()
    {
        return root;
    }

    public void updateImage(Image image)
    {
        Platform.runLater(() -> {
            graphicsContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
        });
    }

    public void updateVideoFrame(int width, int height)
    {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public void setScene(Scene scene)
    {
        this.scene = scene;
    }

    public Scene getScene()
    {
        return scene;
    }

    public void closeStage()
    {
        Platform.runLater(()->{
            if (stage.isShowing()) {
                LogHelper.e(TAG, "closeStage");
                stage.close();
            }
        });
    }

    public void stageToFront()
    {
//        Platform.runLater(()->{
//        });

        if (stage.isShowing()) {
            LogHelper.e(TAG, "stageToFront");
            stage.toFront();
        }
    }

    public VideoController getController()
    {
        return controller;
    }
}
