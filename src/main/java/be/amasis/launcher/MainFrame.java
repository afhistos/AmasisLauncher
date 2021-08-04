package be.amasis.launcher;

import sun.applet.Main;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final MainPanel panel;
    private static WindowMover mover;

    public MainFrame(){
        setTitle(StartHandler.TITLE);
        setSize(StartHandler.WIDTH, StartHandler.HEIGHT);
        setBounds(0,0,getWidth(), getHeight());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(StartHandler.getImage("icon.png"));
        setLocationRelativeTo(null);
        setUndecorated(true);
        mover = new WindowMover(this);
        panel = new MainPanel();
        setContentPane(panel);
        setVisible(true);

    }

    public MainPanel getPanel() {
        return panel;
    }

    public static WindowMover getMover() {
        return mover;
    }
}
