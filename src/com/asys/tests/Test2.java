/**
 * 
 */
package com.asys.tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author ryan
 *
 */
public class Test2 extends JFrame{

	public Test2(){
		JLayeredPane layers = new JLayeredPane();
		layers.setPreferredSize(new Dimension(600, 400));
		
		MyPanel1 myPanel1 = new MyPanel1();
		MyPanel2 myPanel2 = new MyPanel2();
		myPanel1.setSize(600, 400);
		myPanel2.setSize(600, 400);
		myPanel1.setOpaque(false);
		myPanel2.setOpaque(false);
		myPanel2.addMouseListener(new MyMouseListener(myPanel2));
		
		layers.add(myPanel1, new Integer(100)); // At bottom
		layers.add(myPanel2, new Integer(101)); // On top
		
		this.getContentPane().add(layers, BorderLayout.CENTER);
		this.setSize(600, 400);
	}
	
	class MyPanel1 extends JPanel{
		
		Color getRandomColor(){
			int r = (int)(256*Math.random());
			int g = (int)(256*Math.random());
			int b = (int)(256*Math.random());
			return new Color(r,g,b);
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(getRandomColor());
			g2d.fillRoundRect(30, 30, 60, 60, 5, 5);
		}
	}
	class MyPanel2 extends JPanel{
		
		Color getRandomColor(){
			int r = (int)(256*Math.random());
			int g = (int)(256*Math.random());
			int b = (int)(256*Math.random());
			return new Color(r,g,b);
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(getRandomColor());
			g2d.fillRoundRect(45, 45, 75, 75, 5, 5);
		}
	}
	
	class MyMouseListener extends MouseAdapter{
		JPanel panel;
		
		MyMouseListener(JPanel panel){
			this.panel = panel;
		}
		@Override
		public void mouseClicked(MouseEvent e){
			panel.repaint();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				(new Test2()).setVisible(true);
			}
			
		});
	}

}
