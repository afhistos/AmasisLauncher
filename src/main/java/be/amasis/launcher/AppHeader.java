package be.amasis.launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AppHeader implements MouseListener {
    private JButton close, minimize;//42x26
    private JLabel mover;
    private JPanel panel;
    public static Color customColor = StartHandler.COLOR;

    public AppHeader(JPanel panel, String title, int width){
        this.panel = panel;
        close = new JButton("x");
        close.setBounds(width-42,0,42,26);
        close.addMouseListener(this);
        close.setBackground(customColor);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        minimize = new JButton("-");
        minimize.addMouseListener(this);
        minimize.setBounds(width-84,0,42,26);
        minimize.setBackground(customColor);
        minimize.setFocusPainted(false);
        minimize.setBorderPainted(false);
        mover = new JLabel(title, SwingConstants.CENTER);
        mover.setBackground(customColor);
        mover.setOpaque(true);
        mover.setBounds(0,0,width-84,26);
        panel.add(mover);
        panel.add(minimize);
        panel.add(close);

    }


    @Override
    public void mouseClicked(MouseEvent e) {
        JComponent component = (JComponent) e.getSource();
        if(component.equals(close)){
            System.exit(0);
        }else if(component.equals(minimize)){
            StartHandler.getFrame().setState(Frame.ICONIFIED);
            minimize.setBackground(customColor);
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        mouseEvent.getComponent().setBackground(customColor.darker());
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        mouseEvent.getComponent().setBackground(customColor);
    }

    public JLabel getMover() {
        return mover;
    }

    public JButton getClose() {
        return close;
    }
}
