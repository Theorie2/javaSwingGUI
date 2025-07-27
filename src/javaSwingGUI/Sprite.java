package javaSwingGUI;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class Sprite extends Shape {
    private BufferedImage image;
    private Graphics2D g2;
    private String imagePath;
    protected float alpha = 1.0f;


    public Sprite(BufferedImage image, double x, double y, World world) {
        super(world);
        this.image = image;
        initSprite();
        this.x = x;
        this.y = y;
        this.world = world;
    }

    public Sprite(String imagePath, double x, double y, World world) {
        super(world);
        this.image = ImageRegister.getImage(imagePath);

        initSprite();
        this.x = x;
        this.y = y;
        this.world = world;
    }

    private void initSprite() {
        width = image.getWidth();
        height = image.getHeight();
        resetCenter();
    }

    // Only cache alpha composite for performance
    private AlphaComposite cachedComposite = null;
    private float lastAlpha = -1.0f;
    
    private void tick() {
        if (isVisible() && !isDestroyed() && !isOutsideView()) {
            AffineTransform oldTransform = g2.getTransform();
            Composite oldComposite = g2.getComposite();

            // Always create fresh transform to avoid position bugs
            AffineTransform transform = new AffineTransform(oldTransform);
            transform.rotate(getAngleInRadians(), getCenterX(), getCenterY());
            g2.setTransform(transform);

            // Only cache composite if alpha changed
            if (Math.abs(alpha - lastAlpha) > 0.001) {
                cachedComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                lastAlpha = alpha;
            }
            g2.setComposite(cachedComposite);

            g2.drawImage(
                    image,
                    (int) Math.round(x),
                    (int) Math.round(y),
                    (int) (width * getScaleX()),
                    (int) (height * getScaleY()),
                    null
            );

            // Reset Grafik-Kontext
            g2.setTransform(oldTransform);
            g2.setComposite(oldComposite);
        }
    }

    public void setAlpha(float alpha) {
        this.alpha = Math.max(0.0f, Math.min(1.0f, alpha)); // clamp between 0 and 1
    }

    public float getAlpha() {
        return alpha;
    }


    @Override
    public void draw(Graphics2D g2) {
        if (isVisible()) {
            this.g2 = g2;
        }
        tick();
    }

    public String toString() {
        return getClass().getName();
    }

    @Override
    public void destroy() {
        super.destroy();
        image = null;
    }

    @Override
    public void resetCenter() {
        defineCenter(x + (image.getWidth() / 2.0) * getScaleX(), y + (image.getHeight() / 2.0) * getScaleY());
    }

    @Override
    public boolean isDestroyed() {
        return image == null;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (mirroredX) {
            height = -this.image.getHeight();
            width = -this.image.getWidth();
        } else {
            height = this.image.getHeight();
            width = this.image.getWidth();
        }
    }

    public void setImage(String imagePath) {
        this.image = ImageRegister.getImage(imagePath);
        if (mirroredX) {
            height = -this.image.getHeight();
            width = -this.image.getWidth();
        } else {
            height = this.image.getHeight();
            width = this.image.getWidth();
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Doesn't work completely fine, mirroring is not working yet
     */
    public boolean collidesWith(Sprite other) {
        //Checks for more performance
        if (this.isDestroyed() || other.isDestroyed()
                || !new Rectangle((int) x, (int) y, (int)(width*getScaleX()), (int)(height*getScaleY()))
                .intersects(new Rectangle((int) other.x, (int) other.y, (int)(other.width*getScaleX()), (int)(other.height*getScaleY())))) {
            return false;
        }

        // Calculate transformation of both Sprites
        AffineTransform thisTransform = getTransform(this);
        AffineTransform otherTransform = getTransform(other);

        // spiegeln an der X-Achse (horizontal)
        //thisTransform.scale(-1, 1); ||
        //otherTransform.scale(-1, 1);
        // dann verschieben um -width, damit das Bild wieder "rechts" erscheint
        //thisTransform.translate(-width, 0);
        //otherTransform.translate(-width, 0);

        // Calculate Bounding-Boxes in World Coordinates
        Rectangle thisBounds = getTransformedBounds(this, thisTransform);
        Rectangle otherBounds = getTransformedBounds(other, otherTransform);

        // Calculate Intersection-Area
        Rectangle intersection = thisBounds.intersection(otherBounds);
        if (intersection.isEmpty()) {
            return false;
        }
        // Inverse transformation for Reversing
        AffineTransform inverseThis, inverseOther;
        try {
            inverseThis = thisTransform.createInverse();
            inverseOther = otherTransform.createInverse();
        } catch (NoninvertibleTransformException e) {
            return false;
        }
        // Iterate over all Pixels in the Intersection-Area
        for (int x = intersection.x; x < intersection.x + intersection.width; x++) {
            for (int y = intersection.y; y < intersection.y + intersection.height; y++) {
                // Transform World Coordinates in Sprite Coordinates
                Point2D thisPoint = inverseThis.transform(new Point2D.Double(x, y), null);
                Point2D otherPoint = inverseOther.transform(new Point2D.Double(x, y), null);
                if (isSolidPixel(this.image, thisPoint) && isSolidPixel(other.image, otherPoint)) {
                    return true;
                }
            }
        }
        return false;
    }

    private AffineTransform getTransform(Sprite sprite) {
        AffineTransform tx = new AffineTransform();
        tx.translate(sprite.x, sprite.y);
        tx.scale(sprite.getScaleX(), sprite.getScaleY());
        tx.rotate(sprite.getAngleInRadians(), sprite.image.getWidth() / 2.0, sprite.image.getHeight() / 2.0);
        return tx;
    }


    private Rectangle getTransformedBounds(Sprite sprite, AffineTransform transform) {
        int w = (int)(sprite.image.getWidth());
        int h = (int)(sprite.image.getHeight());

        Point2D[] corners = new Point2D[]{
                new Point2D.Double(0, 0),
                new Point2D.Double(w, 0),
                new Point2D.Double(w, h),
                new Point2D.Double(0, h)
        };

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (Point2D p : corners) {
            Point2D transformed = transform.transform(p, null);
            minX = Math.min(minX, transformed.getX());
            minY = Math.min(minY, transformed.getY());
            maxX = Math.max(maxX, transformed.getX());
            maxY = Math.max(maxY, transformed.getY());
        }

        return new Rectangle(
                (int) Math.floor(minX),
                (int) Math.floor(minY),
                (int) Math.ceil(maxX - minX),
                (int) Math.ceil(maxY - minY)
        );
    }


    private boolean isSolidPixel(BufferedImage img, Point2D p) {
        int x = (int) p.getX();
        int y = (int) p.getY();

        if (x < 0 || y < 0 || x >= img.getWidth() || y >= img.getHeight()) {
            return false;
        }

        int pixel = img.getRGB(x, y);
        int alpha = (pixel >> 24) & 0xff;

        return alpha > 0;
    }


    public BufferedImage getImage() {
        return this.image;
    }

    private void moved() {
        resetCenter();
    }

    public String getImagePath() {
        return imagePath;
    }
}