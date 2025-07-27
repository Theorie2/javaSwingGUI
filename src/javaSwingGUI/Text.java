package javaSwingGUI;

import java.awt.Shape;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Text extends Shape {

    private String text;
    private Color color;
    private Map<Character, BufferedImage> letterImages;
    private BufferedImage bufferedImageText;

    public Text(World world, int x, int y, String text) {
        super(world);
        this.text = text;
        this.x = x;
        this.y = y;
        this.world = world;
        color = Color.GRAY;
        letterImages = new HashMap<>();
        loadLetterImages();
        bufferedImageText = createTextToImageWithoutColor(text);
    }

    public Text(World world, int x, int y, String text, Color color) {
        this(world, x, y, text);
        this.color = color;
        bufferedImageText = createTextToImage(text);
    }

    private void loadLetterImages() {
        // Load uppercase letters
        for (char c = 'A'; c <= 'Z'; c++) {
            BufferedImage img = ImageRegister.getImage("/textures/generell/alphabet_letters/big/" + c + ".png");
            if (img != null) {
                letterImages.put(c, img);
            }
        }
        
        // Load lowercase letters
        for (char c = 'a'; c <= 'z'; c++) {
            BufferedImage img = ImageRegister.getImage("/textures/generell/alphabet_letters/small/" + c + ".png");
            if (img != null) {
                letterImages.put(c, img);
            }
        }
        for (char c = '0'; c <= '9'; c++) {
            BufferedImage img = ImageRegister.getImage("/textures/generell/alphabet_letters/numbers/" + c + ".png");
            if (img != null) {
                letterImages.put(c, img);
            }
        }
        
        // Load special characters with null checks
        loadSpecialChar('!', "/textures/generell/alphabet_letters/special/!.png");
        loadSpecialChar('$', "/textures/generell/alphabet_letters/special/$.png");
        loadSpecialChar('%', "/textures/generell/alphabet_letters/special/%.png");
        loadSpecialChar('&', "/textures/generell/alphabet_letters/special/&.png");
        loadSpecialChar('(', "/textures/generell/alphabet_letters/special/(.png");
        loadSpecialChar(')', "/textures/generell/alphabet_letters/special/).png");
        loadSpecialChar('+', "/textures/generell/alphabet_letters/special/+.png");
        loadSpecialChar('-', "/textures/generell/alphabet_letters/special/-.png");
        loadSpecialChar('=', "/textures/generell/alphabet_letters/special/=.png");
        loadSpecialChar('"', "/textures/generell/alphabet_letters/special/anfuehrungszeichen.png");
        loadSpecialChar(':', "/textures/generell/alphabet_letters/special/doppelpunkt.png");
        loadSpecialChar('?', "/textures/generell/alphabet_letters/special/fragezeichen.png");
        loadSpecialChar(',', "/textures/generell/alphabet_letters/special/komma.png");
        loadSpecialChar('.', "/textures/generell/alphabet_letters/special/punkt.png");
        loadSpecialChar('/', "/textures/generell/alphabet_letters/special/slash.png");
        loadSpecialChar('*', "/textures/generell/alphabet_letters/special/stern.png");
        loadSpecialChar(';', "/textures/generell/alphabet_letters/special/strichpunkt.png");
        loadSpecialChar('€', "/textures/generell/alphabet_letters/special/€.png");
    }
    
    private void loadSpecialChar(char c, String path) {
        BufferedImage img = ImageRegister.getImage(path);
        if (img != null) {
            letterImages.put(c, img);
        }
    }

    @Override
    public int getHeight() {
        return bufferedImageText != null ? bufferedImageText.getHeight() : 0;
    }
    @Override
    public int getWidth() {
        return bufferedImageText != null ? bufferedImageText.getWidth() : 0;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isVisible()) {
            super.draw(g2);
            g2.drawImage(bufferedImageText, (int) x, (int) y, (int) (width * getScaleX()), (int) (height * getScaleY()), null);
        }
    }

    public void setColor(Color color) {
        this.color = color;
        bufferedImageText = createTextToImage(text);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        bufferedImageText = createTextToImageWithoutColor(text);
    }

    private BufferedImage createTextToImage(String text) {
        String[] lines = text.split("\n");
        
        // Calculate dimensions
        int maxLineWidth = 0;
        int lineHeight = 0;
        
        for (String line : lines) {
            int currentLineWidth = 0;
            for (char c : line.toCharArray()) {
                if (c == ' ') {
                    currentLineWidth += getSpaceWidth();
                } else {
                    BufferedImage letterImg = letterImages.get(c);
                    if (letterImg != null) {
                        currentLineWidth += letterImg.getWidth();
                        if (letterImg.getHeight() > lineHeight) {
                            lineHeight = letterImg.getHeight();
                        }
                    } else {
                        // Use space width as fallback for unsupported characters
                        currentLineWidth += getSpaceWidth();
                    }
                }
            }
            if (currentLineWidth > maxLineWidth) {
                maxLineWidth = currentLineWidth;
            }
        }

        BufferedImage result = new BufferedImage(Math.max(1, maxLineWidth), Math.max(1, lines.length * lineHeight), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = result.createGraphics();
        width = result.getWidth();
        height = result.getHeight();

        for (int line = 0; line < lines.length; line++) {
            int currentX = 0;
            for (char c : lines[line].toCharArray()) {
                if (c == ' ') {
                    currentX += getSpaceWidth();
                } else {
                    BufferedImage letterImg = letterImages.get(c);
                    if (letterImg != null) {
                        BufferedImage coloredLetter = applyColor(letterImg, color);
                        g2d.drawImage(coloredLetter, currentX, line * lineHeight, null);
                        currentX += letterImg.getWidth();
                    } else {
                        // Log unsupported character and use space width as fallback
                        LOGGER.log("Unsupported character in Text class: '" + c + "' (ASCII: " + (int)c + ")");
                        currentX += getSpaceWidth();
                    }
                }
            }
        }
        
        g2d.dispose();
        return result;
    }
    private BufferedImage createTextToImageWithoutColor(String text) {
        String[] lines = text.split("\n");
        
        // Calculate dimensions
        int maxLineWidth = 0;
        int lineHeight = 0;
        
        for (String line : lines) {
            int currentLineWidth = 0;
            for (char c : line.toCharArray()) {
                if (c == ' ') {
                    currentLineWidth += getSpaceWidth();
                } else {
                    BufferedImage letterImg = letterImages.get(c);
                    if (letterImg != null) {
                        currentLineWidth += letterImg.getWidth();
                        if (letterImg.getHeight() > lineHeight) {
                            lineHeight = letterImg.getHeight();
                        }
                    } else {
                        // Use space width as fallback for unsupported characters
                        currentLineWidth += getSpaceWidth();
                    }
                }
            }
            if (currentLineWidth > maxLineWidth) {
                maxLineWidth = currentLineWidth;
            }
        }

        BufferedImage result = new BufferedImage(Math.max(1, maxLineWidth), Math.max(1, lines.length * lineHeight), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = result.createGraphics();
        width = result.getWidth();
        height = result.getHeight();

        for (int line = 0; line < lines.length; line++) {
            int currentX = 0;
            for (char c : lines[line].toCharArray()) {
                if (c == ' ') {
                    currentX += getSpaceWidth();
                } else {
                    BufferedImage letterImg = letterImages.get(c);
                    if (letterImg != null) {
                        g2d.drawImage(letterImg, currentX, line * lineHeight, null);
                        currentX += letterImg.getWidth();
                    } else {
                        // Use space width as fallback for unsupported characters
                        currentX += getSpaceWidth();
                    }
                }
            }
        }
        
        g2d.dispose();
        return result;
    }
    
    private BufferedImage applyColor(BufferedImage original, Color color) {
        BufferedImage colored = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = colored.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, original.getWidth(), original.getHeight());
        g2d.setComposite(AlphaComposite.DstIn);
        g2d.drawImage(original, 0, 0, null);
        g2d.dispose();
        return colored;
    }
    
    private int getSpaceWidth() {
        // Try to get a representative letter for space width calculation
        BufferedImage sampleLetter = letterImages.get('a');
        if (sampleLetter == null) {
            sampleLetter = letterImages.get('A');
        }
        if (sampleLetter == null && !letterImages.isEmpty()) {
            sampleLetter = letterImages.values().iterator().next();
        }
        return sampleLetter != null ? sampleLetter.getWidth() / 2 : 10;
    }

}
