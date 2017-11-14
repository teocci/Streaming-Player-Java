package com.github.teocci.codesample.av.streaming;

import com.github.teocci.codesample.av.streaming.interfaces.GrabberListener;
import com.github.teocci.codesample.av.streaming.media.AudioPlayer;
import com.github.teocci.codesample.av.streaming.media.SimplePlayer;
import com.github.teocci.codesample.av.streaming.media.VolumeSlider;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.sound.sampled.FloatControl;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-01
 */
public class StreamingAudioClient extends Application
{
    private static final String TAG = LogHelper.makeLogTag(StreamingAudioClient.class);

    public static void main(String[] args)
    {
        launch(args);
    }

    private Stage primaryStage;
    private ImageView imageView;

    private StackPane root;
    private Scene scene;

    private Pane paneA;
    private Pane paneB;

    private AudioPlayer audioPlayerA;
    private AudioPlayer audioPlayerB;

    private GrabberListener grabberListenerA = new GrabberListener()
    {
        @Override
        public void onMediaGrabbed(int width, int height)
        {
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
        }

        @Override
        public void onImageProcessed(Image image)
        {

        }

        @Override
        public void onPlaying()
        {

        }

        @Override
        public void onGainControl(FloatControl gainControl)
        {
            Platform.runLater(() -> {
                VolumeSlider vs = new VolumeSlider(gainControl);
                paneA.getChildren().add(new Label(gainControl.toString()));
                paneA.getChildren().add(vs.getVolume());
            });
        }
    };

    private GrabberListener grabberListenerB = new GrabberListener()
    {
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
                imageView.setImage(image);
//            graphicsContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
            });
        }

        @Override
        public void onPlaying()
        {

        }

        @Override
        public void onGainControl(FloatControl gainControl)
        {
            Platform.runLater(() -> {
                VolumeSlider vs = new VolumeSlider(gainControl);
                root.getChildren().add(new Label(gainControl.toString()));
                root.getChildren().add(vs.getVolume());
            });
        }
    };

    @Override
    public void start(Stage stage) throws Exception
    {
//        String source = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"; // the video is weird for 1 minute then becomes stable
        String sourceA = "rtsp://192.168.1.113:8086"; // the SmartCam app the video is bad
        String sourceB = "rtsp://192.168.1.215:8086"; // the SmartCam app the video is bad

        primaryStage = stage;
        imageView = new ImageView();

        root = new StackPane();

        root.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());

        scene = new Scene(root, 640, 480);

        primaryStage.setTitle("Streaming Audio Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        paneA = new Pane();
        paneA.layoutXProperty().add(0);
        paneA.layoutXProperty().add(100);
        paneA.widthProperty().add(primaryStage.widthProperty());
        paneA.heightProperty().add(primaryStage.heightProperty());
        root.getChildren().add(paneA);

//        paneB = new Pane();
//        paneB.setLayoutX(0);
//        paneB.setLayoutY(300);
//        paneB.setPrefWidth(600);
//        root.getChildren().add(paneB);

        audioPlayerA = new AudioPlayer(sourceA, grabberListenerA);
        audioPlayerB = new AudioPlayer(sourceB, grabberListenerB);
    }

    @Override
    public void stop() throws Exception
    {
        audioPlayerA.stop();
        audioPlayerB.stop();
    }
}
