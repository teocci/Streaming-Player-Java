package com.github.teocci.codesample.av.streaming;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-06
 */
public class MousePressAndHoldTest extends Application
{

    @Override
    public void start(Stage primaryStage)
    {
        Button button = new Button("Push and Hold me");
        addPressAndHoldHandler(button, Duration.millis(100), event -> {
            System.out.println("Press and hold");
            if (event.getEventType().equals(MouseEvent.DRAG_DETECTED)) {
                System.out.println("Ignoring DRAG_DETECTED events");
            }
        });

        Pane root = new Pane(button);

        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }

    private void addPressAndHoldHandler(Node node, Duration holdTime, EventHandler<MouseEvent> handler)
    {
        Wrapper<MouseEvent> eventWrapper = new Wrapper<>();

        PauseTransition holdTimer = new PauseTransition(holdTime);
        holdTimer.setOnFinished(event -> handler.handle(eventWrapper.content));

        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            eventWrapper.content = event;
            holdTimer.playFromStart();
        });

        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> holdTimer.stop());
        node.addEventHandler(MouseEvent.DRAG_DETECTED, event -> {
            eventWrapper.content = event;
            holdTimer.stop();
        });
    }


    public static void main(String[] args)
    {
        launch(args);
    }

    class Wrapper<T>
    {
        T content;
    }
}