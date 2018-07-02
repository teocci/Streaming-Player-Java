package com.github.teocci.codesample.av.streaming;

import com.github.teocci.codesample.av.streaming.elements.VUMeter;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-22
 */
public class FxExperiencePlayer extends Application
{
    private MediaPlayer mediaPlayer;

    @Override public void start(final Stage primaryStage) {
        VUMeter vuMeter = new VUMeter();
        String source = "rtsp://192.168.20.11:8086";

        final Media media = new Media(source);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnError(() -> System.out.println("mediaPlayer.getError() = " + mediaPlayer.getError()));
    }

    /** @param args the command line arguments */
    public static void main(String[] args) {
        launch(args);
    }
}
