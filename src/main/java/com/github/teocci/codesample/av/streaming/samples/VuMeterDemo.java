package com.github.teocci.codesample.av.streaming.samples;

import com.github.teocci.codesample.av.streaming.utils.Builder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

import static javafx.geometry.Orientation.HORIZONTAL;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-31
 */
public class VuMeterDemo extends Application
{
    private static int noOfNodes = 0;

    private static final Random RND = new Random();

    private VuMeter control;

    private long lastTimerCall;

    private AnimationTimer timer;

    @Override
    public void init()
    {
        control = Builder.build(new VuMeter(), node -> {
            node.setNoOfLeds(100);
            node.setOrientation(HORIZONTAL);
            node.setPeakValueVisible(true);
            node.setSections(
                    Builder.build(new Section(), section -> {
                        section.setStart(0);
                        section.setStop(0.6);
                        section.setStyleClass("led-section-0");
                    }),
                    Builder.build(new Section(), section -> {
                        section.setStart(0.6);
                        section.setStop(0.8);
                        section.setStyleClass("led-section-1");
                    }),
                    Builder.build(new Section(), section -> {
                        section.setStart(0.8);
                        section.setStop(1.0);
                        section.setStyleClass("led-section-2");
                    })
            );
        });

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer()
        {
            @Override
            public void handle(final long NOW)
            {
                if (NOW > lastTimerCall + 100_000_000l) {
                    control.setValue(RND.nextDouble());
                    lastTimerCall = NOW;
                }
            }
        };
    }

    @Override
    public void start(Stage stage)
    {
        StackPane pane = new StackPane();
        pane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.getChildren().add(control);

        Scene scene = new Scene(pane);

        stage.setTitle("VuMeter Demo");
        stage.setScene(scene);
        stage.show();

        timer.start();

        calcNoOfNodes(scene.getRoot());
        System.out.println(noOfNodes + " Nodes in SceneGraph");
    }

    @Override
    public void stop() {}

    // ******************** Misc **********************************************
    private static void calcNoOfNodes(Node node)
    {
        if (node instanceof Parent) {
            if (((Parent) node).getChildrenUnmodifiable().size() != 0) {
                ObservableList<Node> tempChildren = ((Parent) node).getChildrenUnmodifiable();
                noOfNodes += tempChildren.size();
                for (Node n : tempChildren) {
                    calcNoOfNodes(n);
                    //System.out.println(n.getStyleClass().toString());
                }
            }
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}