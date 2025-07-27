import javaSwingGUI.LOGGER;
import javaSwingGUI.Sprite;
import javaSwingGUI.World;

public class Main {
   public static void main(String ...args){
       LOGGER.load("/res/logs/log.txt");
       World w = new World(1920,1080,"test");
       w.startDrawThread();
       w.startTicking();
       Sprite s = new Sprite("/test.png",w.getWidth()/2,w.getHeight()/2,w);
       s.scale(1);

   }
}