package UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AboutFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public AboutFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 407, 405);
		contentPane = new JPanel();
		String path = new File(Constants.getData()+File.separator+"splash.jpg").getPath();
        ImageIcon img = new ImageIcon(path);
        contentPane.setLayout(null);
        
        JLabel imglabel = new JLabel(img);
        imglabel.setBounds(0, 0, 404, 310);
        contentPane.add(imglabel);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Made by 3 Sophomor Students at IIT Delhi");
		lblNewLabel.setBounds(20, 322, 378, 29);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Under guidance of Dr. SR Sarangi");
		lblNewLabel_1.setBounds(20, 348, 265, 16);
		contentPane.add(lblNewLabel_1);
	}
}
