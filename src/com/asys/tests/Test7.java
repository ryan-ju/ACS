/**
 * 
 */
package com.asys.tests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author ryan
 *
 */
public class Test7 extends JFrame{
	
	public Test7(){
		setSize(600,400);
		JButton but = new JButton("Throw exception");
		but.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ExceptionThrower.throwException();
				} catch (Exception e1) {
					System.out.println("Exception caught!");
				}
			}
			
		});
		add(but);
	}
	
	static class ExceptionThrower {
		static void throwException() throws Exception{
			throw new Exception();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				(new Test7()).setVisible(true);
			}
			
		});
	}

}
