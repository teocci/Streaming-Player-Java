package com.github.teocci.codesample.av.streaming;

import com.github.teocci.codesample.av.streaming.interfaces.GrabberListener;
import com.github.teocci.codesample.av.streaming.media.SimplePlayer;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.sound.sampled.FloatControl;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-01
 */
public class StreamingVideoClient extends Application implements GrabberListener
{
    private static final String TAG = LogHelper.makeLogTag(StreamingVideoClient.class);

    public static void main(String[] args)
    {
        launch(args);
    }

    private Stage primaryStage;
    private Canvas canvas;
    private ImageView imageView;

    private GraphicsContext graphicsContext;

    private SimplePlayer simplePlayer;

    @Override
    public void start(Stage stage) throws Exception
    {
//        String source = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"; // the video is weird for 1 minute then becomes stable
        String source = "rtsp://192.168.20.11:8086"; // the SmartCam app the video is bad

//        String source = "http://192.168.1.215:8080/video";
//        String source = "rtsp://192.168.1.215:8086";

        primaryStage = stage;
        canvas = new Canvas();

        graphicsContext = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();

        root.getChildren().add(canvas);
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());

        Scene scene = new Scene(root, 640, 480);

        primaryStage.setTitle("Streaming Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        simplePlayer = new SimplePlayer(source, this);
    }

    @Override
    public void onMediaGrabbed(int width, int height)
    {
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }

    @Override
    public void onImageProcessed(Image image)
    {
        LogHelper.e(TAG, "image: " + image);

        Platform.runLater(() -> {
//            imageView.setImage(image);
            graphicsContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
        });
    }

    @Override
    public void onPlaying()
    {

    }

    @Override
    public void onGainControl(FloatControl gainControl)
    {

    }

    @Override
    public void stop() throws Exception
    {
        simplePlayer.stop();
    }
}
