package com.github.teocci.codesample.av.streaming;

import com.github.teocci.codesample.av.streaming.interfaces.GrabberListener;
import com.github.teocci.codesample.av.streaming.media.AudioPlayer;
import com.github.teocci.codesample.av.streaming.media.VolumeSlider;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import com.github.teocci.codesample.av.streaming.elements.HorizontalVUMeter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    private HorizontalVUMeter vuMeter = new HorizontalVUMeter();

    private Pane paneA;
    private Pane paneB;

    private AudioPlayer audioPlayerA;
    private AudioPlayer audioPlayerB;
    private AudioPlayer audioPlayerC;

    private FloatControl gainControl;

    private GrabberListener grabberListenerA = new GrabberListener()
    {
        @Override
        public void onMediaGrabbed(int width, int height) {}

        @Override
        public void onImageProcessed(Image image) {}

        @Override
        public void onPlaying() {}

        @Override
        public void onStop()
        {
            audioPlayerA.stop();
        }

        @Override
        public void onGainControl(FloatControl gainControl)
        {
            updateVolumeControl(paneA, gainControl);
        }

        @Override
        public void onAudioSpectrum(float amplitude, float magnitude)
        {
//            LogHelper.e(TAG, "amplitude: " + amplitude + " | magnitude: " + magnitude);
            if (Platform.isFxApplicationThread()) {
                vuMeter.setValue(magnitude);
            } else {
                Platform.runLater(() -> {
                    vuMeter.setValue(magnitude/20);
                });
            }
        }

        @Override
        public void onError(Exception e)
        {
            audioPlayerA.stop();
        }
    };

    private GrabberListener grabberListenerB = new GrabberListener()
    {
        @Override
        public void onMediaGrabbed(int width, int height) {}

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
        public void onPlaying() {}

        @Override
        public void onStop() {}

        @Override
        public void onGainControl(FloatControl gainControl)
        {
            updateVolumeControl(paneB, gainControl);
        }

        @Override
        public void onAudioSpectrum(float amplitude, float magnitude) {}

        @Override
        public void onError(Exception e) {}
    };

    private GrabberListener grabberListenerC = new GrabberListener()
    {
        @Override
        public void onMediaGrabbed(int width, int height) {}

        @Override
        public void onImageProcessed(Image image)
        {
            LogHelper.e(TAG, "image: " + image);

            Platform.runLater(() -> {
                imageView.setImage(image);
//                graphicsContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
            });
        }

        @Override
        public void onPlaying() {}

        @Override
        public void onStop() {}

        @Override
        public void onGainControl(FloatControl gainControl)
        {
            updateVolumeControl(paneA, gainControl);
        }

        @Override
        public void onAudioSpectrum(float amplitude, float magnitude) {}

        @Override
        public void onError(Exception e) {}
    };

    @Override
    public void start(Stage stage) throws Exception
    {
//        String source = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"; // the video is weird for 1 minute then becomes stable
//        String sourceA = "rtsp://192.168.1.113:8086"; // the SmartCam app the video is bad
//        String sourceB = "rtsp://192.168.1.215:8086"; // the SmartCam app the video is bad
//        String sourceC = "rtsp://192.168.1.215:8086"; // the SmartCam app the video is bad
//        String sourceA = "rtsp://192.168.1.119:8086"; // the SmartCam app the video is bad
//        String sourceB = "rtsp://192.168.1.128:8086"; // the SmartCam app the video is bad
//        String sourceC = "rtsp://192.168.1.133:8086"; // the SmartCam app the video is bad
        String sourceA = "rtsp://192.168.20.43:8086";

        primaryStage = stage;
        imageView = new ImageView();

        root = new StackPane();

        root.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());

        scene = new Scene(root, 640, 480);
        scene.getStylesheets().add("css/style.css");

        vuMeter.setId("vu-meter");
        vuMeter.setValue(0);
//        vuMeter.setLayoutX(54);
//        vuMeter.setLayoutY(177);

        primaryStage.setTitle("Streaming Audio Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        paneA = new Pane();
//        paneA.setTranslateX(20);
//        paneA.setTranslateY(20);

        paneA.widthProperty().add(primaryStage.widthProperty());
        paneA.heightProperty().add(primaryStage.heightProperty());
        root.getChildren().add(paneA);

//        paneB = new Pane();
//        paneB.setLayoutX(0);
//        paneB.setLayoutY(300);
//        paneB.setPrefWidth(600);
//        root.getChildren().add(paneB);

        audioPlayerA = new AudioPlayer(sourceA, grabberListenerA);
//        audioPlayerB = new AudioPlayer(sourceB, grabberListenerB);
//        audioPlayerC = new AudioPlayer(sourceC, grabberListenerC);
    }

    @Override
    public void stop() throws Exception
    {
        audioPlayerA.stop();
//            audioPlayerB.closeAllWindows();
//            audioPlayerC.closeAllWindows();
    }

    private void updateVolumeControl(Pane pane, FloatControl gainControl)
    {
        Platform.runLater(() -> {
            VBox control = new VBox(5);
            VolumeSlider vs = new VolumeSlider(gainControl);
            control.getChildren().addAll(vs.getControl(), vuMeter);

            pane.getChildren().add(control);
        });
    }
}
