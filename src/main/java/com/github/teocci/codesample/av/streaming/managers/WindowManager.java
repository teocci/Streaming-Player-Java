package com.github.teocci.codesample.av.streaming.managers;

import com.github.teocci.codesample.av.streaming.controllers.MainController;
import com.github.teocci.codesample.av.streaming.controllers.VideoController;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-27
 */
public class WindowManager
{
    private static final String TAG = LogHelper.makeLogTag(WindowManager.class);

    private Map<String, VideoController> windows = new ConcurrentHashMap<>();

    private List<Pane> videoPanes = new ArrayList<>();

    private MainController controller;

    public WindowManager(MainController controller)
    {
        this.controller = controller;
    }

    public boolean add(String id, VideoController controller)
    {
        if (controller == null) return false;
        if (contains(id)) return false;

        windows.put(id, controller);
        LogHelper.e(TAG, "Controller added: " + controller);
        return true;
    }

    public void close(String id)
    {
        if (isEmpty()) return;
        LogHelper.e(TAG, "closeStage");

        VideoController controller = windows.get(id);
        controller.stopStream();
    }

    public void closeAll()
    {
        if (isEmpty()) return;

        for (Map.Entry<String, VideoController> entry : windows.entrySet()) {
            VideoController controller = entry.getValue();
            controller.stopStream();
        }
    }

    public boolean remove(String id)
    {
        if (!contains(id)) return false;
        LogHelper.e(TAG, "remove");

        close(id);
        windows.remove(id);
        return true;
    }

    public void toFront(String id)
    {
        if (!contains(id)) return;
        LogHelper.e(TAG, "stageToFront");

        VideoController controller = windows.get(id);
        controller.stageToFront();
    }

    public boolean contains(String id)
    {
        return !isEmpty() && windows.containsKey(id);
    }

    public boolean isEmpty()
    {
        return windows != null && windows.isEmpty();
    }
}
