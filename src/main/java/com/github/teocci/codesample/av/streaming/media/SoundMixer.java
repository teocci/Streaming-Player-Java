package com.github.teocci.codesample.av.streaming.media;

import com.github.teocci.codesample.av.streaming.StreamingVideoClient;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.sound.sampled.*;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-14
 */
public class SoundMixer extends Application
{
    private static final String TAG = LogHelper.makeLogTag(StreamingVideoClient.class);

    private Stage primaryStage;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        primaryStage = stage;

        StackPane root = new StackPane();

        Scene scene = new Scene(root, 640, 480);

        primaryStage.setTitle("Streaming Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        LogHelper.e(TAG, "There are " + mixers.length + " mixer info objects");
        for (Mixer.Info mixerInfo : mixers) {
            LogHelper.e(TAG, "mixer name: " + mixerInfo.getName());
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getSourceLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                LogHelper.e(TAG, "Line.Info: " + lineInfo);
                try {
                    Line line = mixer.getLine(lineInfo);
                    FloatControl volCtrl = (FloatControl) line.getControl(
                            FloatControl.Type.MASTER_GAIN);
                    VolumeSlider vs = new VolumeSlider(volCtrl);
                    root.getChildren().add(new Label(volCtrl.toString()));
                    root.getChildren().add(vs.getControl());
                    LogHelper.e(TAG, "volCtrl.getValue() = " + volCtrl.getValue());
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException iaEx) {
                    LogHelper.e(TAG, iaEx);
                }
            }
        }
    }

//    public static void main(String[] args) {
//        Runnable r = () -> {
//            SoundMixer sm = new SoundMixer();
//            Component c = sm.getGui();
//            JOptionPane.showMessageDialog(null, c);
//        };
//        // Swing GUIs should be created and updated on the EDT
//        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
//        SwingUtilities.invokeLater(r);
//    }
}
