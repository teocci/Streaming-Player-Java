package com.github.teocci.codesample.av.streaming.media;

import com.github.teocci.codesample.av.streaming.interfaces.GrabberListener;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-14
 */
public class SimplePlayer
{
    private static final String TAG = LogHelper.makeLogTag(SimplePlayer.class);

    private static volatile Thread playThread;
    private AnimationTimer timer;

    private SourceDataLine soundLine;

    private int counter;

    public SimplePlayer(String source, GrabberListener grabberListener)
    {
        if (grabberListener == null) return;
        if (source.isEmpty()) return;

        counter = 0;

        playThread = new Thread(() -> {
            try {
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(source);
                grabber.setOption("rtsp_transport", "tcp");
                grabber.start();

                grabberListener.onMediaGrabbed(grabber.getImageWidth(), grabber.getImageHeight());

                if (grabber.getSampleRate() > 0 && grabber.getAudioChannels() > 0) {
                    AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);

                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                    soundLine = (SourceDataLine) AudioSystem.getLine(info);
                    soundLine.open(audioFormat);
                    soundLine.start();
                }

                Java2DFrameConverter converter = new Java2DFrameConverter();

//                ExecutorService executor = Executors.newSingleThreadExecutor();

                while (!Thread.interrupted()) {
                    Frame frame = grabber.grab();
                    if (frame == null) {
                        LogHelper.e(TAG, "frame: null");
                        break;
                    }
                    if (frame.image != null) {
                        Image image = SwingFXUtils.toFXImage(converter.convert(frame), null);
                        Platform.runLater(() -> {
                            grabberListener.onImageProcessed(image);
                        });

//                        Platform.runLater(() -> {
//                            Image image = SwingFXUtils.toFXImage(converter.convert(frame), null);
//                            grabberListener.onImageProcessed(image);
//                        });

//                        try {
//                            executor.submit(() -> {
//                                Image image = SwingFXUtils.toFXImage(converter.convert(frame), null);
//                                grabberListener.onImageProcessed(image);
//                            }).get();
//                        } catch (InterruptedException interruptedException) {
//                            Thread.currentThread().interrupt();
//                        }

//                        timer = new AnimationTimer()
//                        {
//                            @Override
//                            public void handle(long now)
//                            {
//                                Image image = SwingFXUtils.toFXImage(converter.convert(frame), null);
//                                grabberListener.onImageProcessed(image);
//                            }
//                        };
//                        timer.start();

//                        timer = new AnimationTimer()
//                        {
//                            @Override
//                            public void handle(long now)
//                            {
//                                BufferedImage bufferedImage = null;
//                                bufferedImage = converter.convert(frame);
//                                if (bufferedImage == null) return;
//                                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
////                            grabberListener.onImageProcessed(image);
////
////                                BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
////                                try {
////                                    File outputFile = new File("saved" + counter++ + ".png");
////                                    ImageIO.write(bImage, "png", outputFile);
////                                } catch (IOException e) {
////                                    e.printStackTrace();
////                                    System.out.println("Exception caught: " + e);
////                                }
//                                grabberListener.onImageProcessed(image);
//                            }
//                        };
//                        timer.start();
                    } else if (frame.samples != null) {
                        ShortBuffer channelSamplesFloatBuffer = (ShortBuffer) frame.samples[0];
                        channelSamplesFloatBuffer.rewind();

                        ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesFloatBuffer.capacity() * 2);

                        for (int i = 0; i < channelSamplesFloatBuffer.capacity(); i++) {
                            short val = channelSamplesFloatBuffer.get(i);
                            outBuffer.putShort(val);
                        }

                        // We need this because soundLine.write ignores
                        // interruptions during writing.


//                        if (soundLine == null) return;
//                        Platform.runLater(() -> {
//                            soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
//                            outBuffer.clear();
//                        });

//                        try {
//                            executor.submit(() -> {
//                                soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
//                                outBuffer.clear();
//                            }).get();
//                        } catch (InterruptedException interruptedException) {
//                            Thread.currentThread().interrupt();
//                        }
                    }
                }
//                executor.shutdownNow();
//                executor.awaitTermination(10, TimeUnit.SECONDS);

//                if (soundLine != null) {
//                    soundLine.closeAllWindows();
//                }
                grabber.stop();
                grabber.release();
                grabberListener.onStop();
//                Platform.exit();
            } catch (Exception e) {
                try {
                    throw new Exception("Could not open input stream.", e);
                } catch (Exception e1) {
                    grabberListener.onError(e1);
                }
//                System.exit(1);
            }
        });
        playThread.start();
    }

    public void stop()
    {
        if (playThread.isAlive()) {
            LogHelper.e(TAG, "stop stream thread");
            playThread.interrupt();
        }
    }
}
