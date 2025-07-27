package javaSwingGUI;

import java.awt.*;

public class Group extends Shape {
    private Actor[] shapes;

    public Group(World w){
    super(w);
    shapes = new Actor[0];
    }

    /*
    This constructor is only necessary for the World class
     */
    public Group(World w, boolean isParentGroup){
        super(w,isParentGroup);
        shapes = new Actor[0];
    }

    public Group(World w, Actor...shapes){
    super(w);
        this.shapes = shapes;
    }
    /*
    New objects are registered here
     */
    public void add(Actor...shapes){
        for(Actor e:shapes){
            if (e==null){
                System.out.println("Added a null Object to a Group!");
            }
        }
        Actor[] shapeSave = this.shapes;
        this.shapes = new Actor[shapes.length + shapeSave.length];
        for(int i = 0; i < this.shapes.length;i++){
            if (i < shapeSave.length){
                this.shapes[i] = shapeSave[i];
            } else{
                this.shapes[i] = shapes[i-shapeSave.length];
            }
        }

    }

    public void remove(Actor shape){
        //Durchlaufen des Arrays und überprüfen der Ob es das objekt überhaupt gibt
        for (int i = 0; i < shapes.length;i++){
            if (shapes[i] == shape){
                //Abspeichern der Alten Liste
                Actor[] shapeSave = this.shapes;
                //Richtige Länge der Liste festsetzten
                shapes = new Actor[shapeSave.length-1];
                shape.group = null;
                shapes[i] = null;
                //Variable n wird benutzt um den wert null der Liste shapeSave zu überspringen
                int n = shapeSave.length-2;
                //Setzten der neuen Werte ohne das gelöschte Objekt
                for (int l = shapeSave.length-1; l >= 0 ; l--){
                    if (shapeSave[l]!=null){
                        this.shapes[n] = shapeSave[l];
                        n--;
                    }
                }
                //Falls ein Objekt zweimal in einer Gruppe Existiert werden nicht beide gelöscht
                break;
            }
        }

    }

    public Actor[] getShapes(){
        return shapes;
    }
    @Override
    public void draw(Graphics2D g2) {
        for (Actor e :shapes) {
            e.draw(g2);
        }
    }

    @Override
    public Group copy(){
        return this;
    }

    @Override
    public void move(int dx, int dy) {
        for(Actor e: shapes){
            e.move(dx,dy);
        }
    }
}
