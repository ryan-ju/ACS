package com.asys.tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.asys.constants.Constant;

public class Test4 extends JFrame {

	public Test4() {
		BufferedImage buf = null, buf2;
		try {
			buf = ImageIO.read(new File(Constant.IMAGE_PATH
							+ "and.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageIcon icon = new ImageIcon(buf);
		JButton but1 = new JButton(new ImageIcon(buf));
		but1.setContentAreaFilled(false);
		this.add(new MyPanel());
		this.add(but1, BorderLayout.NORTH);
		buf2 = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gc = buf2.createGraphics();
		gc.drawImage(buf, 0, 0, buf2.getWidth(), buf2.getWidth(), null);
		but1.setIcon(new ImageIcon(buf2));
		this.setSize(600, 400);
	}

	class MyPanel extends JPanel {
		Rectangle rec;
		int x, y;
		boolean inBound;
		MouseMotionListener current_l, l1, l2;

		public MyPanel() {
			rec = new Rectangle(0, 0, 100, 100);
			x = 50;
			y = 50;
			inBound = true;
			l1 = new MyListener1(this);
			l2 = new MyListener2(this);
			current_l = l1;
			addMouseMotionListener(current_l);
		}

		public void setAnchor(int x, int y) {
			this.x = x;
			this.y = y;
			if (rec.contains(x, y) != inBound) {
				inBound = !inBound;
				if (inBound) {
					this.removeMouseMotionListener(current_l);
					this.addMouseMotionListener(l1);
					current_l = l1;
				} else {
					this.removeMouseMotionListener(current_l);
					this.addMouseMotionListener(l2);
					current_l = l2;
				}
			}
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLUE);
			g2d.fill(rec);
			BufferedImage buf = null, buf2;
			try {
				buf = ImageIO.read(new File(Constant.IMAGE_PATH
								+ "and.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g2d.drawImage(buf, 100, 100, 50, 20, null);
		}
	}

	class MyListener1 extends MouseAdapter {
		int x = 0, y = 0;
		MyPanel myPanel;

		public MyListener1(MyPanel myPanel) {
			this.myPanel = myPanel;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (Math.abs(e.getX() - x) > 10 || Math.abs(e.getY() - y) > 10) {
				x = e.getX();
				y = e.getY();
				System.out.println("This is listener 1");
				myPanel.setAnchor(x, y);
			}
		}
	}

	class MyListener2 extends MouseAdapter {
		int x = 0, y = 0;
		MyPanel myPanel;

		public MyListener2(MyPanel myPanel) {
			this.myPanel = myPanel;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (Math.abs(e.getX() - x) > 10 || Math.abs(e.getY() - y) > 10) {
				x = e.getX();
				y = e.getY();
				System.out.println("This is listener 2");
				myPanel.setAnchor(x, y);
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				(new Test4()).setVisible(true);
			}
			
		});
	}
}
