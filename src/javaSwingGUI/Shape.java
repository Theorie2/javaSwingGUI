package javaSwingGUI;

import java.awt.*;

public abstract class Shape extends Actor {
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    private double scaleX;
    private double scaleY;
    private double angle;
    private boolean visible;
    private double centerX;
    private double centerY;
    private boolean isOutsideView;
    private final boolean isParentGroup;
    protected World world;

    public Shape(World world) {
        super(world);
        isParentGroup = false;
        this.world = world;
        world.addShape(this);
        initShape();
    }

    public Shape(World world, boolean isParentGroup) {
        super(world);
        this.isParentGroup = isParentGroup;
        if (!isParentGroup) {
            world.addShape(this);
        }
        this.world = world;
        initShape();
    }

    private void initShape() {
        scaleX = 1.0;
        scaleY = 1.0;
        angle = 0;
        visible = true;
        centerX = x + (width / 2.0);
        centerY = y + (height / 2.0);
        calcOutsideView();
    }

    public boolean collidesWith(Shape object) {
        return new Rectangle(width, height).intersects(new Rectangle(object.width, object.height));
    }

    public boolean containsPoint(double nX, double nY) {
        return this.x <= nX && this.x + width*getScaleX() >= nX && this.y <= nY && this.y + height*getScaleY() >= nY;
    }

    public Shape copy() {
        return this;
    }

    public void forward(double distance) {
        move(Math.cos(angle) * distance, Math.sin(angle) * distance);
    }

    public double getCenterX() {
        resetCenter();
        return centerX;
    }

    public double getCenterY() {
        resetCenter();
        return centerY;
    }

    public Group getParentGroup() {
        return group;
    }

    public double getScaleX() {
        return scaleX;
    }
    public double getScaleY() {
        return scaleY;
    }

    public boolean isOutsideView() {
        return isOutsideView;
    }


    //Doesn't work yet
    protected void calcOutsideView() {
        int viewWidth = world.getWidth();
        int viewHeight = world.getHeight();
        isOutsideView = (x + width*getScaleX() < 0 || x > viewWidth || y + height*getScaleY() < 0 || y > viewHeight);
    }

    public boolean isVisible() {
        return visible;
    }


    /**
     * Doesn't work for Sprites yet if you want that collishenchecks work too
     * (Also mirrorY, dMirrorX and dMirrorY)
     */
    @World.UseWithCaution
    public void mirrorX() {
        mirroredX = !mirroredX;
        width *= -1;
        calcOutsideView();
    }

    @World.UseWithCaution
    public void mirrorY() {
        mirroredY = !mirroredY;
        height *= -1;
        calcOutsideView();
    }

    /**
     * dynamic Mirror method that also moves the Shape
     */
    @World.UseWithCaution
    public void dMirrorX() {
        mirroredX = !mirroredX;
        width *= -1;
        x -= width / 2.0;
        calcOutsideView();
    }

    @World.UseWithCaution
    public void dMirrorY() {
        mirroredY = !mirroredY;
        height *= -1;
        y -= height / 2.0;
        calcOutsideView();
    }

    /**
     * Moves the Object dynamic and relative to its Coordinates before
     */
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        calcOutsideView();
    }

    /**
     * Moves a Object directly to this Postion
     */
    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
        calcOutsideView();
    }


    public void rotate(double angleInDeg) {
        angle = angle + Math.toRadians(angleInDeg);
        angle %= (Math.PI*2);
        calcOutsideView();
    }
    public void rotateInRad(double angleRad) {
        angle = angle + Math.toRadians(angleRad);
        angle %= (Math.PI*2);
        calcOutsideView();
    }

    public void setAngle(double angleDeg) {
        angle = Math.toRadians(angleDeg);
        angle %= (Math.PI*2);
        calcOutsideView();
    }

    public void setAngleRad(double angleRad) {
        angle = angleRad;
        angle %= (Math.PI*2);
        calcOutsideView();
    }

    public double getAngleInDegree() {
        return Math.toDegrees(angle);
    }

    public double getAngleInRadians() {
        return (angle);
    }

    public void sendToBack() {

    }

    public void scale(double factor) {
        scaleX *= factor;
        scaleY *= factor;
        calcOutsideView();
    }
    public void scale(double factorX,double factorY) {
        scaleX *= factorX;
        scaleY *= factorY;
        calcOutsideView();
    }

    /**
     * A dynamic scale method that also moves the Shape
     */
    public void dScale(double factor) {
        scaleX *= factor;
        scaleY *= factor;
        x -= (int) (width / 2.0 * scaleX);
        y -= (int) (height / 2.0 * scaleY);
        calcOutsideView();
    }

    public void setScale(double newScale) {
        scaleX = newScale;
        scaleY = newScale;
        calcOutsideView();
    }
    public void setScale(double newScaleX, double newScaleY) {
        scaleX = newScaleX;
        scaleY = newScaleY;
        calcOutsideView();
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
        calcOutsideView();
    }

    public void draw(Graphics2D g2) {

    }

    public void defineCenter(double newX, double newY) {
        centerX = newX;
        centerY = newY;
    }

    public void resetCenter() {
        centerX = x + (width * getScaleX() / 2.0);
        centerY = y + (height * getScaleY() / 2.0);
    }

    @Override
    public void destroy() {
        if (!isParentGroup) {
            super.destroy();

            visible = false;
            if (group != null) {
                group.remove(this);
            }
        }
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    //public boolean collidesWithAnyShape()
    // public void rotate(double angleInDeg, double centerX, double centerY)
    //public Shape[] getCollidingShapes(Group group)
    // public void scale(double factor, double centerX, double centerY)
}
