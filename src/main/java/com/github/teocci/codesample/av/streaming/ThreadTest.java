package com.github.teocci.codesample.av.streaming;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import com.github.teocci.codesample.av.streaming.utils.ConcurrentUtils;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-03
 */
public class ThreadTest extends Application
{
    private static final String TAG = LogHelper.makeLogTag(ThreadTest.class);
    private static final int NUM_INCREMENTS = 1000000;

    private static int count = 0;

    @Override
    public void start(Stage primaryStage)
    {
        Pane pane = new Pane();

        Scene scene = new Scene(pane, 800, 800);
        scene.getStylesheets().add("css/style.css");

        testSyncIncrement();
        testNonSyncIncrement();

//        primaryStage.setScene(scene);
//        primaryStage.show();
        System.exit(0);
    }

    private void testSyncIncrement()
    {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(4);

        IntStream.range(0, NUM_INCREMENTS).forEach(i -> executor.submit(this::incrementSync));

        ConcurrentUtils.stop(executor);

        System.out.println("   Sync: " + count);
    }

    private void testNonSyncIncrement()
    {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(4);

        IntStream.range(0, NUM_INCREMENTS) .forEach(i -> executor.submit(this::increment));

        ConcurrentUtils.stop(executor);

        System.out.println("NonSync: " + count);
    }

    private synchronized void incrementSync()
    {
        count = count + 1;
    }

    private void increment()
    {
        count = count + 1;
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
