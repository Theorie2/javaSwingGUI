package javaSwingGUI;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundRegister {

    private static final Map<String, byte[]> audioData = new HashMap<>();
    private static final Map<String, AudioFormat> formats = new HashMap<>();
    private static boolean loaded = false;

    //Add here .wav files before using them
    public static String[] soundPaths = new String[] {
    };

    public synchronized static void load() {
        for (String path : soundPaths) {
            try {
                File file = new File(path);  // oder: getResourceAsStream(path)
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = stream.getFormat();
                byte[] data = stream.readAllBytes();

                audioData.put(path, data);
                formats.put(path, format);
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
                LOGGER.log("Sound loading failed for: " + path);
            }
        }
        loaded = true;
        LOGGER.log("Loaded sounds correctly");
    }

    public static Clip getClip(String path) {
        try {
            // Fallback bei fehlender Datei
            File file = new File(path);
            if (!file.exists()) {
                LOGGER.log("Sound file not found: " + path);
                return null;
            }

            // Lazy loading
            if (!audioData.containsKey(path)) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = stream.getFormat();
                byte[] data = stream.readAllBytes();
                audioData.put(path, data);
                formats.put(path, format);
            }

            byte[] data = audioData.get(path);
            AudioFormat format = formats.get(path);

            if (data == null || format == null) {
                LOGGER.log("No audio data for: " + path);
                return null;
            }

            AudioInputStream copy = new AudioInputStream(
                    new ByteArrayInputStream(data),
                    format,
                    data.length / format.getFrameSize()
            );

            Clip clip = AudioSystem.getClip();
            clip.open(copy);
            return clip;

        } catch (Exception e) {
            LOGGER.log("Could not create clip for " + path + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }


    public static boolean isLoaded() {
        return loaded;
    }
}
