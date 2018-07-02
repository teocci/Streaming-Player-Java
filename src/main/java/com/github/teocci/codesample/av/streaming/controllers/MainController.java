package com.github.teocci.codesample.av.streaming.controllers;

import com.github.teocci.codesample.av.streaming.elements.VideoPane;
import com.github.teocci.codesample.av.streaming.managers.VideoPaneManager;
import com.github.teocci.codesample.av.streaming.managers.WindowManager;
import com.github.teocci.codesample.av.streaming.models.WindowInfo;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import com.github.teocci.codesample.av.streaming.utils.Utils;
import com.github.teocci.codesample.av.streaming.views.MainView;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import static com.github.teocci.codesample.av.streaming.utils.Config.MAX_ITEMS;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-26
 */
public class MainController
{
    private static final String TAG = LogHelper.makeLogTag(MainController.class);

    private MainView view;

    private Map<String, String> sources = new HashMap<>();

    private VideoPaneManager paneManager;
    private WindowManager windowManager;

    public MainController(Stage stage)
    {
        paneManager = new VideoPaneManager(this);

        view = new MainView(this, stage);

        init();
    }

    private void init()
    {
        windowManager = new WindowManager(this);
        for (int i = 0; i < MAX_ITEMS; i++) {
            String letter = Utils.getCharForNumber(i + 1);
            String id = "video_" + letter.toLowerCase();
            String title = "Video " + letter;

            sources.put(id, getSource(i));

            WindowInfo windowInfo = new WindowInfo(id, title);
            windowInfo.setSource(sources.get(id));
//            controller.showWindow(windowInfo);

            VideoPane pane = new VideoPane(windowInfo);
            pane.setId(id);

            view.addPane(pane);

            paneManager.add(id, pane);
        }
    }

    public void showWindow(WindowInfo windowInfo)
    {
        if (windowInfo == null) return;
        if (windowManager == null) return;

        try {
            if (windowManager.contains(windowInfo.id)) {
                LogHelper.e(TAG, "Focus on " + windowInfo.id);
                windowManager.toFront(windowInfo.id);
            } else {
                VideoController vc = new VideoController(this, windowInfo);
                windowManager.add(windowInfo.id, vc);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private String getSource(int index)
    {
        if (index % 2 == 0) {
            if (index == 2) {
                return "rtsp://192.168.20.74:8086";
            }
            return "rtsp://192.168.20.43:8086";
        } else {
            return "rtsp://192.168.20.16:8086";
        }
    }

    public MainView getView()
    {
        return view;
    }

    public Parent getViewAsParent()
    {
        return view.asParent();
    }

    public void closeAllWindows()
    {
        LogHelper.e(TAG, "closeAllWindows");
        windowManager.closeAll();
    }

    public void closeWindow(String id, Exception e)
    {
        if (windowManager.contains(id)) {
            LogHelper.e(TAG, "closeWindow");
            windowManager.remove(id);

            if (e != null) {
                view.notifyError(e);
            }
        }
    }

    public void updateVideoPane(WindowInfo windowInfo, Image image)
    {
        view.updateVideoPane(windowInfo.id, image);
    }
}
