package com.github.teocci.codesample.av.streaming.managers;

import com.github.teocci.codesample.av.streaming.controllers.MainController;
import com.github.teocci.codesample.av.streaming.controllers.VideoController;
import com.github.teocci.codesample.av.streaming.elements.VideoPane;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jun-28
 */
public class VideoPaneManager
{
    private static final String TAG = LogHelper.makeLogTag(WindowManager.class);

    private Map<String, VideoPane> panes = new ConcurrentHashMap<>();
    private ObservableList<Pane> paneList = FXCollections.observableArrayList(extractVideoViews());

    private MainController controller;

    public VideoPaneManager(MainController controller)
    {
        this.controller = controller;
    }

    public boolean add(String id, VideoPane pane)
    {
        if (id == null || id.isEmpty()) return false;
        if (pane == null) return false;
        if (contains(id)) return false;

        panes.put(id, pane);
        return true;
    }

    private List<Pane> extractVideoViews()
    {
        List<Pane> paneList = new ArrayList<>();
        if (!isEmpty()) {
            for (VideoPane pane : panes.values()) {
                paneList.add(pane);
            }
        }

        return paneList;
    }

    public boolean contains(String id)
    {
        return !isEmpty() && panes.containsKey(id);
    }

    public boolean isEmpty()
    {
        return panes != null && panes.isEmpty();
    }
}
