/**
 * 
 */
package com.asys.tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author ryan
 *
 */
public class GradientTest extends JFrame{
	
	GradientTest(){
		init();
	}
	
	void init(){
		GradientPanel1 gp1 = new GradientPanel1();
		gp1.setSize(400, 600);
		Container content = getContentPane();
		content.add(gp1, BorderLayout.CENTER);
		content.add(new JButton("Test Button"), BorderLayout.NORTH);
		setSize(600, 400);
	}
	
	class GradientPanel1 extends JPanel{
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.black);
			g2d.fillRect(0, 0, 30, 30);
			Paint old_p = g2d.getPaint();
			
			Point2D center = new Point2D.Float(50,50);
			float radius = 10;
			float[] dist = {0f, 1.0f};
			Color c0 = new Color(255, 0, 0, 56);
			Color c1 = new Color(255, 0, 0, 0);
			Color[] colors = {c0, c1};
			RadialGradientPaint p = new RadialGradientPaint(50f, 50f, 5, dist, colors);
			g2d.setPaint(p);
			g2d.fillOval(40, 40, 20, 20);
			
			LinearGradientPaint lp = new LinearGradientPaint(50, 50, 60, 50, dist, colors, MultipleGradientPaint.CycleMethod.REFLECT);
			g2d.setPaint(lp);
			g2d.fillRect(40, 40, 20, 100);
			
			g2d.setPaint(old_p);
			
		}
	}
	
	public static void main(String[] args) {
		new GradientTest().setVisible(true);
	}
}
