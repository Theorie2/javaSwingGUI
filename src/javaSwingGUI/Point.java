package javaSwingGUI;

import java.awt.*;

public class Point extends Shape{
    protected Color color = Color.BLACK;
    public Point(int x, int y,World world) {
        super(world);
        this.x = x;
        this.y = y;
    }
    @Override
    public void draw(Graphics2D g2) {
        if (isVisible() && !isDestroyed() && !isOutsideView()
            ) {
            resetCenter();
            g2.setColor(color);
            g2.drawLine((int)x, (int)y, (int)x, (int)y);
        }
    }



    public String toString(){
        return getClass().getName();
    }

    @Override
    public void destroy() {
        super.destroy();
        color = null;
    }

    @Override
    public boolean isDestroyed() {
        return getParentGroup() != null;//Das ist richtig so!!!
    }

    public synchronized void setColor(Color color){
        this.color=color;
    }

}
