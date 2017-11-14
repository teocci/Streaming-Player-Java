package com.github.teocci.codesample.av.streaming;

import com.github.teocci.codesample.av.streaming.interfaces.GrabberListener;
import com.github.teocci.codesample.av.streaming.media.SimplePlayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-01
 */
public class StreamingClient extends Application implements GrabberListener
{
    private static final Logger LOG = Logger.getLogger(StreamingClient.class.getName());

    public static void main(String[] args)
    {
        launch(args);
    }

    private Stage primaryStage;
    private Canvas canvas;

    private GraphicsContext graphicsContext;
    private PixelWriter pixelWriter;
    private WritablePixelFormat<ByteBuffer> pixelFormat;

    private SimplePlayer simplePlayer;

    @Override
    public void start(Stage stage) throws Exception
    {
        String source = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"; // the sound is weird
//        String source = "rtsp://192.168.1.113:8086"; // the SmartCam app the video is bad

//        String source = "http://192.168.1.215:8080/video";
//        String source = "rtsp://192.168.1.215:8086";

        primaryStage = stage;
        canvas = new Canvas();

        graphicsContext = canvas.getGraphicsContext2D();
        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        pixelFormat = PixelFormat.getByteBgraInstance();

        StackPane root = new StackPane();

        root.getChildren().add(canvas);
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());

        Scene scene = new Scene(root, 640, 480);

        primaryStage.setTitle("Video + Audio");
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
        System.out.println("image: " + image);
        graphicsContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
    }

    @Override
    public void stop() throws Exception
    {
        simplePlayer.stop();
    }
}
