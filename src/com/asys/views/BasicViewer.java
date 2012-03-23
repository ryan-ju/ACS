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
import com.asys.editor.model.RoutingPoint;
import com.asys.editor.model.SelectionManager;
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
	float t = Constant.HIGHLIGHT_TRANSPARANCY;
	JLayeredPane layers;

	public BasicViewer(CircuitManager cm) {
		this.cm = cm;
		elts = cm.getElementManager().getElements();
		wires = cm.getWireManager().getWires();
		init();
		ErrorHighlightPanel errorHighlightPanel = new ErrorHighlightPanel();
		errorHighlightPanel.setSize(2000, 2000);
		SelectionHighlightPanel selectionHighlightPanel = new SelectionHighlightPanel();
		selectionHighlightPanel.setSize(2000, 2000);
		layers.add(errorHighlightPanel, new Integer(102));
		layers.add(selectionHighlightPanel, new Integer(101));
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
		
		public GridPanel(){
			this.setBackground(Constant.BACKGROUND_CLR);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g.setColor(Constant.GRID_CLR);
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
				g2d.setColor(Constant.ELEMENT_BORDER_CLR);
				int x, y, w, h;
				x = elt.getX();
				y = elt.getY();
				w = elt.getWidth();
				h = elt.getHeight();
				g2d.drawRect(x * m, y * m, w * m, h * m);
				Point p = new Point(x + w / 2, y + h / 2);
				g2d.setColor(Color.BLUE);
				paintArrow(g2d, p, elt.getOrientation(), 2, 2);
			}
			g2d.setColor(Constant.WIRE_CLR);
			for (Wire wire : wires) {
				for (WireEdge e : wire.getRoutingEdges()) {
					g2d.drawLine(e.getP1().getX() * m, e.getP1().getY() * m, e
							.getP2().getX() * m, e.getP2().getY() * m);
					paintArrow(g2d, e.getP1(), e.getDirection(), 1f, 1f);
				}
			}
		}

		private void paintArrow(Graphics2D g, Point p, Direction dir,
				float base, float height) {
			int[] xs, ys;
			float b = base / 2, h = height;
			float x1, x2, x3, y1, y2, y3, x_temp, b_temp, y_temp;
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
				xs = new int[] { Math.round(x1), Math.round(x2), Math.round(x3) };
				ys = new int[] { Math.round(y1), Math.round(y2), Math.round(y3) };
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
				xs = new int[] { Math.round(x1), Math.round(x2), Math.round(x3) };
				ys = new int[] { Math.round(y1), Math.round(y2), Math.round(y3) };
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
				xs = new int[] { Math.round(x1), Math.round(x2), Math.round(x3) };
				ys = new int[] { Math.round(y1), Math.round(y2), Math.round(y3) };
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
				xs = new int[] { Math.round(x1), Math.round(x2), Math.round(x3) };
				ys = new int[] { Math.round(y1), Math.round(y2), Math.round(y3) };
				g.fillPolygon(xs, ys, 3);
				break;
			}
		}
	}

	class SelectionHighlightPanel extends JPanel {
		SelectionManager sm;

		SelectionHighlightPanel() {
			this.setOpaque(false);
			sm = SelectionManager.getInstance();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			
			Composite oldComp = g2d.getComposite();
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, t);
			g2d.setComposite(alphaComp);
			
			Paint p_old = g2d.getPaint();
			
			Wire wire = sm.getSelectedWire();
			if (wire!=null){
				paintWire(g, wire, Constant.WIRE_HIGHLIGHT_CLR, r);
			}
			
			WireEdge edge = sm.getSelectedWireEdge();
			if (edge!=null){
				paintWire(g, edge.getParent(), Constant.WIRE_EDGE_HIGHLIGHT_CLR, r);
				paintWireEdge(g, edge, new Color(0, 10, 0), r);
			}
			
			for (Element elt:sm.getGroupManager().getElements()){
				paintElement(g, elt, Constant.ELEMENT_HIGHLIGHT_CLR, r);
			}
			for (Wire ind_wire:sm.getGroupManager().getInducedWires()){
				paintWire(g, ind_wire, Constant.WIRE_HIGHLIGHT_CLR, r);
			}
			
			g2d.setPaint(p_old);
			g2d.setComposite(oldComp);
		}

	}

	class ErrorHighlightPanel extends JPanel {
		EdgeManager em;

		ErrorHighlightPanel() {
			em = cm.getEdgeManager();
			this.setOpaque(false);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			Composite oldComp = g2d.getComposite();
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, t);
			g2d.setComposite(alphaComp);

			Paint p_old = g2d.getPaint();

			RadialGradientPaint radial_paint;
			LinearGradientPaint lp;
			Color[] c = { Constant.ERROR_CLR, new Color(255, 0, 0, 0) };
			float[] dist = { 0f, 1f };

			ArrayList<Edge> overlap = em.getOverlapping();
			for (Edge edge : overlap) {
				Point p1 = edge.getP1();
				Point p2 = edge.getP2();
				if (Point.overlap(p1, p2)) { // So we paint a point
					int x = p1.getX() * m;
					int y = p1.getY() * m;
					radial_paint = new RadialGradientPaint((float) x,
							(float) y, r, dist, c);
					g2d.setPaint(radial_paint);
					g2d.fillOval(Math.round(x - r), Math.round(y - r),
							Math.round(2 * r), Math.round(2 * r));
				} else { // We paint a line
					int x1 = p1.getX() * m;
					int y1 = p1.getY() * m;
					int x2 = p2.getX() * m;
					int y2 = p2.getY() * m;
					int x = x2 - x1;
					int y = y2 - y1;
					float mod = (float) Math.sqrt(x * x + y * y);
					float nx = ((float) x) / mod * r;
					float ny = ((float) y) / mod * r;
					lp = new LinearGradientPaint((float) x1, (float) y1,
							((float) x1) - ny, ((float) y1) + nx, dist, c,
							MultipleGradientPaint.CycleMethod.REFLECT);
					g2d.setPaint(lp);
					int[] xpoints = { Math.round(x1 - ny), Math.round(x2 - ny),
							Math.round(x2 + ny), Math.round(x1 + ny) };
					int[] ypoints = { Math.round(y1 + nx), Math.round(y2 + nx),
							Math.round(y2 - nx), Math.round(y1 - nx) };
					Polygon poly = new Polygon(xpoints, ypoints, 4);
					g2d.fill(poly);
				}
			}

			g2d.setPaint(p_old);
			g2d.setComposite(oldComp);
		}
	}
	
	void paintWireEdge(Graphics g, WireEdge edge, Color color, float rad){
		Graphics2D g2d = (Graphics2D) g;
		
		LinearGradientPaint lp;
		float[] dist = { 0f, 1f };
		Color[] c = { color, new Color(255, 255, 255, 0) };
		
		RoutingPoint p1 = edge.getP1();
		RoutingPoint p2 = edge.getP2();

		int x1 = p1.getX() * m;
		int y1 = p1.getY() * m;
		int x2 = p2.getX() * m;
		int y2 = p2.getY() * m;
		int x = x2 - x1;
		int y = y2 - y1;
		float mod = (float) Math.sqrt(x * x + y * y);
		float nx = ((float) x) / mod * rad;
		float ny = ((float) y) / mod * rad;
		lp = new LinearGradientPaint((float) x1, (float) y1, ((float) x1)
				- ny, ((float) y1) + nx, dist, c,
				MultipleGradientPaint.CycleMethod.REFLECT);
		g2d.setPaint(lp);
		int[] xpoints = { Math.round(x1 - ny), Math.round(x2 - ny),
				Math.round(x2 + ny), Math.round(x1 + ny) };
		int[] ypoints = { Math.round(y1 + nx), Math.round(y2 + nx),
				Math.round(y2 - nx), Math.round(y1 - nx) };
		Polygon poly = new Polygon(xpoints, ypoints, 4);
		g2d.fill(poly);
		
	}

	void paintWire(Graphics g, Wire wire, Color color, float rad) {
		Graphics2D g2d = (Graphics2D) g;
		LinearGradientPaint lp;
		float[] dist = { 0f, 1f };
		Color[] c = { color, new Color(255, 255, 255, 0) };
		for (WireEdge edge : wire.getRoutingEdges()) {
			RoutingPoint p1 = edge.getP1();
			RoutingPoint p2 = edge.getP2();

			int x1 = p1.getX() * m;
			int y1 = p1.getY() * m;
			int x2 = p2.getX() * m;
			int y2 = p2.getY() * m;
			int x = x2 - x1;
			int y = y2 - y1;
			float mod = (float) Math.sqrt(x * x + y * y);
			float nx = ((float) x) / mod * rad;
			float ny = ((float) y) / mod * rad;
			lp = new LinearGradientPaint((float) x1, (float) y1, ((float) x1)
					- ny, ((float) y1) + nx, dist, c,
					MultipleGradientPaint.CycleMethod.REFLECT);
			g2d.setPaint(lp);
			int[] xpoints = { Math.round(x1 - ny), Math.round(x2 - ny),
					Math.round(x2 + ny), Math.round(x1 + ny) };
			int[] ypoints = { Math.round(y1 + nx), Math.round(y2 + nx),
					Math.round(y2 - nx), Math.round(y1 - nx) };
			Polygon poly = new Polygon(xpoints, ypoints, 4);
			g2d.fill(poly);
		}
	}

	void paintElement(Graphics g, Element elt, Color color, float rad) {
		Graphics2D g2d = (Graphics2D) g;

		RadialGradientPaint radial_paint;
		LinearGradientPaint lp;
		Color[] c = { color, new Color(255, 255, 255, 0) };
		float[] dist = { 0f, 1f };

		if (elt.getHeight() <= 0 && elt.getWidth() <= 0) {
			Point p = elt.getPosition();
			int x = p.getX() * m;
			int y = p.getY() * m;
			radial_paint = new RadialGradientPaint((float) x, (float) y, rad,
					dist, c);
			g2d.setPaint(radial_paint);
			g2d.fillOval(Math.round(x - rad), Math.round(y - rad),
					Math.round(2 * rad), Math.round(2 * rad));
		} else {
			ArrayList<Point> points = new ArrayList<Point>();
			points.add(new Point(elt.getX()+elt.getWidth(), elt.getY()));
			points.add(new Point(elt.getX()+elt.getWidth(), elt.getY()+elt.getHeight()));
			points.add(new Point(elt.getX(), elt.getY()+elt.getHeight()));
			points.add(new Point(elt.getX(), elt.getY()));
			
			Point p1 = new Point(elt.getX(), elt.getY());
			Point p2;
			for (Point point:points){
				p2 = point;
				int x1 = p1.getX() * m;
				int y1 = p1.getY() * m;
				int x2 = p2.getX() * m;
				int y2 = p2.getY() * m;
				int x = x2 - x1;
				int y = y2 - y1;
				float mod = (float) Math.sqrt(x * x + y * y);
				float nx = 2*((float) x) / mod * rad;
				float ny = 2*((float) y) / mod * rad;
				lp = new LinearGradientPaint((float) x1, (float) y1,
						((float) x1) - ny, ((float) y1) + nx, dist, c,
						MultipleGradientPaint.CycleMethod.NO_CYCLE);
				g2d.setPaint(lp);
				int[] xpoints = { Math.round(x1 - ny), Math.round(x2 - ny),
						x2, x1 };
				int[] ypoints = { Math.round(y1 + nx), Math.round(y2 + nx),
						y2, y1 };
				Polygon poly = new Polygon(xpoints, ypoints, 4);
				g2d.fill(poly);
				
				p1 = p2;
			}
		}
	}
}
