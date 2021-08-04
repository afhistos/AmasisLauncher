package be.amasis.launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    BufferedImage image;
    public ImagePanel(String fileName){
        image = StartHandler.getImage(fileName);
        setBackground(StartHandler.TRANSPARENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0,0, null);
    }
}
