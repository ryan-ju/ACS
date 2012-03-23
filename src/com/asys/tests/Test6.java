/**
 * 
 */
package com.asys.tests;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * @author ryan
 *
 */
public class Test6 extends JFrame{
	
	JPopupMenu pop;
	
	public Test6(){
		setSize(600, 400);
		pop = new JPopupMenu();
		pop.add(new JMenuItem("Item 1"));
		pop.add(new JMenuItem("Item 2"));
		pop.setPopupSize(50, 100);
		getContentPane().addMouseListener(new MyMouseAdapter());
		this.setLocationByPlatform(true);
	}
	
	class MyMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e){
			if (e.isPopupTrigger()){
				pop.show(Test6.this.getContentPane(), e.getX(), e.getY());
			}
//			if (e.getButton() == MouseEvent.BUTTON3){
//				pop.show(Test6.this.getContentPane(), e.getX(), e.getY());
//			}
		}
		
		@Override
		public void mousePressed(MouseEvent e){
			if (e.isPopupTrigger()){
				pop.show(Test6.this.getContentPane(), e.getX(), e.getY());
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				(new Test6()).setVisible(true);
			}
			
		});
	}
}
