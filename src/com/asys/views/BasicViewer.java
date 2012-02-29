/**
 * 
 */
package com.asys.views;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.util.ArrayList;
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
import com.asys.editor.model.Edge;
import com.asys.editor.model.EdgeManager;
import com.asys.editor.model.Element;
import com.asys.editor.model.GroupManager;
import com.asys.editor.model.Point;
import com.asys.editor.model.Wire;
import com.asys.editor.model.WireEdge;

/**
 * @author ryan
 * 
 */
public class BasicViewer extends JFrame {
	CircuitManager cm;
	List<Element> elts;
	List<Wire> wires;
	int m = Constant.GRID_SIZE;
	float r = Constant.HIGHLIGT_RADIUS;
	JLayeredPane layers;

	public BasicViewer(CircuitManager cm) {
		this.cm = cm;
		elts = cm.getElementManager().getElements();
		wires = cm.getWireManager().getWires();
		init();
		HighlightPanel highlightPanel = new HighlightPanel();
		highlightPanel.setSize(2000, 2000);
		layers.add(highlightPanel, new Integer(101));
	}

	public BasicViewer(GroupManager gm) {
		elts = gm.getElements();
		wires = gm.getInducedWires();
		init();
	}

	public BasicViewer(ClipBoard cb) {
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
		layers = new JLayeredPane();
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
				g2d.drawRect(x * m, y * m, w * m, h * m);
				Point p = new Point(x + w / 2, y + h / 2);
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

	class HighlightPanel extends JPanel {
		EdgeManager em;

		HighlightPanel() {
			em = cm.getEdgeManager();
			this.setOpaque(false);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			Composite oldComp = g2d.getComposite();
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(alphaComp);

			Paint p_old = g2d.getPaint();

			RadialGradientPaint radial_paint;
			LinearGradientPaint lp;
			Color[] c = { Color.RED, new Color(255, 0, 0, 0) };
			float[] dist = { 0f, 1f };

			ArrayList<Edge> overlap = em.getOverlapping();
			for (Edge edge : overlap) {
				Point p1 = edge.getP1();
				Point p2 = edge.getP2();
				if (Point.overlap(p1, p2)) {
					int x = p1.getX() * m;
					int y = p1.getY() * m;
					radial_paint = new RadialGradientPaint((float) x,
							(float) y, r, dist, c);
					g2d.setPaint(radial_paint);
					g2d.fillOval(Math.round(x - r), Math.round(y - r),
							Math.round(2 * r), Math.round(2 * r));
				} else {
					int x1 = p1.getX() * m;
					int y1 = p1.getY() * m;
					int x2 = p2.getX() * m;
					int y2 = p2.getY() * m;
					int min_x = Math.min(x1, x2);
					int max_x = Math.max(x1, x2);
					int min_y = Math.min(y1, y2);
					int max_y = Math.max(y1, y2);
					int x = x2 - x1;
					int y = y2 - y1;
					float mod = (float) Math.sqrt(x * x + y * y);
					float nx = ((float) x) / mod * r;
					float ny = ((float) y) / mod * r;
					lp = new LinearGradientPaint((float) x1, (float) y1,
							((float) x1) - ny, ((float) y1) + nx, dist, c,
							MultipleGradientPaint.CycleMethod.REFLECT);
					g2d.setPaint(lp);
					int[] xpoints = {Math.round(x1-ny),Math.round(x2-ny), Math.round(x2+ny), Math.round(x1+ny)};
					int[] ypoints = {Math.round(y1+nx), Math.round(y2+nx), Math.round(y2-nx), Math.round(y1-nx)};
					Polygon poly = new Polygon(xpoints, ypoints, 4);
//					g2d.fillRoundRect(Math.round(min_x - r),
//							Math.round(min_y - r),
//							Math.round(max_x - min_x + 2 * r),
//							Math.round(max_y - min_y + 2 * r), (int) r, (int) r);
					g2d.fill(poly);
				}
			}

			g2d.setPaint(p_old);
			g2d.setComposite(oldComp);
		}
	}
}
