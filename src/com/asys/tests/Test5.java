/**
 * 
 */
package com.asys.tests;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author ryan
 *
 */
public class Test5 extends JFrame {
	
	public Test5(){
		add(new InnerPanel(), BorderLayout.CENTER);
		setSize(600, 400);
	}
	
	class InnerPanel extends JPanel{
		
		@Override
		public void paint(Graphics g){
			subPaint((Graphics2D) g);
		}
		
		private void subPaint(Graphics2D g2d){
			g2d.setColor(Color.BLUE);
			Path2D.Float path = new Path2D.Float(Path2D.WIND_NON_ZERO);
			path.moveTo(100, 100);
			path.curveTo(200, 100, 100, 200, 200, 200);
			path.clone();
			g2d.fill(path);
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(2));
			g2d.draw(path);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				(new Test5()).setVisible(true);
			}
			
		});
	}
}
