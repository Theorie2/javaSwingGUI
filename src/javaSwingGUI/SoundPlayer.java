package javaSwingGUI;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundPlayer {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void play(String path) {
        executor.submit(() -> playInternal(path, 1.0f, false));
    }

    public static void play(String path, float volume) {
        executor.submit(() -> playInternal(path, volume, false));
    }

    public static long playBlocking(String path, float volume) {
        return playInternal(path, volume, true);
    }

    private static long playInternal(String path, float volume, boolean blocking) {
        try {
            Clip clip = SoundRegister.getClip(path);
            if (clip == null) return 0;

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float min = gain.getMinimum();
                float max = gain.getMaximum();
                float dB = (float) (20.0 * Math.log10(Math.max(0.0001, volume))); // clamp to avoid log10(0)
                gain.setValue(Math.max(min, Math.min(dB, max)));
            }

            clip.start();

            if (blocking) {
                while (!clip.isRunning()) Thread.sleep(5);
                while (clip.isRunning()) Thread.sleep(5);
                clip.close();
                return clip.getMicrosecondLength() / 1000;
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void shutdown() {
        executor.shutdownNow();
    }
}
