package com.github.teocci.codesample.av.streaming.views;

import com.github.teocci.codesample.av.streaming.controllers.MainController;
import com.github.teocci.codesample.av.streaming.elements.VideoPane;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import static com.github.teocci.codesample.av.streaming.utils.Config.IMAGE_ERROR;
import static javafx.geometry.Pos.TOP_CENTER;
import static javafx.geometry.Pos.TOP_RIGHT;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-26
 */
public class MainView
{
    private static final String TAG = LogHelper.makeLogTag(MainView.class);

    private MainController controller;

    private ScrollPane scroll = new ScrollPane();
    private TilePane tile = new TilePane();

    private Label label = new Label("Not clicked");

    private Stage primaryStage;
    private Scene scene;

    private StackPane root = new StackPane();

    public MainView(MainController controller, Stage stage)
    {
        this.controller = controller;

        primaryStage = stage;
        primaryStage.setTitle("Main Stream Video");

        scene = new Scene(root, 1030, 600);
        scene.getStylesheets().add("css/style.css");

        initElements();
        initHandlers();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initElements()
    {
        tile.setPadding(new Insets(5));
        tile.setId("content-tile");
        tile.getChildren().clear();
        tile.setHgap(5);
        tile.setVgap(5);

        scroll.setId("video-list-scroller");
        scroll.setFitToWidth(true);
//        scroll.setFitToHeight(true);
//        scroll.setVbarPolicy(AS_NEEDED);
        scroll.setContent(tile);

//        HBox.setHgrow(scroll, NEVER);
        StackPane.setAlignment(label, TOP_CENTER);
        root.getChildren().addAll(label, scroll);
    }

    private void initHandlers()
    {
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void addPane(VideoPane pane)
    {
        LogHelper.e(TAG, "Adding: " + pane.getTitle());
        pane.setOnMousePressed(event -> {
            label.setText(pane.getTitle() + "  clicked!");
            controller.showWindow(pane.getWindowInfo());
        });

        tile.getChildren().add(pane);
    }

    public Parent asParent()
    {
        return root;
    }

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public void setScene(Scene scene)
    {
        this.scene = scene;
    }

    public Scene getScene()
    {
        return scene;
    }

    public void updateVideoPane(String id, Image image)
    {
        VideoPane videoPane = getVideoPane(id);
        if (videoPane == null) return;

        videoPane.updateImage(image);
    }

    private VideoPane getVideoPane(String id)
    {
        ObservableList<Node> panes = FXCollections.observableArrayList(tile.getChildren());
        for (Node pane : panes) {
            if (pane.getId().equals(id)) {
                return (VideoPane) pane;
            }
        }

        return null;
    }

    public void notifyError(Exception e)
    {
        Platform.runLater(()->{
            Notifications.create()
                    .title("Warning")
                    .text(e.getMessage())
                    .position(TOP_RIGHT)
                    .graphic(new ImageView(new Image(IMAGE_ERROR)))
                    .show();
        });
    }
}
