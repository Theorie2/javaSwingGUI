package javaSwingGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class World {
    private int width;
    private int height;
    private JFrame window;
    private WorldPanel panel;
    private final String name;
    private Group allShapes;
    private Actor.KeyHandler keyHandler;
    private Ticker gameTicker;
    private GraphicsDevice device;
    private boolean inFullScreen;

    public World() {
        this("");
    }

    public World(String name) {
        this(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, name);
    }

    public World(int width, int height, String name) {
        this.width = width;
        this.height = height;
        initAttributes();
        this.name = name;
        panel.setPreferredSize(new Dimension(width, height));
        initWindow();
    }


    private void initAttributes() {
        window = new JFrame();
        panel = new WorldPanel(this);
        gameTicker = new Ticker();
        allShapes = new Group(this, true);
        keyHandler = new Actor.KeyHandler() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_F11) {
                    switchFullScreen();
                }
            }
        };
        this.addKeyListener(keyHandler);
        inFullScreen = false;
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    public void addWindowAdapter(WindowAdapter windowAdapter) {
        window.addWindowListener(windowAdapter);
    }

    public void setDefaultCloseOperation(int closeOperation) {
        window.setDefaultCloseOperation(closeOperation);
    }

    public void setResizeable(boolean yn) {
        window.setResizable(yn);
    }

    public Actor.KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public void setSize(int width, int height) {
        window.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    private void initWindow() {
        if (!ImageRegister.isLoaded()) {
            ImageRegister.load();
        }
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle(name);


        panel.setBackground(Color.BLACK);
        panel.setDoubleBuffered(true);
        // Additional performance settings
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.accthreshold", "0");
        panel.setFocusable(true);
        panel.requestFocus();

        window.add(panel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void startDrawThread() {
        panel.startDrawThread();
    }

    public void startTicking() {
        gameTicker.startTicker();
    }

    public void addMouseListener(MouseListener listener) {
        panel.addMouseListener(listener);
    }
    public void removeMouseListener(MouseListener listener) {
        panel.removeMouseListener(listener);
    }
    public void addMouseMotionListener(MouseMotionListener listener) {
        panel.addMouseMotionListener(listener);
    }
    public void removeMouseMotionListener(MouseMotionListener listener) {
        panel.removeMouseMotionListener(listener);
    }
    public void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
        panel.addMouseWheelListener(mouseWheelListener);
    }
    public void removeMouseWheelListener(MouseWheelListener mouseWheelListener) {
        panel.removeMouseWheelListener(mouseWheelListener);
    }
    public void addKeyListener(KeyListener listener) {
        panel.addKeyListener(listener);
    }


    public void clear() {
        allShapes.destroy();
    }

    public int getHeight() {
        return panel.getHeight();
    }

    public int getWidth() {
        return panel.getWidth();
    }

    public void setBackgroundColor(Color color) {
        panel.setBackground(color);
    }

    public Color getBackgroundColor() {
        return panel.getBackground();
    }
    //public void setCoordinateSystem(double left, double top, double width, double height)

    public void act() {
        allShapes.act();
    }

    public void draw(Graphics2D g2) {
        allShapes.draw(g2);
    }

    public void addShape(Actor shape) {
        allShapes.add(shape);
    }

    public void removeShape(Actor shape) {
        allShapes.remove(shape);
    }

    public Group getAllShapes() {
        return allShapes;
    }

    public void maximize() {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
    }
    
    public boolean isMaximized() {
        return (window.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
    }

    public synchronized void setFullScreen() {
        if (!inFullScreen && window != null) {
            window.dispose();
            window.setUndecorated(true);
            window.setResizable(false);
            device.setFullScreenWindow(window);
            inFullScreen = true;
            panel.requestFocus();
        }
    }

    public synchronized void endFullScreen() {
        if (inFullScreen && window != null) {
            device.setFullScreenWindow(null);
            window.dispose();
            window.setUndecorated(false);
            window.setResizable(true);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            panel.requestFocus();
            inFullScreen = false;
            maximize();
        }
    }


    public synchronized void switchFullScreen() {
        if (inFullScreen) {
            endFullScreen();
        } else {
            setFullScreen();
        }
    }

    public void bringToFront(Actor actor) {
        allShapes.bringToFront(actor);
    }

    public void removeKeyListener(KeyListener keyListener) {
        panel.removeKeyListener(keyListener);
    }



    /*
    public void move(double x, double y)
    public void rotate(double angleInDeg, double x, double y)
    public void scale(double factor, double x, double y)
    public void setCursor(String cursor)
    */

    private class Ticker implements Runnable {

        public int tps = 30; // Reduced for better performance
        private final double drawInterval = 1000000000.0 / tps;
        private Thread tickThread;

        public void startTicker() {
            tickThread = new Thread(this);
            tickThread.start();
            LOGGER.log("Started Game-Ticker!");
        }

        public void stopTicker() {
            tickThread.interrupt();
            LOGGER.log("Stopped Game-Ticker!");
        }

        @Override
        public void run() {
            double delta = 0;
            long lastTime = System.nanoTime();
            long currentTime;

            long timer = 0;

            while (tickThread != null) {

                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                timer += (currentTime - lastTime);
                lastTime = currentTime;

                if (delta >= 1) {
                    act();
                    delta--;
                }

                if (timer >= 1000000000) {
                    timer = 0;
                }
            }
        }
    }

    private class WorldPanel extends JPanel implements Runnable {

        //Screen settings
        public double fps = 60;
        private final double drawInterval = 1000000000.0 / fps;
        private Thread drawThread;

        private World w;

        public WorldPanel(World world) {
            w = world;
        }

        public void startDrawThread() {
            drawThread = new Thread(this);
            drawThread.start();
            LOGGER.log("Started Draw-Ticker!");
        }


        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            
            // Optimize rendering hints for performance
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

            w.draw(g2);
            g2.dispose();
        }

        @Override
        public void run() {
            double delta = 0;
            long lastTime = System.nanoTime();
            long currentTime;

            long timer = 0;
            //int drawCount = 0;

            while (drawThread != null) {

                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                timer += (currentTime - lastTime);
                lastTime = currentTime;

                if (delta >= 1) {
                    repaint();
                    delta--;
                    //drawCount++;
                }

                if (timer >= 1000000000) {
                    //System.out.println("FPS: "+ drawCount);
                    //drawCount = 0;
                    timer = 0;
                }

            }
        }
    }

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface UseWithCaution {
        String value() default "This Method can show unexpected behavior";
    }
}
