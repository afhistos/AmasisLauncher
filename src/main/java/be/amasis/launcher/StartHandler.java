package be.amasis.launcher;

import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartHandler {
    //GLOBAL VARS
    public static Color COLOR = new Color(81,130,252);
    public static Color TRANSPARENT = new Color(0,0,0,0);
    public static String NAME = "Amasis";
    public static String TITLE = NAME + " launcher";
    public static int HEIGHT = 600, WIDTH = (int) (HEIGHT * 1.55);//Format 14:9 600 » 930
    public static boolean PRINT_LOGS;
    public static File DIR = GameDirGenerator.createGameDir(NAME);
    public static File PROPS_FILE = new File(StartHandler.DIR, "launcher.properties");
    public static Font CUSTOM_FONT;
    private static MainFrame frame;


    public static void main(String[] args){
        System.out.println("Dim: "+WIDTH+"x"+HEIGHT);
        PRINT_LOGS = isArgUsed("printlogs", args);
        if(PRINT_LOGS)System.out.println("Chargement de la police...");
        CUSTOM_FONT = loadFont("Ubuntu.ttf");

        frame = new MainFrame();

    }

    private static Font loadFont(String s) {
        Font f = null;
        try{
            f = Font.createFont(Font.TRUETYPE_FONT, StartHandler.class.getResourceAsStream("/"+s));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(f);
            if(PRINT_LOGS)System.out.println("Police chargée!");
        } catch (FontFormatException e) {
            if(PRINT_LOGS)e.printStackTrace();
        } catch (IOException e) {
            if(PRINT_LOGS)e.printStackTrace();
        }
        return f.deriveFont(16f);
    }


    //GLOBALS FUNCTIONS & METHODS
    public static boolean isArgUsed(String arg, String[] args){
        if(args.length == 0){
            return false;
        }
        for(String s : args){
            if(arg.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }

    public static BufferedImage getImage(String path){
        try{
            return ImageIO.read(StartHandler.class.getResourceAsStream("/"+path));
        } catch (IOException e) {
            if(PRINT_LOGS)e.printStackTrace();
        }
        return null;
    }

    public static MainFrame getFrame() {
        return frame;
    }

    public static int getCenteredXPos(int width){
        return (WIDTH/2)-(width/2);
    }

    public static int getCenteredYPos(int height){
        return (HEIGHT/2)-(height/2);
    }

    public static Rectangle getCenteredPos(int width, int height){
        return new Rectangle(getCenteredXPos(width), getCenteredYPos(height));
    }
}
