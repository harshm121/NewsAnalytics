package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Backend.GoogleConfig;
import Backend.main;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.JButton;

public class GoogleConfigurationFrame extends JFrame {

	private JPanel contentPane;
	private JTextField tfPub1;
	private JTextField tfUrl2;
	private JTextField tfUrl1;
	private JTextField tfDate1;
	private JTextField tfDateFormat;
	private JTextField tfPub2;
	private JTextField tfDate2;


	/**
	 * Create the frame.
	 */
	public GoogleConfigurationFrame() {
		File googleConfigs = new File(Constants.getData()+File.separator+"googleConfig.ser");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 508, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUrl = new JLabel("URL");
		lblUrl.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblUrl.setBounds(228, 32, 70, 28);
		contentPane.add(lblUrl);
		
		JLabel lblHtmlTagClass = new JLabel("(html) URL Class 1:");
		lblHtmlTagClass.setBounds(53, 83, 137, 16);
		contentPane.add(lblHtmlTagClass);
		
		tfPub1 = new JTextField();
		tfPub1.setBounds(219, 127, 97, 26);
		contentPane.add(tfPub1);
		tfPub1.setColumns(10);
		
		tfUrl2 = new JTextField();
		tfUrl2.setBounds(328, 78, 97, 26);
		contentPane.add(tfUrl2);
		tfUrl2.setColumns(10);
		
		JLabel lblHtmlPublisherTag = new JLabel("(html) Publisher Class:");
		lblHtmlPublisherTag.setBounds(33, 132, 157, 16);
		contentPane.add(lblHtmlPublisherTag);
		
		tfUrl1 = new JTextField();
		tfUrl1.setBounds(219, 78, 97, 26);
		contentPane.add(tfUrl1);
		tfUrl1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("(html) Date Class:");
		lblNewLabel_1.setBounds(64, 178, 137, 16);
		contentPane.add(lblNewLabel_1);
		
		tfDate1 = new JTextField();
		tfDate1.setBounds(219, 173, 97, 26);
		contentPane.add(tfDate1);
		tfDate1.setColumns(10);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblDate.setBounds(228, 246, 61, 16);
		contentPane.add(lblDate);
		
		JLabel lblDateFormat = new JLabel("Date Format:");
		lblDateFormat.setBounds(103, 285, 114, 16);
		contentPane.add(lblDateFormat);
		
		tfDateFormat = new JTextField();
		tfDateFormat.setBounds(219, 280, 197, 26);
		contentPane.add(tfDateFormat);
		tfDateFormat.setColumns(10);
		
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(232, 369, 117, 29);
		btnSave.setToolTipText("Saves the configuration for this PC");
		btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				tfPub1.setEditable(false);
				tfPub2.setEditable(false);
				tfUrl2.setEditable(false);
				tfUrl1.setEditable(false);
				tfDate1.setEditable(false);
				tfDate2.setEditable(false);
				tfDateFormat.setEditable(false);
				tfPub1.setBackground(Color.LIGHT_GRAY);
				tfPub2.setBackground(Color.LIGHT_GRAY);
				tfUrl2.setBackground(Color.LIGHT_GRAY);
				tfUrl1.setBackground(Color.LIGHT_GRAY);
				tfDate1.setBackground(Color.LIGHT_GRAY);
				tfDate2.setBackground(Color.LIGHT_GRAY);
				tfDateFormat.setBackground(Color.LIGHT_GRAY);
				btnSave.setEnabled(false);
				GoogleConfig newConfig = getConfigurations();
				main.serialize(newConfig, googleConfigs.getAbsolutePath());
			}
		});
		contentPane.add(btnSave);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(361, 369, 117, 29);
		btnEdit.setToolTipText("Do not change untill you know what you are doing");
		btnEdit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int x= JOptionPane.showConfirmDialog(null, "Are you sure you want to edit?");
				if(x==JOptionPane.YES_OPTION){
					btnSave.setEnabled(true);
					tfPub1.setEditable(true);
					tfUrl2.setEditable(true);
					tfUrl1.setEditable(true);
					tfDate1.setEditable(true);
					tfDateFormat.setEditable(true);
					tfPub1.setBackground(Color.WHITE);
					tfPub2.setBackground(Color.WHITE);
					tfUrl2.setBackground(Color.WHITE);
					tfUrl1.setBackground(Color.WHITE);
					tfDate1.setBackground(Color.WHITE);
					tfDate2.setBackground(Color.WHITE);
					tfDateFormat.setBackground(Color.WHITE);
				}
			}
		});
		contentPane.add(btnEdit);
		
		tfPub2 = new JTextField();
		tfPub2.setBounds(328, 127, 97, 26);
		contentPane.add(tfPub2);
		tfPub2.setColumns(10);
		
		tfDate2 = new JTextField();
		tfDate2.setBounds(328, 173, 97, 26);
		contentPane.add(tfDate2);
		tfDate2.setColumns(10);
		
		if(googleConfigs.exists()){
			GoogleConfig exists = (GoogleConfig) main.deserialize(googleConfigs.getAbsolutePath());
			tfPub1.setText(exists.getPublisherTag1());
			tfPub2.setText(exists.getPublisherTag2());
			tfUrl2.setText(exists.getUrlTag2());
			tfUrl1.setText(exists.getUrlTag1());
			tfDate1.setText(exists.getDateTag1());
			tfDate2.setText(exists.getDateTag2());
			tfDateFormat.setText(exists.getDateFormat());
			tfPub1.setEditable(false);
			tfUrl2.setEditable(false);
			tfUrl1.setEditable(false);
			tfDate1.setEditable(false);
			tfDateFormat.setEditable(false);
			tfPub1.setBackground(Color.LIGHT_GRAY);
			tfPub2.setBackground(Color.LIGHT_GRAY);
			tfUrl2.setBackground(Color.LIGHT_GRAY);
			tfUrl1.setBackground(Color.LIGHT_GRAY);
			tfDate1.setBackground(Color.LIGHT_GRAY);
			tfDate2.setBackground(Color.LIGHT_GRAY);
			tfDateFormat.setBackground(Color.LIGHT_GRAY);
			btnSave.setEnabled(false);
			btnEdit.setEnabled(true);
		}else{
			tfPub1.setEditable(true);
			tfPub2.setEditable(true);
			tfUrl2.setEditable(true);
			tfUrl1.setEditable(true);
			tfDate1.setEditable(true);
			tfDate2.setEditable(true);
			tfDateFormat.setEditable(true);
			tfPub1.setBackground(Color.WHITE);
			tfPub2.setBackground(Color.WHITE);
			tfUrl2.setBackground(Color.WHITE);
			tfUrl1.setBackground(Color.WHITE);
			tfDate1.setBackground(Color.WHITE);
			tfDate2.setBackground(Color.WHITE);
			tfDateFormat.setBackground(Color.WHITE);
			btnSave.setEnabled(true);
			btnEdit.setEnabled(true);
		}
	}
	
	public GoogleConfig getConfigurations(){
		GoogleConfig gc = new GoogleConfig();
		gc.setUrlTag1(tfUrl1.getText());
		gc.setUrlTag2(tfUrl2.getText());
		gc.setPublisherTag1(tfPub1.getText());
		gc.setPublisherTag2(tfPub2.getText());
		gc.setDateTag1(tfDate1.getText());
		gc.setDateTag2(tfDate2.getText());
		gc.setDateFormat(tfDateFormat.getText());
		return gc;
	}
}
