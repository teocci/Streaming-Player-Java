package com.github.teocci.codesample.av.streaming.utils;

import com.github.teocci.codesample.av.streaming.enums.JoystickEventEnum;
import com.github.teocci.codesample.av.streaming.enums.JoystickLevelEnum;
import com.github.teocci.codesample.av.streaming.enums.JoystickQuadrantEnum;
import com.github.teocci.codesample.av.streaming.events.JoystickEvent;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.Map;
import java.util.Optional;

import static com.github.teocci.codesample.av.streaming.enums.JoystickEventEnum.*;
import static com.github.teocci.codesample.av.streaming.enums.JoystickQuadrantEnum.calculateQuadrant;
import static javafx.scene.input.MouseEvent.*;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class JoystickEventProducer implements EventHandler<MouseEvent>
{
    private static final String TAG = LogHelper.makeLogTag(JoystickEventProducer.class);

    private final Node node;

    private double beforeX = 0;
    private double beforeY = 0;

    private JoystickReader reader;

    private DoubleProperty povCenterXProperty;
    private DoubleProperty povCenterYProperty;
    private DoubleProperty povCenterXLayoutProperty;
    private DoubleProperty povCenterYLayoutProperty;

    private Map<Integer, IntegerProperty> levels;

    private int joystickLevelBefore = 0;
    private JoystickQuadrantEnum quadrantBefore = JoystickQuadrantEnum.NONE;

    public JoystickEventProducer(
            Node node,
            DoubleProperty povCenterXProperty,
            DoubleProperty povCenterYProperty,
            DoubleProperty povCenterXLayoutProperty,
            DoubleProperty povCenterYLayoutProperty,
            Map<Integer, IntegerProperty> levels
    )
    {
        this.node = node;
        this.levels = levels;

        this.povCenterXProperty = povCenterXProperty;
        this.povCenterYProperty = povCenterYProperty;

        this.povCenterXLayoutProperty = povCenterXLayoutProperty;
        this.povCenterYLayoutProperty = povCenterYLayoutProperty;
    }

    @Override
    public void handle(MouseEvent event)
    {
        if (!isIgnored(event.getEventType().getName())) {
//            LogHelper.e(TAG, "event(getEventType)" + event.getEventType());
            produce(toMouseEventWithCartesianPoints(event));
        }
    }

    private boolean isIgnored(String eventName)
    {
        return eventName.equals("MOUSE_ENTERED") || eventName.equals("MOUSE_ENTERED_TARGET")
                || eventName.equals("MOUSE_MOVED") || eventName.equals("MOUSE_EXITED") || eventName.equals("MOUSE_EXITED_TARGET");
    }

    private void produce(MouseEvent event)
    {
        LogHelper.e(TAG, "produce()");
        quadrantBefore = whichQuadrant(beforeX, beforeY);

        double x = event.getX();
        double y = event.getY();

        int angle = getAngle(x, y);
        int strength = getStrength(x, y);
        int level = determineLevel(x, y);
        JoystickQuadrantEnum quadrant = whichQuadrant(x, y);


        if (event.getEventType().equals(MOUSE_PRESSED)) {
//            LogHelper.e(TAG, "MOUSE_PRESSED(" + ")");
            reader = new JoystickReader(angle, strength, level, quadrant, event);
            reader.start();
        }

        if (event.getEventType().equals(MOUSE_RELEASED)) {
//            LogHelper.e(TAG, "MOUSE_RELEASED(" + ")");
            if (reader != null) {
                reader.stop();
            }

//            KeyValue keyValue = new KeyValue(node.layoutXProperty(), 0);
//            KeyFrame keyFrame = new KeyFrame(Duration.millis(100), keyValue);
//            Timeline timeline = new Timeline(keyFrame);
//            timeline.play();
        }

        if (reader != null) {
            update(angle, strength, level, quadrant, event);
        }

        fireEvent(angle, strength, level, quadrant, event);

        resetToAfter(x, y, quadrant, level);
    }

    private void update(int angle, int strength, int level, JoystickQuadrantEnum quadrant, MouseEvent event)
    {
        reader.angle = angle;
        reader.strength = strength;
        reader.level = level;
        reader.quadrant = quadrant;
        reader.event = event;
    }

    private void fireEvent(int angle, int strength, int level, JoystickQuadrantEnum quadrant, MouseEvent e)
    {
        LogHelper.e(TAG, "fireEvent(" + angle + ", " + strength + ", " + level + ", " + quadrant + ", " + e.getEventType() + ")");
        if (!(level == joystickLevelBefore)) {
            LogHelper.e(TAG, "fireEvent(LEVEL_CHANGED)");
            node.fireEvent(createEvent(e, LEVEL_CHANGED, angle, strength, level, quadrant));
        }

        if (!quadrant.equals(quadrantBefore)) {
            LogHelper.e(TAG, "fireEvent(QUADRANT_CHANGED)");
            node.fireEvent(createEvent(e, QUADRANT_CHANGED, angle, strength, level, quadrant));
        }

//        if (e.getEventType().equals(MOUSE_DRAGGED)) {
////            LogHelper.e(TAG, "fireEvent(POV_MOVED)");
//            node.fireEvent(createEvent(e, POV_MOVED, angle, strength, level, quadrant));
//        }

        node.fireEvent(createEvent(e, UNDEFINED, angle, strength, level, quadrant));
    }

    public static JoystickEvent createEvent(MouseEvent e, JoystickEventEnum joystickEvent, int angle, int strength, int levelNumber, JoystickQuadrantEnum quadrantAfter)
    {
        LogHelper.e(TAG, "createEvent()");
        return new JoystickEvent(
                e.getSource(),
                e.getTarget(),
                joystickEvent,
                e.getX(),
                e.getY(),
                angle,
                strength,
                quadrantAfter,
                JoystickLevelEnum.getJoystickLevelByCode(levelNumber)
        );
    }

    private int determineLevel(double x, double y)
    {
        if (y == x && y == 0) {
            return 0;
        }

        Optional<Map.Entry<Integer, IntegerProperty>> level = levels
                .entrySet()
                .stream()
                .filter(e -> MoveCalculatorUtil.isCircleArea(e.getValue().get(), x, y))
                .max((l, r) -> r.getKey().compareTo(l.getKey()));

        return level.isPresent() ? level.get().getKey() : joystickLevelBefore;
    }

    private JoystickQuadrantEnum whichQuadrant(double x, double y)
    {
        if (y == x && y == 0) {
            return JoystickQuadrantEnum.NONE;
        } else {
//            LogHelper.event(TAG, "whichQuadrant(" + x + ", " + y + ")");
            int angle = getAngle(x, y);
//            LogHelper.event(TAG, "angle: " + angle);

            int strength = getStrength(x, y);
//            LogHelper.event(TAG, "strength: " + strength);

            return calculateQuadrant(angle);
        }
    }

    private void resetToAfter(double afterX, double afterY, JoystickQuadrantEnum quadrantAfter, int joystickLevelAfter)
    {
        beforeX = afterX;
        beforeY = afterY;
        joystickLevelBefore = joystickLevelAfter;
        quadrantBefore = quadrantAfter;
    }

    /**
     * Process the angle following the 360° counter-clock protractor rules.
     *
     * @return the angle of the button
     */
    private int getAngle(double x, double y)
    {
        int angle = (int) Math.toDegrees(Math.atan2(y, x));
        return angle < 0 ? angle + 360 : angle; // make it as a regular counter-clock protractor
    }

    /**
     * Process the strength as a percentage of the distance between the center and the border.
     *
     * @return the strength of the button
     */
    private int getStrength(double x, double y)
    {
        return (int) (Math.sqrt(x * x + y * y));
    }

    private MouseEvent toMouseEventWithCartesianPoints(MouseEvent e)
    {
        return new MouseEvent(e.getSource(), e.getTarget(), e.getEventType(),
                MoveCalculatorUtil.resetToCenterX(povCenterXProperty.get(), povCenterXLayoutProperty.get()),
                MoveCalculatorUtil.resetToCenterY(povCenterYProperty.get(), povCenterYLayoutProperty.get()),
                e.getScreenX(), e.getScreenY(), e.getButton(), e.getClickCount(), e.isShiftDown(), e.isControlDown(),
                e.isAltDown(), e.isMetaDown(), e.isPrimaryButtonDown(), e.isMiddleButtonDown(),
                e.isSecondaryButtonDown(), e.isSynthesized(), e.isPopupTrigger(), e.isStillSincePress(), null);
    }


    class JoystickReader extends AnimationTimer
    {
        private long lastTime = System.currentTimeMillis();
        public int angle, strength, level;

        public JoystickQuadrantEnum quadrant;
        public MouseEvent event;


        public JoystickReader(int angle, int strength, int level, JoystickQuadrantEnum quadrant, MouseEvent event)
        {
            this.angle = angle;
            this.strength = strength;
            this.level = level;
            this.quadrant = quadrant;
            this.event = event;
        }

        @Override
        public void handle(long now)
        {
//            LogHelper.event(TAG, "handle(" + 0 + ")");
            // max speed: 100 hundred times per second
            if (now - lastTime > 10000000) {
                LogHelper.e(TAG, "handle(" + (now - lastTime) + ")");
                lastTime = now;
//                fireEvent(angle, strength, level, quadrant, event);
                node.fireEvent(createEvent(event, POV_MOVED, angle, strength, level, quadrant));
            }
        }
    }
}
