/**
 * 
 */
package com.asys.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.asys.constants.Constant;
import com.asys.constants.Direction;
import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.ClipBoard;
import com.asys.editor.model.Element;
import com.asys.editor.model.ElementManager;
import com.asys.editor.model.GroupManager;
import com.asys.editor.model.Point;
import com.asys.editor.model.Wire;
import com.asys.editor.model.WireEdge;
import com.asys.editor.model.WireManager;

/**
 * @author ryan
 * 
 */
public class BasicViewer extends JFrame {
	CircuitManager cm;
//	ElementManager em;
//	WireManager wm;
	List<Element> elts;
	List<Wire> wires;
	int m = Constant.GRID_SIZE;

	public BasicViewer(CircuitManager cm) {
		this.cm = cm;
		elts = cm.getElementManager().getElements();
		wires = cm.getWireManager().getWires();
		init();
	}
	
	public BasicViewer(GroupManager gm){
		elts = gm.getElements();
		wires = gm.getInducedWires();
		init();
	}
	
	public BasicViewer(ClipBoard cb){
		elts = cb.getElements();
		wires = cb.getWires();
		init();
	}

	private void init() {
		this.setSize(600, 400);
		Container content = this.getContentPane();
		GridPanel gridPanel = new GridPanel();
		CircuitPanel circuitPanel = new CircuitPanel();
		gridPanel.setSize(2000, 2000);
		// gridPanel.setBounds(0, 0, 1000, 1000);
		circuitPanel.setSize(2000, 2000);
		// circuitPanel.setBounds(0, 0, 1000, 1000);
		JLayeredPane layers = new JLayeredPane();
		layers.add(gridPanel, new Integer(1));
		layers.add(circuitPanel, new Integer(100));
		layers.setPreferredSize(new Dimension(2000, 2000));
		JScrollPane scrollPane = new JScrollPane(layers);
		content.add(scrollPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	class TestPanel extends JPanel {
		public TestPanel() {
			this.add(new JButton("Test Button"), BorderLayout.CENTER);
		}
	}

	class GridPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g.setColor(Color.LIGHT_GRAY);
			int i = 0, max_x = this.getWidth() / m, max_y = this.getHeight()
					/ m;
			while (i <= max_x) {
				int j = 0;
				while (j <= max_y) {
					int x = i * m, y = j * m;
					g2d.drawLine(x, y, x, y);
					j++;
				}
				i++;
			}
		}
	}

	class CircuitPanel extends JPanel {
		public CircuitPanel() {
			this.setOpaque(false);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			for (Element elt : elts) {
				g2d.setColor(Color.BLACK);
				int x, y, w, h;
				x = elt.getX();
				y = elt.getY();
				w = elt.getWidth();
				h = elt.getHeight();
				g2d.drawRect(x*m,y*m,w*m,h*m);
				Point p = new Point(x+w/2, y+h/2);
				g2d.setColor(Color.PINK);
				paintArrow(g2d, p, elt.getOrientation(), 2, 2);
			}
			g2d.setColor(Color.BLACK);
			for (Wire wire : wires) {
				for (WireEdge e : wire.getRoutingEdges()) {
					g2d.drawLine(e.getP1().getX() * m, e.getP1().getY() * m, e
							.getP2().getX() * m, e.getP2().getY() * m);
					paintArrow(g2d, e.getP1(), e.getDirection(), 2, 1);
				}
			}
		}

		private void paintArrow(Graphics2D g, Point p, Direction dir, int base,
				int height) {
			int[] xs, ys;
			int b = base / 2, h = height;
			int x1, x2, x3, y1, y2, y3, x_temp, b_temp, y_temp;
			switch (dir) {
			case UP:
				x_temp = p.getX() * m;
				b_temp = b * m / 2;
				y_temp = p.getY() * m;
				x1 = x_temp - b_temp;
				x2 = x_temp + b_temp;
				x3 = x_temp;
				y1 = y_temp;
				y2 = y_temp;
				y3 = y_temp - h * m;
				xs = new int[] { x1, x2, x3 };
				ys = new int[] { y1, y2, y3 };
				g.fillPolygon(xs, ys, 3);
				break;
			case DOWN:
				x_temp = p.getX() * m;
				b_temp = b * m / 2;
				y_temp = p.getY() * m;
				x1 = x_temp - b_temp;
				x2 = x_temp + b_temp;
				x3 = x_temp;
				y1 = y_temp;
				y2 = y_temp;
				y3 = y_temp + h * m;
				xs = new int[] { x1, x2, x3 };
				ys = new int[] { y1, y2, y3 };
				g.fillPolygon(xs, ys, 3);
				break;
			case LEFT:
				x_temp = p.getX() * m;
				b_temp = b * m / 2;
				y_temp = p.getY() * m;
				x1 = x_temp;
				x2 = x_temp;
				x3 = x_temp - h * m;
				y1 = y_temp - b_temp;
				y2 = y_temp + b_temp;
				y3 = y_temp;
				xs = new int[] { x1, x2, x3 };
				ys = new int[] { y1, y2, y3 };
				g.fillPolygon(xs, ys, 3);
				break;
			case RIGHT:
				x_temp = p.getX() * m;
				b_temp = b * m / 2;
				y_temp = p.getY() * m;
				x1 = x_temp;
				x2 = x_temp;
				x3 = x_temp + h * m;
				y1 = y_temp - b_temp;
				y2 = y_temp + b_temp;
				y3 = y_temp;
				xs = new int[] { x1, x2, x3 };
				ys = new int[] { y1, y2, y3 };
				g.fillPolygon(xs, ys, 3);
				break;
			}
		}
	}
}
