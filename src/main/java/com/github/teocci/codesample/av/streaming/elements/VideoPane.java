package com.github.teocci.codesample.av.streaming.elements;

import com.github.teocci.codesample.av.streaming.models.WindowInfo;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import static com.github.teocci.codesample.av.streaming.utils.Config.IMAGE_NO_VIDEO;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-28
 */
public class VideoPane extends Pane
{
    private static final String TAG = LogHelper.makeLogTag(VideoPane.class);

    private static int SPACING_SIZE = 2;

    private final WindowInfo windowInfo;
    private final Label titleLbl = new Label();


    private Canvas canvas = new Canvas();
    private GraphicsContext graphicsContext;

//    private final ImageView thumbnail = new ImageView();

    private final HBox darkBar = new HBox(SPACING_SIZE);

    private final StackPane previewPane = new StackPane();

    public VideoPane(WindowInfo windowInfo)
    {
        this.windowInfo = windowInfo;

        setId("video-pane");

        initElements();
        initStyles();
        layoutComponents();
    }

    private void initElements()
    {
        String title = windowInfo.getTitle();

        titleLbl.setText(title);
        titleLbl.setTooltip(new Tooltip(title));


        Image image = new Image(IMAGE_NO_VIDEO, 280, 160, true, true);
        graphicsContext = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(image.widthProperty());
        canvas.heightProperty().bind(image.heightProperty());

        graphicsContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());

        Tooltip.install(this, new Tooltip(title));
    }

    private void initStyles()
    {

        titleLbl.setId("video-pane-title");
        titleLbl.getStyleClass().add("text-full-width");

//        canvas.setId("video-pane-thumbnail");
//        canvas.getStyleClass().add("preview-full-size");

        darkBar.setId("video-pane-dark-bar");
        darkBar.getStyleClass().add("dark-bar-normal");

        previewPane.setId("video-pane-preview-pane");
        previewPane.getStyleClass().add("preview-full-size");
    }

    private void layoutComponents()
    {
        darkBar.getChildren().add(titleLbl);

        StackPane.setAlignment(canvas, Pos.TOP_CENTER);
        StackPane.setAlignment(darkBar, Pos.TOP_CENTER);
        previewPane.getChildren().addAll(canvas, darkBar);

        getChildren().add(previewPane);
    }

    public String getTitle()
    {
        return titleLbl.getText();
    }

    public WindowInfo getWindowInfo()
    {
        return windowInfo;
    }

    public void updateImage(Image image)
    {
        Platform.runLater(() -> {
            graphicsContext.drawImage(image, 0, 0, 280, 160);
        });
    }
}
