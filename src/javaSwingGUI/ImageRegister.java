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
        SPECIAL_EXCLAMATION("/textures/general/alphabet_letters/special/!.png"),
        SPECIAL_DOLLAR("/textures/general/alphabet_letters/special/$.png"),
        SPECIAL_PERCENT("/textures/general/alphabet_letters/special/%.png"),
        SPECIAL_AMPERSAND("/textures/general/alphabet_letters/special/&.png"),
        SPECIAL_PARENTHESIS_OPEN("/textures/general/alphabet_letters/special/(.png"),
        SPECIAL_PARENTHESIS_CLOSE("/textures/general/alphabet_letters/special/).png"),
        SPECIAL_PLUS("/textures/general/alphabet_letters/special/+.png"),
        SPECIAL_MINUS("/textures/general/alphabet_letters/special/-.png"),
        SPECIAL_EQUALS("/textures/general/alphabet_letters/special/=.png"),
        SPECIAL_QUOTES("/textures/general/alphabet_letters/special/quotation_marks.png"),
        SPECIAL_COLON("/textures/general/alphabet_letters/special/colon.png"),
        SPECIAL_QUESTION("/textures/general/alphabet_letters/special/question_mark.png"),
        SPECIAL_COMMA("/textures/general/alphabet_letters/special/comma.png"),
        SPECIAL_PERIOD("/textures/general/alphabet_letters/special/period.png"),
        SPECIAL_SLASH("/textures/general/alphabet_letters/special/slash.png"),
        SPECIAL_ASTERISK("/textures/general/alphabet_letters/special/asterisk.png"),
        SPECIAL_SEMICOLON("/textures/general/alphabet_letters/special/semicolon.png"),
        SPECIAL_EURO("/textures/general/alphabet_letters/special/€.png");


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
        try (InputStream stream = ImageRegister.class.getResourceAsStream(ImagePath.TEST.getPath())) {
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