package com.github.teocci.codesample.av.streaming;

import com.github.teocci.codesample.av.streaming.elements.Joystick;
import com.github.teocci.codesample.av.streaming.enums.JoystickQuadrantEnum;
import com.github.teocci.codesample.av.streaming.exceptions.JoystickException;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.controlsfx.control.PlusMinusSlider;

import static com.github.teocci.codesample.av.streaming.enums.JoystickEventEnum.*;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class GroupVsPaneTest extends Application
{
    private static final String TAG = LogHelper.makeLogTag(GroupVsPaneTest.class);

    private static final int MOVE_MAX_RANGE = 2;
    private static final int RADIUS = 100;

    private PlusMinusSlider plusMinusSlider = new PlusMinusSlider();

    private Rectangle rect1 = new Rectangle(100, 100, 100, 100);
    private Rectangle rect2 = new Rectangle(100, 100, 100, 100);
    private Rectangle rect3 = new Rectangle(200, 200, 100, 100);
    private Rectangle rect4 = new Rectangle(200, 200, 100, 100);

    private String quadrant;

    @Override
    public void start(Stage primaryStage)
    {
        Pane pane = new Pane();
        Group group = new Group();

        VBox.setVgrow(group, Priority.NEVER);
        VBox.setVgrow(pane, Priority.NEVER);

//        plusMinusSlider.setOrientation(VERTICAL);
        Button roundButton = new Button();
        roundButton.setId("round-button");

        VBox vbox = new VBox(group, pane, roundButton, plusMinusSlider, getJoystickPane());

        double r = 20;
        roundButton.setShape(new Circle(r));
        roundButton.setMinSize(2 * r, 2 * r);
        roundButton.setMaxSize(2 * r, 2 * r);

        rect1.setFill(Color.BLUE);
        rect2.setFill(Color.BLUE);
        rect3.setFill(Color.GREEN);
        rect4.setFill(Color.GREEN);

        group.getChildren().addAll(rect1, rect3);
        pane.getChildren().addAll(rect2, rect4);

        Scene scene = new Scene(vbox, 800, 800);
        scene.getStylesheets().add("css/style.css");

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            double deltaX;
            switch (e.getCode()) {
                case LEFT:
                    deltaX = -10;
                    break;
                case RIGHT:
                    deltaX = 10;
                    break;
                default:
                    deltaX = 0;
            }
            rect3.setX(rect3.getX() + deltaX);
            rect4.setX(rect4.getX() + deltaX);
        });

        plusMinusSlider.setId("zoom-slider");
        plusMinusSlider.setOrientation(Orientation.VERTICAL);

        plusMinusSlider.setOnValueChanged(event -> {
            double deltaX = MOVE_MAX_RANGE * event.getValue();

            rect3.setX(rect3.getX() + deltaX);
            rect4.setX(rect4.getX() + deltaX);
            LogHelper.e(TAG, "Slider");
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane getJoystickPane()
    {
        BorderPane result = new BorderPane();
        result.setPadding(new Insets(15, 12, 15, 12));

        Joystick joystickPane = new Joystick(RADIUS, 1);
        joystickPane.addEventHandler(QUADRANT_CHANGED.getEventType(), e -> {
            quadrant = getQuadrantToCommand(e.getQuadrant());
//            LogHelper.e(TAG, "Whenever quadrant change: " + e.getQuadrant());
        });

        joystickPane.addEventHandler(LEVEL_CHANGED.getEventType(), e -> {
            LogHelper.e(TAG, "Whenever level changed: " + e.getJoystickLevel());
        });

        joystickPane.addEventHandler(POV_MOVED.getEventType(), e -> {
//            LogHelper.e(TAG, "(angle, strength) -> (" + e.getAngle() + ", " + e.getStrength() + ")");
            double deltaX = calculateDelta(e.getQuadrant(), e.getStrength());
            if (deltaX != 0) {
                LogHelper.e(TAG, "(deltaX) -> (" + deltaX + ")");
                rect3.setX(rect3.getX() + deltaX);
                rect4.setX(rect4.getX() + deltaX);
            }
        });

        result.setCenter(joystickPane);
        return result;
    }

    private double calculateDelta(final JoystickQuadrantEnum quadrant, int strength)
    {
        switch (quadrant) {
            case QUADRANT_I:
            case QUADRANT_II:
            case QUADRANT_VIII:
                return MOVE_MAX_RANGE * ((double) strength / 200);
            case QUADRANT_V:
            case QUADRANT_IV:
            case QUADRANT_VI:
                return -1 * MOVE_MAX_RANGE * ((double) strength / 200);
            default:
                return 0;
        }
    }


    private String getQuadrantToCommand(final JoystickQuadrantEnum quadrant)
    {
        switch (quadrant) {
            case NONE:
                return "stop";
            case QUADRANT_I:
                return "right";
            case QUADRANT_II:
                return "top-right";
            case QUADRANT_III:
                return "top";
            case QUADRANT_IV:
                return "top-left";
            case QUADRANT_V:
                return "left";
            case QUADRANT_VI:
                return "bottom-left";
            case QUADRANT_VII:
                return "bottom";
            case QUADRANT_VIII:
                return "bottom-right";
            default:
                throw new JoystickException("No such command");
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
