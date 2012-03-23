/**
 * 
 */
package com.asys.tests;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.asys.constants.Constant;

/**
 * @author ryan
 *
 */
public class Test3 extends JFrame{
	
	public Test3(){
		JPanel pl = new MyPanel();
		this.add(pl, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(600,400);
	}
	
	class MyPanel extends JPanel{
		MyPanel(){
			this.setOpaque(false);
		}
		
		@Override
		public void paintComponent(Graphics g){
			BufferedImage buf = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
			Graphics2D gc = buf.createGraphics();
			gc.setColor(Color.BLACK);
			gc.setStroke(new BasicStroke(2));
			gc.drawOval(3, 3, 10, 10);
			Graphics2D g2d = (Graphics2D) g;
			
			
			AffineTransform newAT = new AffineTransform();
//			newAT.translate(0, 0);
			newAT.translate(200, 0);
			newAT.quadrantRotate(1, 0, 0);
			g2d.setTransform(newAT);
			g2d.drawImage(buf, 90, 90, null);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		(new Test3()).setVisible(true);
	}

}
