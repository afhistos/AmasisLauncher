package be.amasis.launcher;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Copy of fr.theshark34.swinger.util.WindowMover;
public class WindowMover extends MouseAdapter {
    private Point click;
    private JFrame window;

    public WindowMover(JFrame window) {
        this.window = window;
    }
    public void mouseDragged(MouseEvent e){
        if(click != null){
            Point draggedPoint = MouseInfo.getPointerInfo().getLocation();
            window.setLocation(new Point((int)draggedPoint.getX() - (int)this.click.getX(), (int)draggedPoint.getY() - (int)this.click.getY()));
        }
    }

    public void mousePressed(MouseEvent e) {
        click = e.getPoint();
    }
}
