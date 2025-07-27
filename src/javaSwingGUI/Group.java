package javaSwingGUI;

import java.awt.Shape;
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
    public Group(World w, boolean isParentGroup){
        super(w,isParentGroup);
        shapes = Collections.synchronizedList(new ArrayList<Actor>());
    }

    public Group(World w, Actor...shapes){
    super(w);
        this.shapes = Collections.synchronizedList(new ArrayList<Actor>());
        Collections.addAll(this.shapes, shapes);
    }
    /*
    New objects are registered here
     */
    public synchronized void add(Actor...shapes){
        for(Actor e:shapes){
            if (e==null){
                System.out.println("Added a null Object to a Group!");
            }
        }
        Collections.addAll(this.shapes, shapes);
    }

    public synchronized void remove(Actor shape){
        shapes.remove(shape);
    }

    public synchronized ArrayList<Actor> getShapes(){
        return new ArrayList<>(shapes);
    }
    @Override
    public void draw(Graphics2D g2) {
        //Absicht hier kein enhanced for loop!
        for (int i = 0; i < shapes.size(); i++){
            shapes.get(i).draw(g2);
        }
    }

    @Override
    public void act() {
        //Absicht hier kein enhanced for loop!
        for(int i = 0; i < shapes.size(); i++){
            if (shapes.get(i) != null && shapes.get(i).isActing()){
                shapes.get(i).act();
            }
        }
    }

    @Override
    public Group copy(){
        return this;
    }

    @Override
    public void move(double dx, double dy) {
        for(Actor e: shapes){
            e.move(dx,dy);
        }
    }

    public void bringToFront(Actor actor) {
        remove(actor);
        add(actor);
    }
}
