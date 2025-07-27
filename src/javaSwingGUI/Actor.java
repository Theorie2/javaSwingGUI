package javaSwingGUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public abstract class Actor{
    protected World world;
    private boolean destroyed;
    protected boolean mirroredX;
    protected boolean mirroredY;
    private boolean isActing;

    protected Group group;

    public Actor(World world){
        this.world = world;
        isActing = true;
        destroyed = false;
        mirroredX = false;
        mirroredY = false;
        if (!(this instanceof Shape)){
        world.addShape(this);
        }
    }

    public World getWorld(){
        return world;
    }


    public void changeWorld(World world){
        this.world = world;
    }



    public void destroy(){
        destroyed = true;
        world.removeShape(this);
    }

    public boolean isDestroyed(){
        return destroyed;
    }


    public void bringToFront(){
        world.bringToFront(this);
    }

    public static class KeyHandler implements KeyListener {
        private final Set<Integer> pressedKeys;

        public KeyHandler() {
            this(10);
        }

        public KeyHandler(int maxKeysAtTheSameTime) {
            // HashSet für O(1) Lookup-Performance
            pressedKeys = new HashSet<>(maxKeysAtTheSameTime);
        }

        public boolean isKeyPressed(int keyCode) {
            return pressedKeys.contains(keyCode);
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            pressedKeys.add(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            pressedKeys.remove(e.getKeyCode());
        }
    }

    /**
     * Method to overwrite and put game-code in; Normally called 40 times per second
     */
    public void act(){

    }

    /**
     *Used for the paint loop
     */
    @World.UseWithCaution
    public void draw(Graphics2D g2){
        //if (this.isActing()) {
        //    act();
        //}
    }
    public void move(double dx,double dy){

    }

    public boolean isActing(){
        return this.isActing;
    }

    public void setActing(boolean shouldAct){
        this.isActing = shouldAct;
    }
}
