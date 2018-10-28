package UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
 
public class SplashScreen extends JFrame {
 
    private JLabel imglabel;
    private ImageIcon img;
    private static JProgressBar pbar;
    Thread t = null;
 
    public SplashScreen() {
        super("Splash");
        setSize(404, 310);
        this.setTitle("NewsAnalytics - IITD");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        String path = new File(Constants.getData()+File.separator+"splash.jpg").getPath();
        img = new ImageIcon(path);
        imglabel = new JLabel(img);
        setLayout(null);
        pbar = new JProgressBar();
        pbar.setMinimum(0);
        pbar.setMaximum(100);
        pbar.setStringPainted(true);
        pbar.setForeground(Color.LIGHT_GRAY);
        imglabel.setBounds(0, 0, 404, 310);
        add(pbar);
         add(imglabel);
        pbar.setPreferredSize(new Dimension(310, 30));
        pbar.setBounds(0, 290, 404, 20);
        pbar.setBackground(Color.RED);
        Thread t = new Thread() {
 
            public void run() {
                int i = 0;
                while (i <= 45) {
                    pbar.setValue((int)Math.ceil(i*100/45));
                    try {
                        sleep(90);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    i++;
                }
            }
        };
        t.start();
    }
}