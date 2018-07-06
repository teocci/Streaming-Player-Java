package com.github.teocci.codesample.av.streaming.controllers;

import com.github.teocci.codesample.av.streaming.elements.Joystick;
import com.github.teocci.codesample.av.streaming.handlers.JoystickEventHandler;
import com.github.teocci.codesample.av.streaming.handlers.PovEventHandler;
import com.github.teocci.codesample.av.streaming.utils.JoystickEventProducer;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.teocci.codesample.av.streaming.elements.Joystick.POV_RADIUS_DIVIDER;
import static com.github.teocci.codesample.av.streaming.enums.JoystickEventEnum.LEVEL_CHANGED;
import static com.github.teocci.codesample.av.streaming.enums.JoystickEventEnum.QUADRANT_CHANGED;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class JoystickController
{
    private static final String TAG = LogHelper.makeLogTag(JoystickController.class);

    private Joystick joystick;

    private Map<Integer, IntegerProperty> levels;

    private DoubleProperty radiusProperty;
    private DoubleProperty povCenterXProperty;
    private DoubleProperty povCenterYProperty;

    private JoystickEventProducer eventProducer;

    private JoystickEventHandler joystickEventHandler;
    private PovEventHandler povEventHandler;

    public JoystickController(final Joystick joystick)
    {
        this.joystick = joystick;
        initProperties(joystick.getRadius(), joystick.getLevels());
        bindingProperties();
        addCanvasListener();
        initHandlers();
        addPovListener();
        povEventHandling();

        radiusProperty.set(joystick.getRadius());
    }

    public Joystick getJoystick()
    {
        return joystick;
    }

    public Map<Integer, IntegerProperty> getLevels()
    {
        return levels;
    }

    private void povEventHandling()
    {
        eventProducer = new JoystickEventProducer(
                joystick,
                joystick.getPov().centerXProperty(),
                joystick.getPov().centerYProperty(),
                povCenterXProperty,
                povCenterYProperty,
                levels
        );

        joystick.addEventHandler(MouseEvent.ANY, event -> eventProducer.handle(event));
    }

    private void initProperties(final double radius, final int levelNumber)
    {
        radiusProperty = new SimpleDoubleProperty();

        povCenterXProperty = new SimpleDoubleProperty();
        povCenterYProperty = new SimpleDoubleProperty();

        levels = IntStream
                .range(0, levelNumber)
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> createProperty(radius, levelNumber, i + 1)));
    }

    private IntegerProperty createProperty(final double radius, int levels, Integer i)
    {
        SimpleIntegerProperty simpleIntegerProperty = new SimpleIntegerProperty((int) (radius / i));

        LogHelper.e(TAG, "createProperty(" + simpleIntegerProperty.get() + ", " + levels + ", " + levels + ")");
        simpleIntegerProperty.bind(radiusProperty.divide(levels).multiply(i));

        LogHelper.e(TAG, "createProperty(" + radiusProperty.get() + ", " + levels + ", " + levels + ")");
        return simpleIntegerProperty;
    }

    private void bindingProperties()
    {
        joystick.getPov().radiusProperty().bind(radiusProperty.divide(levels.size()).divide(POV_RADIUS_DIVIDER));

        Canvas canvas = joystick.getCanvas();
        povCenterXProperty.bind(radiusProperty.add(radiusProperty.divide(POV_RADIUS_DIVIDER)).add(canvas.layoutXProperty()));
        povCenterYProperty.bind(radiusProperty.add(radiusProperty.divide(POV_RADIUS_DIVIDER)).add(canvas.layoutYProperty()));

//        LogHelper.e(TAG, "povCenterX: " + povCenterXProperty.get() + " povCenterY: " + povCenterYProperty.get());

        radiusProperty.addListener(e -> {
            double size = Joystick.calculateSize(radiusProperty.get());

            joystick.getCanvas().setHeight(size);
            joystick.getCanvas().setWidth(size);
            LogHelper.e(TAG, "size: " + size);
            joystickEventHandler.drawJoystick(joystick.getCanvas(), radiusProperty, levels, levels.size());
            povEventHandler.bindPovToCanvas(joystick.getPov(), povCenterXProperty, povCenterYProperty);

//            LogHelper.e(TAG, "addListener->povCenterX: " + povCenterXProperty.get() + " povCenterY: " + povCenterYProperty.get());
//            LogHelper.e(TAG, "addListener->canvas.layout( " + canvas.getLayoutX() + ", " + canvas.getLayoutY() + ")");
        });
    }

    private void initHandlers()
    {
        joystickEventHandler = new JoystickEventHandler();
        povEventHandler = new PovEventHandler();
    }

    private void addPovListener()
    {

        joystick.getPov().addEventHandler(
                MouseEvent.MOUSE_DRAGGED,
                (e) -> povEventHandler.draggedPov(
                        e,
                        joystick.getPov(),
                        radiusProperty,
                        povCenterXProperty,
                        povCenterYProperty
                )
        );
        joystick.getPov().setOnMouseReleased(
                e -> povEventHandler.bindPovToCanvas(joystick.getPov(), povCenterXProperty, povCenterYProperty)
        );
    }

    private void addCanvasListener()
    {
        joystick.getCanvas().addEventHandler(
                MouseEvent.MOUSE_DRAGGED,
                (e) -> joystickEventHandler.onMouseDragged(joystick.getPov(), povCenterXProperty, povCenterYProperty)
        );
        joystick.addEventHandler(
                QUADRANT_CHANGED.getEventType(),
                e -> joystickEventHandler.drawTargetLevel(e, joystick.getCanvas(), radiusProperty, levels)
        );
        joystick.addEventHandler(
                LEVEL_CHANGED.getEventType(),
                e -> joystickEventHandler.drawTargetLevel(e, joystick.getCanvas(), radiusProperty, levels)
        );
    }
}
