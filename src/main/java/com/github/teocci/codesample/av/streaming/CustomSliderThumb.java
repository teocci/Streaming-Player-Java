package com.github.teocci.codesample.av.streaming;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-06
 */
public class CustomSliderThumb extends Application
{
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) throws Exception
    {
        Slider slider = new Slider();
        slider.setId("custom-slider");

        VBox layout = new VBox();
        layout.setId("base-layout");
        layout.getChildren().setAll(slider);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("css/style.css");

        stage.setScene(scene);
        stage.show();
    }
}
