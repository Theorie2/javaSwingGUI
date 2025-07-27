package javaSwingGUI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageRegister {
    // Enum für typsichere Bildpfade
    public enum ImagePath {
        TEST("/test.png"),
        HABERMEISTER("/textures/humans/habermeister.png"),
        ALICE("/textures/humans/alice.png"),
        STEFAN("/textures/humans/stefan.png"),
        SIGHTFIELD("/textures/humans/sightfield.png"),
        MENU_BACKGROUND("/textures/menu/main_menu_display/background1.png"),
        SLIDER_KNOB("/textures/menu/slider_knob.png"),
        SLIDER_TRACK("/textures/menu/slider_track.png"),
        DROPDOWN_ENTRY("/textures/menu/dropdown_entry.png"),
        DROPDOWN_MAIN("/textures/menu/dropdown_main.png"),
        SETTINGS_BUTTON_0("/textures/menu/main_menu_display/settings_button_0.png"),
        SETTINGS_BUTTON_1("/textures/menu/main_menu_display/settings_button_1.png"),
        START_BUTTON_0("/textures/menu/main_menu_display/start_button_0.png"),
        START_BUTTON_1("/textures/menu/main_menu_display/start_button_1.png"),
        BACK_BUTTON("/textures/menu/settings_display/back_button.png"),
        SETTINGS_BACKGROUND("/textures/menu/settings_display/background.png"),
        CONTROLS_BUTTON("/textures/menu/settings_display/controls_button.png"),
        MUSIC_VOLUME("/textures/menu/settings_display/music_volume.png"),
        SOUND_EFFECT_VOLUME("/textures/menu/settings_display/sound_effect_volume.png"),
        PARTICLE_SMELL("/textures/particles/smell.png"),
        PARTICLE_SMOKE("/textures/particles/smoke.png"),
        PARTICLE_ATTANTION_MARK("/textures/particles/attantion_mark.png"),
        ITEM_BOX("/textures/items/item_box.png"),
        ENERGY("/textures/items/energy.png"),
        PAPE1("/textures/items/pape1.png"),
        GUM_PAPER("/textures/items/gum_paper.png"),
        GUM("/textures/items/gum.png"),
        BOOK("/textures/items/book.png"),
        ALPHABET("/textures/generell/alphabet.png"),
        ALPHABET2("/textures/generell/alphabet2.png"),
        NINA_AMARSCH("/textures/players/nina_amarsch.png"),
        NINA_AMARSCH_SKAL("/textures/players/nina_amarsch_skal.png"),
        FLOOR("/textures/mapElements/floor.png"),
        FLOOR_WOOD("/textures/mapElements/floor_wood.png"),
        FLOOR_SCIENCE("/textures/mapElements/floor_science.png"),
        FLOOR_HALL("/textures/mapElements/floor_hall.png"),
        GRASS("/textures/mapElements/grass.png"),
        WALL("/textures/mapElements/wall.png"),
        STAIR("/textures/mapElements/stair.png"),
        DOOR_CLOSED("/textures/mapElements/door_closed.png"),
        DOOR_OPEN("/textures/mapElements/door_open.png"),
        PLAYGROUND("/textures/mapElements/playground.png"),
        EMPTY("/textures/empty.png"),
        
        // Alphabet letters - uppercase
        LETTER_A("/textures/generell/alphabet_letters/big/A.png"),
        LETTER_B("/textures/generell/alphabet_letters/big/B.png"),
        LETTER_C("/textures/generell/alphabet_letters/big/C.png"),
        LETTER_D("/textures/generell/alphabet_letters/big/D.png"),
        LETTER_E("/textures/generell/alphabet_letters/big/E.png"),
        LETTER_F("/textures/generell/alphabet_letters/big/F.png"),
        LETTER_G("/textures/generell/alphabet_letters/big/G.png"),
        LETTER_H("/textures/generell/alphabet_letters/big/H.png"),
        LETTER_I("/textures/generell/alphabet_letters/big/I.png"),
        LETTER_J("/textures/generell/alphabet_letters/big/J.png"),
        LETTER_K("/textures/generell/alphabet_letters/big/K.png"),
        LETTER_L("/textures/generell/alphabet_letters/big/L.png"),
        LETTER_M("/textures/generell/alphabet_letters/big/M.png"),
        LETTER_N("/textures/generell/alphabet_letters/big/N.png"),
        LETTER_O("/textures/generell/alphabet_letters/big/O.png"),
        LETTER_P("/textures/generell/alphabet_letters/big/P.png"),
        LETTER_Q("/textures/generell/alphabet_letters/big/Q.png"),
        LETTER_R("/textures/generell/alphabet_letters/big/R.png"),
        LETTER_S("/textures/generell/alphabet_letters/big/S.png"),
        LETTER_T("/textures/generell/alphabet_letters/big/T.png"),
        LETTER_U("/textures/generell/alphabet_letters/big/U.png"),
        LETTER_V("/textures/generell/alphabet_letters/big/V.png"),
        LETTER_W("/textures/generell/alphabet_letters/big/W.png"),
        LETTER_X("/textures/generell/alphabet_letters/big/X.png"),
        LETTER_Y("/textures/generell/alphabet_letters/big/Y.png"),
        LETTER_Z("/textures/generell/alphabet_letters/big/Z.png"),
        
        // Alphabet letters - lowercase
        LETTER_a("/textures/generell/alphabet_letters/small/a.png"),
        LETTER_b("/textures/generell/alphabet_letters/small/b.png"),
        LETTER_c("/textures/generell/alphabet_letters/small/c.png"),
        LETTER_d("/textures/generell/alphabet_letters/small/d.png"),
        LETTER_e("/textures/generell/alphabet_letters/small/e.png"),
        LETTER_f("/textures/generell/alphabet_letters/small/f.png"),
        LETTER_g("/textures/generell/alphabet_letters/small/g.png"),
        LETTER_h("/textures/generell/alphabet_letters/small/h.png"),
        LETTER_i("/textures/generell/alphabet_letters/small/i.png"),
        LETTER_j("/textures/generell/alphabet_letters/small/j.png"),
        LETTER_k("/textures/generell/alphabet_letters/small/k.png"),
        LETTER_l("/textures/generell/alphabet_letters/small/l.png"),
        LETTER_m("/textures/generell/alphabet_letters/small/m.png"),
        LETTER_n("/textures/generell/alphabet_letters/small/n.png"),
        LETTER_o("/textures/generell/alphabet_letters/small/o.png"),
        LETTER_p("/textures/generell/alphabet_letters/small/p.png"),
        LETTER_q("/textures/generell/alphabet_letters/small/q.png"),
        LETTER_r("/textures/generell/alphabet_letters/small/r.png"),
        LETTER_s("/textures/generell/alphabet_letters/small/s.png"),
        LETTER_t("/textures/generell/alphabet_letters/small/t.png"),
        LETTER_u("/textures/generell/alphabet_letters/small/u.png"),
        LETTER_v("/textures/generell/alphabet_letters/small/v.png"),
        LETTER_w("/textures/generell/alphabet_letters/small/w.png"),
        LETTER_x("/textures/generell/alphabet_letters/small/x.png"),
        LETTER_y("/textures/generell/alphabet_letters/small/y.png"),
        LETTER_z("/textures/generell/alphabet_letters/small/z.png"),
        
        // Numbers
        NUMBER_0("/textures/generell/alphabet_letters/numbers/0.png"),
        NUMBER_1("/textures/generell/alphabet_letters/numbers/1.png"),
        NUMBER_2("/textures/generell/alphabet_letters/numbers/2.png"),
        NUMBER_3("/textures/generell/alphabet_letters/numbers/3.png"),
        NUMBER_4("/textures/generell/alphabet_letters/numbers/4.png"),
        NUMBER_5("/textures/generell/alphabet_letters/numbers/5.png"),
        NUMBER_6("/textures/generell/alphabet_letters/numbers/6.png"),
        NUMBER_7("/textures/generell/alphabet_letters/numbers/7.png"),
        NUMBER_8("/textures/generell/alphabet_letters/numbers/8.png"),
        NUMBER_9("/textures/generell/alphabet_letters/numbers/9.png"),
        
        // Special characters
        SPECIAL_EXCLAMATION("/textures/generell/alphabet_letters/special/!.png"),
        SPECIAL_DOLLAR("/textures/generell/alphabet_letters/special/$.png"),
        SPECIAL_PERCENT("/textures/generell/alphabet_letters/special/%.png"),
        SPECIAL_AMPERSAND("/textures/generell/alphabet_letters/special/&.png"),
        SPECIAL_PARENTHESIS_OPEN("/textures/generell/alphabet_letters/special/(.png"),
        SPECIAL_PARENTHESIS_CLOSE("/textures/generell/alphabet_letters/special/).png"),
        SPECIAL_PLUS("/textures/generell/alphabet_letters/special/+.png"),
        SPECIAL_MINUS("/textures/generell/alphabet_letters/special/-.png"),
        SPECIAL_EQUALS("/textures/generell/alphabet_letters/special/=.png"),
        SPECIAL_QUOTES("/textures/generell/alphabet_letters/special/anfuehrungszeichen.png"),
        SPECIAL_COLON("/textures/generell/alphabet_letters/special/doppelpunkt.png"),
        SPECIAL_QUESTION("/textures/generell/alphabet_letters/special/fragezeichen.png"),
        SPECIAL_COMMA("/textures/generell/alphabet_letters/special/komma.png"),
        SPECIAL_PERIOD("/textures/generell/alphabet_letters/special/punkt.png"),
        SPECIAL_SLASH("/textures/generell/alphabet_letters/special/slash.png"),
        SPECIAL_ASTERISK("/textures/generell/alphabet_letters/special/stern.png"),
        SPECIAL_SEMICOLON("/textures/generell/alphabet_letters/special/strichpunkt.png"),
        SPECIAL_EURO("/textures/generell/alphabet_letters/special/€.png");

        private final String path;

        ImagePath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    // Thread-safe Maps for better performance
    private static final Map<String, BufferedImage> cache = new ConcurrentHashMap<>();
    private static final Map<ImagePath, BufferedImage> enumCache = new EnumMap<>(ImagePath.class);
    private static final AtomicBoolean loaded = new AtomicBoolean(false);
    private static final AtomicBoolean loading = new AtomicBoolean(false);

    // Executor for asynchron loading
    private static final ExecutorService executor = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "ImageLoader");
        t.setDaemon(true);
        return t;
    });

    private static BufferedImage fallbackImage;

    static {
        initializeFallbackImage();
    }

    private static void initializeFallbackImage() {
        try (InputStream stream = ImageRegister.class.getResourceAsStream(ImagePath.EMPTY.getPath())) {
            if (stream != null) {
                fallbackImage = ImageIO.read(stream);
            }
        } catch (IOException e) {
            //Creates as last fallback a 1x1 image
            fallbackImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
    }

    /**
     * Loads all images synchron
     */
    public static void load() {
        if (loaded.get() || !loading.compareAndSet(false, true)) {
            return;
        }

        try {
            loadImages();
            loaded.set(true);
            LOGGER.log("Loaded " + cache.size() + " textures successfully");
        } finally {
            loading.set(false);
        }
    }

    /**
     * loads all images asynchron
     */
    public static CompletableFuture<Void> loadAsync() {
        return CompletableFuture.runAsync(() -> load(), executor);
    }

    private static void loadImages() {
        for (ImagePath imagePath : ImagePath.values()) {
            BufferedImage img = loadSingleImage(imagePath.getPath());
            if (img != null) {
                cache.put(imagePath.getPath(), img);
                enumCache.put(imagePath, img);
            }
        }
    }

    private static BufferedImage loadSingleImage(String path) {
        try (InputStream stream = ImageRegister.class.getResourceAsStream(path)) {
            if (stream == null) {
                LOGGER.log("Image resource not found: " + path);
                return null;
            }
            return ImageIO.read(stream);
        } catch (IOException e) {
            LOGGER.log("Failed to load image: " + path + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets an image by path
     */
    public static BufferedImage getImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            return fallbackImage;
        }

        BufferedImage img = cache.get(path);
        if (img != null) {
            return img;
        }

        // Lazy Loading
        return cache.computeIfAbsent(path, p -> {
            BufferedImage loadedImg = loadSingleImage(p);
            if (loadedImg != null) {
                LOGGER.log("Lazy-loaded image: " + p);
                return loadedImg;
            }
            return fallbackImage;
        });
    }

    /**
     * Typsichere Methode to get Images
     */
    public static BufferedImage getImage(ImagePath imagePath) {
        if (imagePath == null) {
            return fallbackImage;
        }

        BufferedImage img = enumCache.get(imagePath);
        if (img != null) {
            return img;
        }

        // fallback
        return getImage(imagePath.getPath());
    }

    /**
     * Preloadeds a specific number of Images
     */
    public static CompletableFuture<Void> preloadImages(ImagePath... imagePaths) {
        return CompletableFuture.runAsync(() -> {
            for (ImagePath path : imagePaths) {
                getImage(path);
            }
        }, executor);
    }

    public static boolean isLoaded() {
        return loaded.get();
    }

    public static boolean isLoading() {
        return loading.get();
    }

    /**
     * Frees Ram
     */
    public static void unloadAll() {
        cache.clear();
        enumCache.clear();
        loaded.set(false);
        loading.set(false);
        System.gc(); // Hinweis für Garbage Collection
        LOGGER.log("Unloaded all textures");
    }

    /**
     * Gives Memory stats after loading
     */
    public static String getMemoryStats() {
        return String.format("Loaded images: %d, Enum cache: %d, Loading: %s",
                cache.size(), enumCache.size(), loading.get());
    }

    /**
     * unloads specific images
     */
    public static void unloadImage(String path) {
        cache.remove(path);
    }

    public static void unloadImage(ImagePath imagePath) {
        cache.remove(imagePath.getPath());
        enumCache.remove(imagePath);
    }

    public static Map<String, BufferedImage> getAllImages() {
        return Collections.unmodifiableMap(cache);
    }

    /**
     * Shutdown of the Executor
     */
    public static void shutdown() {
        executor.shutdown();
    }
}