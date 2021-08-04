package be.amasis.launcher;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class MainPanel extends JPanel implements MouseListener {

    private JPanel titlePanel;
    private Saver saver = new Saver(StartHandler.PROPS_FILE);
    private JTextField username = new JTextField(saver.get("username"));
    private JPasswordField password;
    private JButton playButton;
    private JCheckBox savePwd;
    private JProgressBar progressBar;
    private JLabel info;

    public MainPanel() {
        setLayout(null);
        if(StartHandler.PROPS_FILE.exists()){
            try {
                if (saver.get("savePwd?").equals("true")) {
                    password = new JPasswordField(decrypt(saver.get("password")));
                } else {
                    password = new JPasswordField();
                    saver.set("password", "");
                }
            }catch (NullPointerException ignored){
                password = new JPasswordField();
            }
        }else{
            try{
                StartHandler.PROPS_FILE.getParentFile().mkdir();
                StartHandler.PROPS_FILE.createNewFile();
            } catch (IOException e) {
                if(StartHandler.PRINT_LOGS)e.printStackTrace();
            }
            password = new JPasswordField();
        }
        playButton = new JButton("Jouer");
        progressBar = new JProgressBar();
        info = new JLabel("Clique sur jouer!", 0);
        savePwd = new JCheckBox("Sauvegarder le mot de passe");

        drawElements();
        add(titlePanel);
    }

    private void drawElements() {
        AppHeader appHeader = new AppHeader(this,"Bienvenue à l'équipe de trisos", StartHandler.WIDTH);
        appHeader.getMover().addMouseListener(MainFrame.getMover());
        appHeader.getMover().addMouseMotionListener(MainFrame.getMover());
        titlePanel = new ImagePanel("title.png");//270x88
        titlePanel.setLayout(new FlowLayout());
        titlePanel.setBounds(StartHandler.getCenteredXPos(270), 30, 270,88);
        savePwd.setToolTipText("Autorise le launcher à sauvegarder ton mot de passe");
        savePwd.setBounds(StartHandler.getCenteredXPos(340), 340, 340,30);
        savePwd.setHorizontalAlignment(SwingConstants.CENTER);
        savePwd.setBackground(StartHandler.TRANSPARENT);
        savePwd.setOpaque(false);
        savePwd.setForeground(StartHandler.COLOR);
        savePwd.setFont(StartHandler.CUSTOM_FONT);
        try{
            if(saver.get("savePwd?").equals("true")){
                savePwd.setSelected(true);
            }else{
                savePwd.setSelected(false);
            }
        }catch (NullPointerException ignored){
            savePwd.setSelected(false);
        }
        add(savePwd);
        JLabel userLabel = new JLabel("Adresse mail:", SwingConstants.CENTER);
        userLabel.setForeground(StartHandler.COLOR);
        userLabel.setFont(StartHandler.CUSTOM_FONT.deriveFont(25f));
        userLabel.setBounds(StartHandler.getCenteredXPos(350), 120,325,30);
        add(userLabel);
        username.setCaretColor(StartHandler.COLOR);
        username.setForeground(StartHandler.COLOR);
        username.setFont(userLabel.getFont().deriveFont(35f));
        username.setOpaque(false);
        username.setBorder(BorderFactory.createLineBorder(StartHandler.COLOR, 2));
        username.setBounds(StartHandler.getCenteredXPos(350), 157,350,60);
        username.setText(saver.get("username", ""));
        username.setHorizontalAlignment(SwingConstants.CENTER);
        username.setDisabledTextColor(StartHandler.COLOR.brighter());
        add(username);
        JLabel pwLabel = new JLabel("Mot de passe:", SwingConstants.CENTER);
        pwLabel.setForeground(StartHandler.COLOR);
        pwLabel.setFont(userLabel.getFont());
        pwLabel.setBounds(StartHandler.getCenteredXPos(350),240,350,30);
        add(pwLabel);
        password.setCaretColor(StartHandler.COLOR);
        password.setForeground(StartHandler.COLOR);
        password.setFont(username.getFont());
        password.setOpaque(false);
        password.setBorder(BorderFactory.createLineBorder(StartHandler.COLOR,2));
        password.setBounds(StartHandler.getCenteredXPos(350), 277,350,60);
        password.setHorizontalAlignment(SwingConstants.CENTER);
        password.setDisabledTextColor(StartHandler.COLOR.brighter());
        add(password);
        playButton.setBackground(StartHandler.COLOR);
        playButton.addMouseListener(this);
        playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playButton.setFont(StartHandler.CUSTOM_FONT.deriveFont(26f));
        playButton.setBounds(StartHandler.getCenteredXPos(200), 420,200,30);
        add(playButton);
        progressBar.setStringPainted(true);
        progressBar.setBackground(new Color(StartHandler.COLOR.getRed(),StartHandler.COLOR.getGreen(),
                StartHandler.COLOR.getBlue(),23));
        progressBar.setForeground(StartHandler.COLOR);
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);

        progressBar.setBounds(10, StartHandler.HEIGHT-60,StartHandler.WIDTH-20,40);
        add(progressBar);
        info.setFont(username.getFont());
        info.setForeground(StartHandler.COLOR);
        info.setBounds(10, StartHandler.HEIGHT-125,StartHandler.WIDTH-20,55);
        add(info);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(StartHandler.getImage("bgd.png"),0,0,null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == playButton){
            setFieldsEnabled(false);
            if((username.getText().replaceAll(" ","").length() == 0) || (password.getPassword().length == 0)){
                JOptionPane.showMessageDialog(this, "ERREUR: Adresse mail |Mot de passe invalide", "Erreur de connexion", 2);
                setFieldsEnabled(true);
            }
            Thread t = new Thread(() -> {
                try{
                    LoginHandler.auth(username.getText(), String.valueOf(password.getPassword()));
                } catch (AuthenticationException e1) {
                    JOptionPane.showMessageDialog(this,
                            "ERREUR: Impossible de se connecter: \n" +
                                e1.getErrorModel().getErrorMessage(), "Erreur de connexion", 2);
                    setFieldsEnabled(true);
                    return;
                }
                saver.set("username", username.getText());
                if(savePwd.isSelected()){
                    String pwd = encrypt(String.valueOf(password.getPassword()));
                    saver.set("password", pwd);
                    saver.set("savePwd?", "true");
                }else{
                    saver.set("password", "null");
                    saver.set("savePwd?", "true");
                }
                try{
                    LoginHandler.update();
                }catch (Exception e2){
                    LoginHandler.interruptUpdate();
                    LoginHandler.getReporter().catchError(e2, "Impossible de mettre le launcher à jour.");
                    setFieldsEnabled(true);
                    return;
                }
                try{
                    LoginHandler.launch();
                } catch (LaunchException e3) {
                    LoginHandler.interruptUpdate();
                    LoginHandler.getReporter().catchError(e3,"Impossible de lancer le jeu.");
                    setFieldsEnabled(true);
                }
            });
            t.start();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    private void setFieldsEnabled(Boolean enabled) {
        username.setEnabled(enabled);
        password.setEnabled(enabled);
        playButton.setEnabled(enabled);
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setInfoText(String info) {
        this.info.setText(info);
    }

    private String encrypt(String p){
        String crypt = "";
        int i = 0;
        while (i < p.length()){
            int c = p.charAt(i) ^ 0x30;
            crypt = String.valueOf(crypt) + (char)c;
            i++;
        }
        return crypt;
    }

    private String decrypt(String p){
        String crypt = "";
        int i = 0;
        while (i < p.length()){
            int c = p.charAt(i) ^ 0x30;
            crypt = String.valueOf(crypt) + (char)c;
            i++;
        }
        return crypt;
    }
}
