import javaSwingGUI.LOGGER;
import javaSwingGUI.Sprite;
import javaSwingGUI.World;


public class Main {
   public static void main(String ...args){
       LOGGER.load("C:\\Users\\Toran\\IdeaProjects\\systemLib\\res\\logs\\log.txt");
       World w = new World();
       Sprite s = new Sprite("/test.png",w.getWidth()/2,w.getHeight()/2,w);
       w.startDrawThread();
       w.startTicking();
       s.scale(1);
       System.out.println(s.getCenterX()+"   "+s.getCenterY());

   }
}