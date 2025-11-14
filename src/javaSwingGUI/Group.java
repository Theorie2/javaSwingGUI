package javaSwingGUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group extends Shape {
    private List<Actor> shapes;

    public Group(World w) {
        super(w);
        shapes = Collections.synchronizedList(new ArrayList<Actor>());
    }

    /*
    This constructor is only necessary for the World class
     */
    public Group(World w, boolean isParentGroup) {
        super(w, isParentGroup);
        shapes = Collections.synchronizedList(new ArrayList<Actor>());
    }

    public Group(World w, Actor... shapes) {
        super(w);
        this.shapes = Collections.synchronizedList(new ArrayList<Actor>());
        Collections.addAll(this.shapes, shapes);
    }

    /*
    New objects are registered here
     */
    public synchronized void add(Actor... shapes) {
        for (Actor e : shapes) {
            if (e == null) {
                System.out.println("Added a null Object to a Group!");
            }
        }
        Collections.addAll(this.shapes, shapes);
    }

    public synchronized void remove(Actor shape) {
        shapes.remove(shape);
    }

    public synchronized ArrayList<Actor> getShapes() {
        return new ArrayList<>(shapes);
    }

    @Override
    public void draw(Graphics2D g2) {
        //Absicht hier kein enhanced for loop!
        for (int i = 0; i < shapes.size(); i++) {
            shapes.get(i).draw(g2);
        }
    }

    @Override
    public void act() {
        //Absicht hier kein enhanced for loop!
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i) != null && shapes.get(i).isActing()) {
                shapes.get(i).act();
            }
        }
    }

    @Override
    public void rotate(double angleInDeg, double centerX, double centerY) {
        synchronized(shapes) {
            for(int i = 0; i < shapes.size(); i++){
                Actor shape = shapes.get(i);
                if(shape instanceof Shape){
                    ((Shape)shape).rotate(angleInDeg, centerX, centerY);
                }
            }
        }
    }
    /**
     * Rotates all shapes around the calculated center of the group
     * @param angleInDeg angle in degrees
     */
    public void rotate(double angleInDeg) {
        if(shapes.isEmpty()){
            return;
        }

        // Finde die Bounding Box aller Shapes
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        synchronized(shapes) {
            for(int i = 0; i < shapes.size(); i++){
                Actor shape = shapes.get(i);
                if(shape instanceof Shape){

                    double x = ((Shape)shape).getX();
                    double y = ((Shape)shape).getY();
                    double width = ((Shape)shape).getWidth() * ((Shape)shape).getScaleX();
                    double height = ((Shape)shape).getHeight() * ((Shape)shape).getScaleY();

                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x + width);
                    maxY = Math.max(maxY, y + height);
                }
            }
        }

        // Zentrum der Bounding Box
        double centerX = (minX + maxX) / 2.0;
        double centerY = (minY + maxY) / 2.0;

        // Rotiere um das berechnete Zentrum
        rotate(angleInDeg, centerX, centerY);
    }
    @Override
    public Group copy() {
        return this;
    }

    @Override
    public void move(double dx, double dy) {
        for (Actor e : shapes) {
            e.move(dx, dy);
        }
    }

    public void bringToFront(Actor actor) {
        remove(actor);
        add(actor);
    }
}
