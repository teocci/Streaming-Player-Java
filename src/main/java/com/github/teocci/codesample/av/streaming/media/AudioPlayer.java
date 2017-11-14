package com.github.teocci.codesample.av.streaming.media;

import com.github.teocci.codesample.av.streaming.interfaces.GrabberListener;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Platform;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017-Nov-14
 */
public class AudioPlayer
{
    private static final String TAG = LogHelper.makeLogTag(AudioPlayer.class);

    private static volatile Thread playThread;

    private SourceDataLine soundLine;
    private FloatControl gainControl;

    private AtomicBoolean isPlaying;

    public AudioPlayer(String source, GrabberListener grabberListener)
    {
        if (grabberListener == null) return;
        if (source.isEmpty()) return;
        isPlaying = new AtomicBoolean(false);

        playThread = new Thread(() -> {
            try {
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(source);
                grabber.start();

                grabberListener.onMediaGrabbed(grabber.getImageWidth(), grabber.getImageHeight());

                if (grabber.getSampleRate() > 0 && grabber.getAudioChannels() > 0) {
                    AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);

                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                    soundLine = (SourceDataLine) AudioSystem.getLine(info);
                    soundLine.open(audioFormat);
                    soundLine.start();

                    gainControl = (FloatControl) soundLine.getControl(FloatControl.Type.MASTER_GAIN);
                    grabberListener.onGainControl(gainControl);
                }

                Java2DFrameConverter converter = new Java2DFrameConverter();

                ExecutorService executor = Executors.newSingleThreadExecutor();

                while (!Thread.interrupted()) {
                    Frame frame = grabber.grab();
                    if (frame == null) {
                        LogHelper.e(TAG, "frame: null");
                        break;
                    }

                    isPlaying.set(true);

                    if (frame.samples != null) {
                        ShortBuffer channelSamplesFloatBuffer = (ShortBuffer) frame.samples[0];
                        channelSamplesFloatBuffer.rewind();

                        ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesFloatBuffer.capacity() * 2);

                        for (int i = 0; i < channelSamplesFloatBuffer.capacity(); i++) {
                            short val = channelSamplesFloatBuffer.get(i);
                            outBuffer.putShort(val);
                        }

                        // We need this because soundLine.write ignores
                        // interruptions during writing.


                        if (soundLine == null) return;
//                        soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
//                        outBuffer.clear();

//                        Platform.runLater(() -> {
//                            soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
//                            outBuffer.clear();
//                        });

                        try {
                            executor.submit(() -> {
                                soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
                                outBuffer.clear();
                            }).get();
                        } catch (InterruptedException interruptedException) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                executor.shutdownNow();
                executor.awaitTermination(10, SECONDS);

                if (soundLine != null) {
                    soundLine.stop();
                }
                grabber.stop();
                grabber.release();
                Platform.exit();
            } catch (Exception exception) {
                LogHelper.e(TAG, exception);
                System.exit(1);
            }
        });
        playThread.start();
    }

    public void stop()
    {
        playThread.interrupt();
    }

    public void changeVolume(int newVolume) {
        gainControl.setValue(newVolume);
    }

    public boolean isPlaying() {
        return isPlaying.get();
    }
}
