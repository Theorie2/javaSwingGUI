package javaSwingGUI;

public class Particle extends Sprite {
    private long spawnTime;
    private final long lifeTime;
    private float alpha;
    private final float fadeRate;

    public Particle(String imagePath, double x, double y, long lifeTimeMs, World world) {
        super(imagePath, x, y, world);
        this.lifeTime = lifeTimeMs;
        this.spawnTime = System.currentTimeMillis();
        this.alpha = 1.0f;

        this.fadeRate = 1.0f / (lifeTimeMs / 16.0f);
        setAlpha(alpha);
    }

    @Override
    public void act() {
        long now = System.currentTimeMillis();
        long age = now - spawnTime;

        if (age > lifeTime) {
            destroy();
            return;
        }

        alpha -= fadeRate;
        if (alpha < 0.0f) alpha = 0.0f;

        setAlpha(alpha);
    }
    
    /**
     * Reset particle for reuse from object pool
     */
    public void reset(double x, double y, long lifeTimeMs) {
        moveTo(x, y);
        this.spawnTime = System.currentTimeMillis();
        this.alpha = 1.0f;
        setAlpha(alpha);
        setVisible(true);
        // Reset destroyed state if needed
        if (isDestroyed()) {
            // Re-register with world for reuse
            world.addShape(this);
        }
    }
    
    /**
     * Check if particle should be returned to pool
     */
    public boolean shouldReturnToPool() {
        return isDestroyed();
    }
}
