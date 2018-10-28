package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Backend.main;
import Backend.proxy;

import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class ProxyDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	ProxyConfig proxyc;
	File proxyConfigs = new File(Constants.getData()+File.separator+"proxyConfig.ser");
	JButton editBtn;
	JButton saveButton;
	/**
	 * Create the dialog.
	 */
	public ProxyDialog() {
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        textField.setText("");
		        textField_1.setText("");
		        closeDialog();
		    }
		});
		setBounds(100, 100, 467, 314);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 467, 236);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JRadioButton rdbtnEnableProxy = new JRadioButton("Enable Proxy", null, true);
		rdbtnEnableProxy.setBounds(75, 18, 141, 23);
		contentPanel.add(rdbtnEnableProxy);
		
		JRadioButton rdbtnDisableProxy = new JRadioButton("Disable Proxy");
		rdbtnDisableProxy.setBounds(251, 18, 141, 23);
		contentPanel.add(rdbtnDisableProxy);
		
		JLabel lblProxyHost = new JLabel("Proxy Host :");
		lblProxyHost.setBounds(75, 77, 87, 23);
		contentPanel.add(lblProxyHost);
		
		textField = new JTextField();
		textField.setBounds(174, 75, 237, 26);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblProxyPort = new JLabel("Proxy Port : ");
		lblProxyPort.setBounds(75, 133, 87, 23);
		contentPanel.add(lblProxyPort);
		
		textField_1 = new JTextField();
		textField_1.setBounds(174, 131, 141, 26);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		Color defaultColor=textField.getBackground();
		
		rdbtnEnableProxy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnEnableProxy.isSelected()){
					rdbtnDisableProxy.setSelected(false);
					textField.setEditable(true);
					textField.setBackground(defaultColor);
					textField_1.setEditable(true);
					textField_1.setBackground(defaultColor);
					editBtn.setEnabled(true);
					saveButton.setEnabled(true);
				}
				else{
					rdbtnDisableProxy.setSelected(true);
					rdbtnEnableProxy.setSelected(false);
					textField.setEditable(false);
					textField.setBackground(contentPanel.getBackground());
					textField_1.setEditable(false);
					textField_1.setBackground(contentPanel.getBackground());
					textField.setText("");
					textField_1.setText("");
					proxy.disableProxy();
					proxyConfigs.delete();
					saveButton.setEnabled(false);
					editBtn.setEnabled(false);
					closeDialog();
				}
			}
		});
		rdbtnDisableProxy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnDisableProxy.isSelected()){
					rdbtnEnableProxy.setSelected(false);
					textField.setEditable(false);
					textField.setBackground(contentPanel.getBackground());
					textField_1.setEditable(false);
					textField_1.setBackground(contentPanel.getBackground());
					textField.setText("");
					textField_1.setText("");
					proxy.disableProxy();
					proxyConfigs.delete();
					saveButton.setEnabled(false);
					editBtn.setEnabled(false);
					closeDialog();
					
				}
				else{
					rdbtnEnableProxy.setSelected(true);
					textField.setEditable(true);
					textField.setBackground(defaultColor);
					textField_1.setEditable(true);
					textField_1.setBackground(defaultColor);
					editBtn.setEnabled(true);
					saveButton.setEnabled(true);
				}
			}
		});
		{	
			if(proxyConfigs.exists()){
				ProxyConfig p = (ProxyConfig) main.deserialize(proxyConfigs.getAbsolutePath());
				textField.setText(p.getProxyNameHTTP());
				textField.setEditable(false);
				textField.setBackground(contentPanel.getBackground());
				textField_1.setText(p.getProxyPortHTTP());
				textField_1.setEditable(false);
				textField_1.setBackground(contentPanel.getBackground());
			}
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 237, 467, 55);
			getContentPane().add(buttonPane);
				buttonPane.setLayout(null);
			
				saveButton = new JButton("Save");
				saveButton.setBounds(195, 12, 75, 29);
				saveButton.setEnabled(true);
				saveButton.setToolTipText("Save for this PC");
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
				saveButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						textField.setEditable(false);
						textField.setBackground(contentPanel.getBackground());
						textField_1.setEditable(false);
						textField_1.setBackground(contentPanel.getBackground());
						closeDialog();
				        proxy.enableProxy(textField.getText(),textField_1.getText(),textField.getText(),textField_1.getText());
				        proxyc = new ProxyConfig(textField.getText(),textField_1.getText(),textField.getText(),textField_1.getText());
				        main.serialize(proxyc, proxyConfigs.getAbsolutePath());
				        QueryInputPanel.updateInternetStatus(true);
				        saveButton.setEnabled(false);

					}
				});
			
			
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setBounds(364, 12, 86, 29);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						//textField.setText("");
						//textField_1.setText("");
						textField.setEditable(true);
						textField_1.setEditable(true);
						rdbtnEnableProxy.setSelected(true);
						rdbtnDisableProxy.setSelected(false);
						closeDialog();
					}
				});
				
				editBtn = new JButton("Edit");
				editBtn.setBounds(276, 12, 86, 29);
				buttonPane.add(editBtn);
				editBtn.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						textField.setEditable(true);
						textField_1.setEditable(true);
						saveButton.setEnabled(true);
					}
				});
		}
	}
	public void closeDialog(){
		this.setVisible(false);
		this.dispose();
	}
}
