package com.github.teocci.codesample.av.streaming.media;

import com.github.teocci.codesample.av.streaming.interfaces.GrabberListener;
import com.github.teocci.codesample.av.streaming.utils.LogHelper;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.media.MediaPlayer;
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

//    private final Object disposeLock = new Object();

    private static volatile Thread playThread;

    private SourceDataLine soundLine;
    private FloatControl gainControl;


//
//    /**
//     * The volume at which the media should be played. The range of effective
//     * values is <code>[0.0&nbsp;1.0]</code> where <code>0.0</code> is inaudible
//     * and <code>1.0</code> is full volume, which is the default.
//     */
//    private DoubleProperty volume;
//
//    /**
//     * Sets the audio playback volume. Its effect will be clamped to the range
//     * <code>[0.0,&nbsp;1.0]</code>.
//     *
//     * @param value the volume
//     */
//    public final void setVolume(double value) {
//        volumeProperty().set(value);
//    }
//
//    /**
//     * Retrieves the audio playback volume. The default value is <code>1.0</code>.
//     * @return the audio volume
//     */
//    public final double getVolume() {
//        return volume == null ? 1.0 : volume.get();
//    }
//
//    public DoubleProperty volumeProperty() {
//        if (volume == null) {
//            volume = new DoublePropertyBase(1.0) {
//
//                @Override
//                protected void invalidated() {
//                    synchronized (disposeLock) {
//                        if (isPlaying.get()) {
//                                gainControl.setValue((float) clamp(volume.get(), 0.0, 1.0));
//                        }
//                    }
//                }
//
//                @Override
//                public Object getBean() {
//                    return AudioPlayer.this;
//                }
//
//                @Override
//                public String getName() {
//                    return "volume";
//                }
//            };
//        }
//        return volume;
//    }
//
//    private static double clamp(double dvalue, double dmin, double dmax) {
//        if (dmin != Double.MIN_VALUE && dvalue < dmin) {
//            return dmin;
//        } else if (dmax != Double.MAX_VALUE && dvalue > dmax) {
//            return dmax;
//        } else {
//            return dvalue;
//        }
//    }

    private AtomicBoolean isPlaying;

    public AudioPlayer(String source, GrabberListener grabberListener)
    {
        if (grabberListener == null) return;
        if (source.isEmpty()) return;
        isPlaying = new AtomicBoolean(false);

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
                        float[] samples = new float[channelSamplesFloatBuffer.capacity()];

                        float lastPeak = 0f;

                        for (int i = 0; i < channelSamplesFloatBuffer.capacity(); i++) {
                            short val = channelSamplesFloatBuffer.get(i);
                            outBuffer.putShort(val);
                        }

                        // convert bytes to samples here
                        for(int i = 0, s = 0; i < channelSamplesFloatBuffer.capacity();) {
                            int sample = 0;

                            sample |= channelSamplesFloatBuffer.get(i++) & 0xFF; // (reverse these two lines
                            sample |= channelSamplesFloatBuffer.get(i++) << 8;   //  if the format is big endian)

                            // normalize to range of +/-1.0f
                            samples[s++] = sample / 32768f;
                        }

                        float rms = 0f;
                        float peak = 0f;
                        for(float sample : samples) {
                            float abs = Math.abs(sample);
                            if(abs > peak) {
                                peak = abs;
                            }

                            rms += sample * sample;
                        }

                        rms = (float)Math.sqrt(rms / samples.length);

                        if(lastPeak > peak) {
                            peak = lastPeak * 0.875f;
                        }

                        lastPeak = peak;

                        grabberListener.onAudioSpectrum(rms, peak);

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
                isPlaying.set(false);
                Platform.exit();
            } catch (Exception exception) {
                LogHelper.e(TAG, exception);
                isPlaying.set(false);
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
