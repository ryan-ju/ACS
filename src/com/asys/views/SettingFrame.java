/**
 * 
 */
package com.asys.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.asys.simulator.Builder;
import com.asys.simulator.SimulatorModel;

/**
 * @author ryan
 *
 */
public class SettingFrame extends JFrame {
	JFrame frame = this;
	
	public SettingFrame(){
		JPanel content = new JPanel();
		
		// Sheet
		JLabel sheet_label = new JLabel("Please enter initialization sheet.\n" +
				"The format is [gate_id]\\s[logic value] on each line, eg, g10 X");
		final JTextArea sheet_area = new JTextArea();
		sheet_area.setPreferredSize(new Dimension(100, 300));
		
		// Is automated
		final JRadioButton automated_radio = new JRadioButton("Automated");
		JRadioButton manual_radio = new JRadioButton("Manual");
		ButtonGroup auto_group = new ButtonGroup();
		auto_group.add(automated_radio);
		auto_group.add(manual_radio);
		automated_radio.setSelected(true);
		
		// Set speed
		JLabel speed_label = new JLabel("Please set speed as the number of circuit time unit per second.  The recommended value is < 10");
		final JTextField speed_field = new JTextField();
		
		//Buttons
		JButton ok_button = new JButton("OK");
		JButton cancel_button = new JButton("Cancel");
		
		// Button panel
		JPanel button_panel = new JPanel();
		button_panel.add(ok_button, BorderLayout.WEST);
		button_panel.add(cancel_button, BorderLayout.EAST);
		
		// Putting components together
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(sheet_label);
		content.add(sheet_area);
//		content.add(automated_radio);
//		content.add(manual_radio);
		content.add(speed_label);
		content.add(speed_field);
		content.add(button_panel);
		
		this.setContentPane(content);
		
		ok_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Builder b = Builder.getInstance();
				if (b.isStable(sheet_area.getText())){
					if (speed_field.getText().isEmpty()){
						JOptionPane.showMessageDialog(frame, "The number of units per second cannot be empty.");
						return;
					}
					int speed = Integer.parseInt(speed_field.getText());
					if (speed>0 && speed<=100){
						SimulatorModel.getInstance().setIsAutomated(automated_radio.isSelected());
						SimulatorModel.getInstance().setUnitPerSecond(speed);
						SimulatorModel.getInstance().fireChanged();
						frame.setVisible(false);
					}else{
						JOptionPane.showMessageDialog(frame, "Invalid value.  The number i of units per seconds should be 0 < i <= 100");
						return;
					}
				}else{
					b.initialize();
					JOptionPane.showMessageDialog(frame, "The initialization sheet contains errors.  Please check.");
				}
			}
		});
		
		cancel_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
	}
}
