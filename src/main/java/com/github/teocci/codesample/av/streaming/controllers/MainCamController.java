package com.github.teocci.codesample.av.streaming.controllers;

import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import com.github.teocci.codesample.av.streaming.views.MainCamView;
import com.github.teocci.codesample.av.streaming.views.MainView;
import javafx.stage.Stage;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class MainCamController
{
    private static final String TAG = LogHelper.makeLogTag(MainCamController.class);

    private MainCamView view;

    public MainCamController(Stage stage)
    {
        view = new MainCamView(this, stage);

        init();
    }

    private void init()
    {

    }
}
