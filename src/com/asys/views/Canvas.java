/**
 * 
 */
package com.asys.views;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.asys.constants.CommandName;
import com.asys.constants.Constant;
import com.asys.constants.Direction;
import com.asys.constants.ImageFetcher;
import com.asys.editor.model.AndGate;
import com.asys.editor.model.CGate;
import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.CircuitManagerListener;
import com.asys.editor.model.ClipBoard;
import com.asys.editor.model.Command;
import com.asys.editor.model.Edge;
import com.asys.editor.model.EdgeManager;
import com.asys.editor.model.Element;
import com.asys.editor.model.ElementCreationManager;
import com.asys.editor.model.ElementCreationManagerListener;
import com.asys.editor.model.EnvironmentGate;
import com.asys.editor.model.Executor;
import com.asys.editor.model.Fanout;
import com.asys.editor.model.Inport;
import com.asys.editor.model.InputGate;
import com.asys.editor.model.NandGate;
import com.asys.editor.model.NorGate;
import com.asys.editor.model.NotGate;
import com.asys.editor.model.OrGate;
import com.asys.editor.model.Outport;
import com.asys.editor.model.OutputGate;
import com.asys.editor.model.Point;
import com.asys.editor.model.Rectangle;
import com.asys.editor.model.RoutingPoint;
import com.asys.editor.model.SelectionManager;
import com.asys.editor.model.SelectionManagerListener;
import com.asys.editor.model.Wire;
import com.asys.editor.model.WireCreationManager;
import com.asys.editor.model.WireCreationManagerListener;
import com.asys.editor.model.WireEdge;
import com.asys.editor.model.WireEdgeCreationManager;
import com.asys.editor.model.WireEdgeCreationManagerListener;
import com.asys.editor.model.XorGate;

/**
 * The has multiple layers, each layer is responsible for a specific task.
 * 
 * @author ryan
 * 
 */
public class Canvas extends JScrollPane implements ModeManagerListener {
	private CircuitManager cm;
	private SelectionManager sm;
	private ElementCreationManager ecm;
	private WireCreationManager wcm;
	private WireEdgeCreationManager wecm;
	private Executor ext;
	private final int m = Constant.GRID_SIZE;
	private final float r = Constant.HIGHLIGT_RADIUS;
	private final float t = Constant.HIGHLIGHT_TRANSPARANCY;
	private JLayeredPane layers;
	private GridPanel grid_pl;
	private CircuitPanel circuit_pl;
	private SelectionHighlightPanel shl_pl;
	private DragSelectionHighlightPanel dshl_pl;
	private ErrorHighlightPanel ehl_pl;
	private GateCreationPanel ec_pl;
	private WireCreationPanel wc_pl;
	private WireEdgeCreationPanel wec_pl;
	private ControlPanel control_pl;
	static private InnerElementPainter iePainter;

	public Canvas() {
		this.cm = CircuitManager.getInstance();
		this.sm = SelectionManager.getInstance();
		this.ecm = ElementCreationManager.getInstance();
		this.wcm = WireCreationManager.getInstance();
		this.wecm = WireEdgeCreationManager.getInstance();
		this.ext = Executor.getInstance();
		if (iePainter == null) {
			this.iePainter = new InnerElementPainter();
		}
		init();
	}

	public void init() {
		this.setSize(800, 600);
		layers = new JLayeredPane();
		grid_pl = new GridPanel();
		circuit_pl = new CircuitPanel();
		shl_pl = new SelectionHighlightPanel();
		ehl_pl = new ErrorHighlightPanel();
		dshl_pl = new DragSelectionHighlightPanel();
		wc_pl = new WireCreationPanel();
		wec_pl = new WireEdgeCreationPanel();
		ec_pl = new GateCreationPanel();
		control_pl = new ControlPanel();
		TestPanel test_pl = new TestPanel();
		grid_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		circuit_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		shl_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		ehl_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		dshl_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		wc_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		wec_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		ec_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		control_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);
		test_pl.setSize(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT);

		wc_pl.setVisible(false);

		sm.addListener(shl_pl);
		cm.addCircuitManagerListener(circuit_pl);
		cm.addCircuitManagerListener(ehl_pl);
		wcm.addListener(wc_pl);
		wecm.addListener(wec_pl);
		ecm.addListener(ec_pl);

		layers.setPreferredSize(new Dimension(Constant.DEFAULT_CANVAS_WIDTH,
				Constant.DEFAULT_CANVAS_HEIGHT));
		layers.add(grid_pl, new Integer(100));
		layers.add(circuit_pl, new Integer(101));
		layers.add(ehl_pl, new Integer(102));
		layers.add(shl_pl, new Integer(103));
		layers.add(dshl_pl, new Integer(104));
		layers.add(test_pl, new Integer(105));
		layers.add(wc_pl, new Integer(106));
		layers.add(wec_pl, new Integer(107));
		layers.add(ec_pl, new Integer(108));
		layers.add(control_pl, new Integer(109));

		this.getViewport().add(layers);

		this.setVisible(true);
	}

	@Override
	public void modeChanged(Mode mode) {
		control_pl.modeChanged(mode);
	}

	class ControlPanel extends JPanel implements ModeManagerListener {
		private int x, y;
		Command cmd;
		private GateCreationMouseAdapter gc_ma;
		private EditingMouseAdapter e_ma;
		private WireCreationMouseAdapter wc_ma;
		private WireEdgeCreationMouseAdapter wec_ma;
		// private SelectionMouseAdapter s_ma;
		private GateCreationKeyAdapter gc_ka;
		private EditingKeyAdapter e_ka;
		private WireCreationKeyAdapter wc_ka;
		private WireEdgeCreationKeyAdapter wec_ka;
		private PopUpMenu pop;

		public ControlPanel() {
			this.setOpaque(false);
			this.cmd = new Command();
			this.gc_ma = new GateCreationMouseAdapter();
			this.e_ma = new EditingMouseAdapter();
			this.wc_ma = new WireCreationMouseAdapter();
			this.wec_ma = new WireEdgeCreationMouseAdapter();
			this.gc_ka = new GateCreationKeyAdapter();
			this.e_ka = new EditingKeyAdapter();
			this.wc_ka = new WireCreationKeyAdapter();
			this.wec_ka = new WireEdgeCreationKeyAdapter();
			this.pop = new PopUpMenu();
			changeMouseListener(e_ma);
			changeMouseMotionListener(e_ma);
			this.getActionMap().put("undo", new UndoAction());
			this.getActionMap().put("rotate_clockwise",
					new RotateClockwiseAction());
			this.getActionMap().put("rotate_anticlockwise",
					new RotateAntiClockwiseAction());
			this.getActionMap().put("delete", new DeleteAction());
		}

		protected void changeMouseListener(MouseListener ml) {
			for (MouseListener l : this.getMouseListeners().clone()) {
				this.removeMouseListener(l);
			}
			this.addMouseListener(ml);
		}

		protected void changeMouseMotionListener(MouseMotionListener mml) {
			for (MouseMotionListener l : this.getMouseMotionListeners().clone()) {
				this.removeMouseMotionListener(l);
			}
			this.addMouseMotionListener(mml);
		}

		protected void changeKeyListener(KeyListener kl) {
			for (KeyListener l : this.getKeyListeners().clone()) {
				this.removeKeyListener(l);
			}
			this.addKeyListener(kl);
		}

		@Override
		public void modeChanged(Mode mode) {
			switch (mode) {
			case ELEMENT_CREATION_MODE:
				changeMouseListener(gc_ma);
				changeMouseMotionListener(gc_ma);
				changeKeyListener(gc_ka);
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.UNDO));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.ROTATE_RIGHT));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.ROTATE_LEFT));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
				break;
			case WIRE_CREATION_MODE:
				changeMouseListener(wc_ma);
				changeMouseMotionListener(wc_ma);
				changeKeyListener(wc_ka);
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.UNDO));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.ROTATE_RIGHT));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.ROTATE_LEFT));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
				break;
			case WIRE_EDGE_CREATION_MODE:
				changeMouseListener(wec_ma);
				changeMouseMotionListener(wec_ma);
				changeKeyListener(wec_ka);
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.UNDO));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.ROTATE_RIGHT));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(Constant.ROTATE_LEFT));
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(
						KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
				break;
			case EDIT_MODE:
				changeMouseListener(e_ma);
				changeMouseMotionListener(e_ma);
				changeKeyListener(e_ka);
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
						KeyStroke.getKeyStroke(Constant.UNDO), "undo");
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
						KeyStroke.getKeyStroke(Constant.ROTATE_RIGHT),
						"rotate_clockwise");
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
						KeyStroke.getKeyStroke(Constant.ROTATE_LEFT),
						"rotate_anticlockwise");
				this.getInputMap(WHEN_IN_FOCUSED_WINDOW)
						.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
								"delete");
				break;
			}
		}

		/*
		 * Return true if the coordinates (measured on the grid, not screen)
		 * have changed.
		 */
		private boolean coordinateConvert(int x, int y) {
			int old_x = this.x;
			int old_y = this.y;
			this.x = (2 * x + m) / (2 * m);
			this.y = (2 * y + m) / (2 * m);
			if (this.x == old_x && this.y == old_y) {
				return false;
			}
			return true;
		}

		// ====================================================
		// UndoAction
		// ====================================================
		private class UndoAction extends AbstractAction {

			@Override
			public void actionPerformed(ActionEvent e) {
				cmd.setCommandName(CommandName.UNDO);
				cmd.setParams(new Object[] {});
				ext.execute(cmd);
			}

		}

		// ====================================================
		// RotateClockwiseAction
		// ====================================================
		private class RotateClockwiseAction extends AbstractAction {

			@Override
			public void actionPerformed(ActionEvent e) {
				cmd.setCommandName(CommandName.ROTATE);
				cmd.setParams(new Object[] { true });
				ext.execute(cmd);
			}

		}

		// ====================================================
		// RotateAntiClockwiseAction
		// ====================================================
		private class RotateAntiClockwiseAction extends AbstractAction {

			@Override
			public void actionPerformed(ActionEvent e) {
				cmd.setCommandName(CommandName.ROTATE);
				cmd.setParams(new Object[] { false });
				ext.execute(cmd);
			}

		}

		// ====================================================
		// DeleteAction
		// ====================================================
		private class DeleteAction extends AbstractAction {

			@Override
			public void actionPerformed(ActionEvent e) {
				cmd.setCommandName(CommandName.DELETE);
				cmd.setParams(new Object[] {});
				ext.execute(cmd);
			}

		}

		// ====================================================
		// GateCreation Adapters
		// ====================================================
		private class GateCreationMouseAdapter extends MouseAdapter {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (coordinateConvert(e.getX(), e.getY())) {
					if (ecm.getCreation() != null) {
						ecm.setX(ControlPanel.this.x);
						ecm.setY(ControlPanel.this.y);
						System.out.println("x = " + x + ", y = " + y);
					} else {
						System.out.println("ElementCreationManager is empty!");
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getButton() == MouseEvent.BUTTON1) {
					coordinateConvert(e.getX(), e.getY());
					cmd.setCommandName(CommandName.CREATE_GATE);
					cmd.setParams(new Object[] { x, y });
					ext.execute(cmd);
				} else {
					ecm.finish();
					ModeManager.getInstance().setMode(Mode.EDIT_MODE);
				}
			}
		}

		private class GateCreationKeyAdapter extends KeyAdapter {

		}

		// ====================================================
		// WireCreation Adapters
		// ====================================================

		private class WireCreationMouseAdapter extends MouseAdapter {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (coordinateConvert(e.getX(), e.getY())) {
					System.out.println("Mouse moved! x = " + x + ", y = " + y);
					wcm.setBoth(x, y);
					System.out.println("wcm x = " + wcm.getX() + ", y = "
							+ wcm.getY());
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				coordinateConvert(e.getX(), e.getY());
				if (e.getButton() == MouseEvent.BUTTON1) {
					wcm.setBoth(x, y);
					wcm.addRoutingPoint();
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					wcm.removeLastRoutingPoint();
				}
				if (!wcm.isCreating()) {
					ModeManager.getInstance().setMode(Mode.EDIT_MODE);
				}
			}
		}

		private class WireCreationKeyAdapter extends KeyAdapter {

		}

		// ====================================================
		// WireEdgeCreation Adapters
		// ====================================================
		private class WireEdgeCreationMouseAdapter extends MouseAdapter {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (coordinateConvert(e.getX(), e.getY())) {
					wecm.setX2(x);
					wecm.setY2(y);
					System.out.println("x = " + x + ", y = " + y);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				coordinateConvert(e.getX(), e.getY());
				if (e.getButton() == MouseEvent.BUTTON1) {
					wecm.finish();
					ModeManager.getInstance().setMode(Mode.EDIT_MODE);
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					wecm.cancel();
					ModeManager.getInstance().setMode(Mode.EDIT_MODE);
				}
			}
		}

		private class WireEdgeCreationKeyAdapter extends KeyAdapter {
			@Override
			public void keyPressed(KeyEvent e) {

			}
		}

		// ====================================================
		// Editing Adapters
		// ====================================================
		private class EditingMouseAdapter extends MouseAdapter {
			private boolean pressedOnSelected = false;
			private int x0, y0;

			/**
			 * If 'sm' is empty, then a mouse press on any Element or Wire
			 * selects it. If 'sm' is not empty, then a mouse press on the
			 * selected Element or Wire starts a moving action, and a mouse
			 * press outside the selected deselects everything.
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				coordinateConvert(e.getX(), e.getY());
				pressedOnSelected = false;
				x0 = x;
				y0 = y;
				if (sm.isEmpty()) {
					dshl_pl.start();
				} else {
					if (sm.getSelectedWireEdge() != null) {
						if (WireEdge.isOnWireEdge(new Point(x, y),
								sm.getSelectedWireEdge())) {
							pressedOnSelected = true;
						} else {
							dshl_pl.start();
						}
					} else if (!sm.getGroupManager().isEmpty()) {
						if (sm.getGroupManager().isInBound(new Point(x, y))) {
							pressedOnSelected = true;
						} else {
							dshl_pl.start();
						}
					} else {
						assert sm.getSelectedWire() != null;
						dshl_pl.start();
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// If the button down is Button1
				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
					if (coordinateConvert(e.getX(), e.getY())) {
						if (pressedOnSelected) { // The dragging is to move
													// those
							// selected
							assert !sm.isEmpty();
							if (sm.getSelectedWireEdge() != null) {
								if (sm.getSelectedWireEdge().isVertical()) {
									shl_pl.setDisplacement(x - x0, 0);
								} else {
									shl_pl.setDisplacement(0, y - y0);
								}
							} else {
								assert !sm.getGroupManager().isEmpty();
								shl_pl.setDisplacement(x - x0, y - y0);
							}
						} else { // The dragging is to drag out a selection
							// rectangle
							dshl_pl.setAll(x0, y0, x - x0, y - y0);
						}
					}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				coordinateConvert(e.getX(), e.getY());
				// Only perform the block if there has been a drag, ie, !(x0
				// ==
				// x && y0 == y)
				if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0
						&& !(x0 == x && y0 == y)) {
					if (pressedOnSelected) {
						cmd.setCommandName(CommandName.MOVE);
						cmd.setParams(new Object[] { x - x0, y - y0 });
						ext.execute(cmd);
						shl_pl.resetDisplacement();
					} else {
						ArrayList<Element> elts_selected = cm
								.getElementManager().getElementsInRectangle(
										new Rectangle(x0, y0, x - x0, y - y0));
						if (e.isControlDown()) {
							cmd.setCommandName(CommandName.SELECT_GROUP_MULTI_ELEMENT);
						} else {
							cmd.setCommandName(CommandName.SELECT_GROUP_ELEMENT);
						}
						cmd.setParams(new Object[] { elts_selected });
						ext.execute(cmd);
						dshl_pl.finish();
					}
					x0 = x;
					y0 = y;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				coordinateConvert(e.getX(), e.getY());
				if (e.getButton() == MouseEvent.BUTTON3) {
					pop.build();
					if (pop.canShow()) {
						pop.show(ControlPanel.this, x * m, y * m);
					}
				} else if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
					x0 = x;
					y0 = y;
					// If Ctrl is held down
					if (e.isControlDown()) {
						Element elt_selected = cm.getElementManager()
								.getElementAt(x, y);
						if (elt_selected != null) {
							cmd.setCommandName(CommandName.SELECT_MULTI_ELEMENT);
							cmd.setParams(new Object[] { elt_selected });
							ext.execute(cmd);
						}
					} else { // If Ctrl is not held down
						if (e.getClickCount() >= 2) { // If double clicked
							Wire wire = cm.getWireManager().getWireAt(x, y);
							if (wire != null) {
								cmd.setCommandName(CommandName.SELECT_WIRE);
								cmd.setParams(new Object[] { wire });
								ext.execute(cmd);
								return;
							}
						} else { // If single clicked
							WireEdge edge = cm.getEdgeManager().getEdgeAt(x, y);
							if (edge != null) {
								cmd.setCommandName(CommandName.SELECT_WIRE_EDGE);
								cmd.setParams(new Object[] { edge });
								ext.execute(cmd);
								return;
							}
							Element elt = cm.getElementManager().getElementAt(
									x, y);
							if (elt != null) {
								cmd.setCommandName(CommandName.SELECT_ELEMENT);
								cmd.setParams(new Object[] { elt });
								ext.execute(cmd);
								return;
							}
						}
						cmd.setCommandName(CommandName.DESELECT);
						cmd.setParams(new Object[] {});
						ext.execute(cmd);
						return;

					}
				}
			}

			public boolean getPressedOnSelected() {
				return pressedOnSelected;
			}
		}

		private class EditingKeyAdapter extends KeyAdapter {
			@Override
			public void keyPressed(KeyEvent e) {

			}
		}

		/*
		 * The pop up menu's content depends on many parameters. Need to call
		 * build before showing the pop up menu.
		 */
		private class PopUpMenu extends JPopupMenu {
			boolean canShow = false;
			Outport available_op = null;

			public PopUpMenu() {
				this.setBorder(BorderFactory.createRaisedBevelBorder());
			}

			/*
			 * There are several cases of where the pop up menu can shown: On an
			 * unconnected Outport, on a selected wire.
			 */
			void build() {
				canShow = false;
				this.removeAll();
				this.available_op = null;
				ArrayList<Outport> ops = cm.getElementManager().getOutportAt(x,
						y);
				if (!ops.isEmpty()) {
					available_op = null;
					for (Outport op : ops) {
						if (op.getWire() == null) {
							available_op = op;
							break;
						}
					}
					if (available_op != null) {
						JMenuItem add_wire_item = new JMenuItem("Add wire");
						add_wire_item.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								wcm.setOutport(available_op);
								ModeManager.getInstance().setMode(
										Mode.WIRE_CREATION_MODE);
							}

						});
						this.add(add_wire_item);
						canShow = true;
					}
				}
				final Wire selected_wire = sm.getSelectedWire();
				if (selected_wire != null) {
					final int we_index = selected_wire.getWireEdgeIndexAt(x, y);
					if (we_index >= 0) {
						// JMenuItem for adding routing points
						JMenuItem add_rp_item = new JMenuItem(
								"Add routing points");
						add_rp_item.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								wecm.start(selected_wire, we_index, x, y);
								ModeManager.getInstance().setMode(
										Mode.WIRE_EDGE_CREATION_MODE);
							}

						});
						// JMenuItem for adding a Fanout
						JMenuItem add_fanout_item = new JMenuItem("Add fanout");
						add_fanout_item.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								cmd.setCommandName(CommandName.CREATE_FANOUT);
								cmd.setParams(new Object[] { selected_wire,
										we_index, x, y });
								ext.execute(cmd);
							}

						});
						this.add(add_rp_item);
						this.add(add_fanout_item);
						canShow = true;
					}
				}
				// If clicked inside the selected region
				if (e_ma.getPressedOnSelected()) {
					// Add a separator
					this.addSeparator();
					// JMenuItem for copying the selection
					JMenuItem copy_item = new JMenuItem("copy");
					copy_item.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							cmd.setCommandName(CommandName.COPY);
							cmd.setParams(new Object[]{});
							ext.execute(cmd);
						}

					});
					this.add(copy_item);
					canShow = true;
				}
				// If clipboard is not empty
				if (!ClipBoard.getInstance().isEmpty()) {
					// JMenuItem for pasting
					JMenuItem paste_item = new JMenuItem("paste");
					paste_item.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							cmd.setCommandName(CommandName.PASTE);
							cmd.setParams(new Object[]{x, y});
							ext.execute(cmd);
						}

					});
					this.add(paste_item);
					canShow = true;
				}
			}

			boolean canShow() {
				return canShow;
			}
		}
	}

	class GateCreationPanel extends JPanel implements
			ElementCreationManagerListener {

		GateCreationPanel() {
			this.setOpaque(false);
			this.setVisible(false);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			Element elt = ecm.getCreation();
			if (elt instanceof InputGate) {
				iePainter.paint((InputGate) elt, g2d);
			} else if (elt instanceof OutputGate) {
				iePainter.paint((OutputGate) elt, g2d);
			} else if (elt instanceof EnvironmentGate) {
				iePainter.paint((EnvironmentGate) elt, g2d);
			} else if (elt instanceof NotGate) {
				iePainter.paint((NotGate) elt, g2d);
			} else if (elt instanceof AndGate) {
				iePainter.paint((AndGate) elt, g2d);
			} else if (elt instanceof OrGate) {
				iePainter.paint((OrGate) elt, g2d);
			} else if (elt instanceof NandGate) {
				iePainter.paint((NandGate) elt, g2d);
			} else if (elt instanceof NorGate) {
				iePainter.paint((NorGate) elt, g2d);
			} else if (elt instanceof XorGate) {
				iePainter.paint((XorGate) elt, g2d);
			} else if (elt instanceof CGate) {
				iePainter.paint((CGate) elt, g2d);
			}
			if (!ecm.canCreate()) {
				paintCross(elt, g2d);
			}
		}

		private void paintCross(Element elt, Graphics2D g2d) {
			int xr = elt.getX() * m;
			int yr = elt.getY() * m;
			int wr = elt.getWidth() * m;
			int hr = elt.getHeight() * m;
			int xc = xr + wr / 2;
			int yc = yr + hr / 2;
			Color old_color = g2d.getColor();
			Stroke old_stroke = g2d.getStroke();
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(m / 2));
			int l = 1;
			g2d.drawLine(xc - l * m, yc - l * m, xc + l * m, yc + l * m);
			g2d.drawLine(xc + l * m, yc - l * m, xc - l * m, yc + l * m);
			g2d.setColor(old_color);
			g2d.setStroke(old_stroke);
		}

		@Override
		public void started() {
			this.setVisible(true);
		}

		@Override
		public void finished() {
			this.setVisible(false);
		}

		@Override
		public void cancelled() {
			this.setVisible(false);
		}

		@Override
		public void changedCoordinates() {
			this.repaint();
		}
	}

	class WireCreationPanel extends JPanel implements
			WireCreationManagerListener {
		WireCreationPanel() {
			this.setOpaque(false);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			Composite oldComp = g2d.getComposite();
			Stroke oldStroke = g2d.getStroke();
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, t);
			g2d.setComposite(alphaComp);

			if (wcm.isCreating()) {
				assert wcm.getOutport() != null;
				Element out_elt = wcm.getOutport().getParent();
				highlightElement(g, out_elt, 0, 0, true,
						Constant.WIRE_CREATION_CLR, 2 * r);
				LinkedList<RoutingPoint> rps = wcm.getRoutingPoints();
				rps.addFirst(new RoutingPoint(wcm.getOutport().getPosition()));
				RoutingPoint rpa = null, rpb = rps.getFirst();
				int i = 0, xa, ya, xb, yb;
				for (RoutingPoint rp : rps) {
					rpb = rp;
					if (i > 0) {
						xa = rpa.getX() * m;
						ya = rpa.getY() * m;
						xb = rpb.getX() * m;
						yb = rpb.getY() * m;
						g2d.setColor(Constant.WIRE_CREATION_CLR);
						g2d.setStroke(new BasicStroke(
								Constant.WIRE_CREATION_WIDTH_RATIO * m,
								BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
						g2d.drawLine(xa, ya, xb, yb);
					}
					rpa = rpb;
					i++;
				}
				g2d.setColor(Constant.WIRE_CREATION_CLR);
				g2d.setStroke(new BasicStroke(Constant.WIRE_CREATION_WIDTH,
						BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(rpb.getX() * m, rpb.getY() * m, wcm.getX() * m,
						wcm.getY() * m);
				if (wcm.getIPTemp() != null) {
					highlightElement(g, wcm.getIPTemp().getParent(), 0, 0,
							true, Constant.WIRE_CREATION_CLR, 2 * r);
				}
			}

			g2d.setComposite(oldComp);
			g2d.setStroke(oldStroke);
		}

		@Override
		public void startedCreation() {
			this.setVisible(true);
		}

		@Override
		public void finishedCreation() {
			this.setVisible(false);
		}

		@Override
		public void cleared() {
			this.setVisible(false);
		}

		@Override
		public void addedPoint() {
			repaint();
		}

		@Override
		public void removedLastPoint() {
			repaint();
		}

		@Override
		public void coordinatesChanged() {
			repaint();
		}

		@Override
		public void cancelled() {
			repaint();
		}
	}

	class WireEdgeCreationPanel extends JPanel implements
			WireEdgeCreationManagerListener {

		WireEdgeCreationPanel() {
			this.setOpaque(false);
		}

		@Override
		public void started() {
			this.setVisible(true);
		}

		@Override
		public void coordinatesChanged() {
			this.repaint();
		}

		@Override
		public void cancelled() {
			this.setVisible(false);
		}

		@Override
		public void finished() {
			this.setVisible(false);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			Composite oldComp = g2d.getComposite();
			Stroke oldStroke = g2d.getStroke();
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, t);
			g2d.setComposite(alphaComp);

			if (wecm.isCreating()) {
				int x1r = wecm.getX1() * m, y1r = wecm.getY1() * m, x2r = wecm
						.getX2() * m, y2r = wecm.getY2() * m;
				g2d.setColor(Constant.WIRE_EDGE_CREATION_CLR);
				g2d.setStroke(new BasicStroke(
						Constant.WIRE_EDGE_CREATION_WIDTH,
						BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(x1r, y1r, x2r, y2r);
			}
			g2d.setComposite(oldComp);
			g2d.setStroke(oldStroke);
		}
	}

	class DragSelectionHighlightPanel extends JPanel {
		private int x0, y0, w, h;
		private boolean isCleared = true;

		protected DragSelectionHighlightPanel() {
			this.setOpaque(false);
		}

		void start() {
			this.isCleared = true;
			this.setVisible(true);
		}

		int getMinX() {
			return Math.min(x0, x0 + w);
		}

		int getMinY() {
			return Math.min(y0, y0 + h);
		}

		void setPostion(int x0, int y0) {
			this.x0 = x0;
			this.y0 = y0;
			isCleared = false;
			this.repaint();
		}

		int getW() {
			return Math.abs(w);
		}

		int getH() {
			return Math.abs(h);
		}

		void setDimension(int w, int h) {
			this.w = w;
			this.h = h;
			isCleared = false;
			this.repaint();
		}

		void setAll(int x0, int y0, int w, int h) {
			this.x0 = x0;
			this.y0 = y0;
			this.w = w;
			this.h = h;
			isCleared = false;
			this.repaint();
		}

		void finish() {
			isCleared = true;
			this.setVisible(false);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!isCleared) {
				Graphics2D g2d = (Graphics2D) g;

				Composite oldComp = g2d.getComposite();
				Stroke oldStroke = g2d.getStroke();
				Composite alphaComp = AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, (float) (t * 0.3));
				g2d.setComposite(alphaComp);

				g2d.setColor(Constant.SELECTION_RECTANGLE_CLR);
				g2d.fillRect(getMinX() * m, getMinY() * m, getW() * m, getH()
						* m);

				g2d.setComposite(oldComp);
				g2d.setStroke(oldStroke);
			}
		}

	}

	class GridPanel extends BufferedPanel {
		public GridPanel() {
			this.setBackground(Constant.BACKGROUND_CLR);
			this.setOpaque(true);
		}

		@Override
		void customizedPaint(Graphics2D g2d) {
			g2d.setColor(Constant.GRID_CLR);
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

	class CircuitPanel extends BufferedPanel implements CircuitManagerListener {
		private InnerElementPainter iep;

		public CircuitPanel() {
			this.setOpaque(false);
			this.iep = iePainter;
		}

		@Override
		public void update() {
			this.needToChange();
			this.repaint();
		}

		@Override
		void customizedPaint(Graphics2D g2d) {
			for (Element elt : cm.getElementManager().getElements()) {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Constant.ELEMENT_BORDER_CLR);
				if (elt instanceof InputGate) {
					iep.paint((InputGate) elt, g2d);
				} else if (elt instanceof OutputGate) {
					iep.paint((OutputGate) elt, g2d);
				} else if (elt instanceof EnvironmentGate) {
					iep.paint((EnvironmentGate) elt, g2d);
				} else if (elt instanceof NotGate) {
					iep.paint((NotGate) elt, g2d);
				} else if (elt instanceof AndGate) {
					iep.paint((AndGate) elt, g2d);
				} else if (elt instanceof OrGate) {
					iep.paint((OrGate) elt, g2d);
				} else if (elt instanceof NandGate) {
					iep.paint((NandGate) elt, g2d);
				} else if (elt instanceof NorGate) {
					iep.paint((NorGate) elt, g2d);
				} else if (elt instanceof XorGate) {
					iep.paint((XorGate) elt, g2d);
				} else if (elt instanceof CGate) {
					iep.paint((CGate) elt, g2d);
				} else if (elt instanceof Fanout) {
					iep.paint((Fanout) elt, g2d);
				} else {
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
			}
			g2d.setColor(Constant.WIRE_CLR);
			for (Wire wire : cm.getWireManager().getWires()) {
				for (WireEdge e : wire.getRoutingEdges()) {
					g2d.drawLine(e.getP1().getX() * m, e.getP1().getY() * m, e
							.getP2().getX() * m, e.getP2().getY() * m);
					// paintArrow(g2d, e.getP1(), e.getDirection(), 1f, 1f);
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

	class InnerElementPainter implements ElementRenderer {

		@Override
		public void paint(InputGate input, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(input), input, g2d);
		}

		@Override
		public void paint(OutputGate output, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(output), output, g2d);
		}

		@Override
		public void paint(EnvironmentGate env, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(env), env, g2d);
		}

		@Override
		public void paint(NotGate not, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(not), not, g2d);
		}

		@Override
		public void paint(AndGate and, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(and), and, g2d);
		}

		@Override
		public void paint(OrGate or, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(or), or, g2d);
		}

		@Override
		public void paint(NandGate nand, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(nand), nand, g2d);
		}

		@Override
		public void paint(NorGate nor, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(nor), nor, g2d);
		}

		@Override
		public void paint(XorGate xor, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(xor), xor, g2d);
		}

		@Override
		public void paint(CGate c, Graphics2D g2d) {
			paintSub(ImageFetcher.getElementImage(c), c, g2d);
		}

		@Override
		public void paint(Fanout elt, Graphics2D g2d) {
			int x = elt.getX() * m, y = elt.getY() * m;
			int s = Constant.FANOUT_RADIUS;
			g2d.setColor(Constant.ELEMENT_BORDER_CLR);
			g2d.fillOval(x - s, y - s, 2 * s, 2 * s);
		}

		/*
		 * w and h are width and height as they are seen (after considering the
		 * orientation).
		 */
		private void paintSub(Image img, Element elt, Graphics2D g2d) {
			/*
			 * Paint pins
			 */
			int dx = 0, dy = 0;
			int x1_ip, y1_ip, x2_ip, y2_ip;
			int w = elt.getWidth() * m, h = elt.getHeight() * m;
			switch (elt.getOrientation()) {
			case RIGHT:
				dx = 1;
				dy = 0;
				break;
			case DOWN:
				dx = 0;
				dy = 1;
				break;
			case LEFT:
				dx = -1;
				dy = 0;
				break;
			case UP:
				dx = 0;
				dy = -1;
				break;
			}
			for (Inport ip : elt.getInports()) {
				x1_ip = ip.getPosition().getX() * m;
				y1_ip = ip.getPosition().getY() * m;
				x2_ip = x1_ip + dx * w / 3;
				y2_ip = y1_ip + dy * h / 3;
				g2d.drawLine(x1_ip, y1_ip, x2_ip, y2_ip);
				g2d.fillOval(x1_ip - Constant.GATE_KNOB_SIZE / 2, y1_ip
						- Constant.GATE_KNOB_SIZE / 2, Constant.GATE_KNOB_SIZE,
						Constant.GATE_KNOB_SIZE);
			}
			dx = -dx;
			dy = -dy;
			for (Outport op : elt.getOutports()) {
				x1_ip = op.getPosition().getX() * m;
				y1_ip = op.getPosition().getY() * m;
				x2_ip = x1_ip + dx * w / 3;
				y2_ip = y1_ip + dy * h / 3;
				g2d.drawLine(x1_ip, y1_ip, x2_ip, y2_ip);
				g2d.fillOval(x1_ip - Constant.GATE_KNOB_SIZE / 2, y1_ip
						- Constant.GATE_KNOB_SIZE / 2, Constant.GATE_KNOB_SIZE,
						Constant.GATE_KNOB_SIZE);
			}
			/*
			 * Paint element icon
			 */
			int x_real = elt.getX() * m, y_real = elt.getY() * m;
			g2d.drawImage(img, x_real, y_real, null);
		}

	}

	class SelectionHighlightPanel extends BufferedPanel implements
			SelectionManagerListener {
		SelectionManager sm;
		int dx, dy;

		SelectionHighlightPanel() {
			this.setOpaque(false);
			sm = SelectionManager.getInstance();
		}

		public void setDisplacement(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
			update();
		}

		public void resetDisplacement() {
			this.dx = 0;
			this.dy = 0;
			update();
		}

		@Override
		public void update() {
			this.needToChange();
			this.repaint();
		}

		@Override
		void customizedPaint(Graphics2D g2d) {
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, t);
			g2d.setComposite(alphaComp);

			Wire wire = sm.getSelectedWire();
			if (wire != null) {
				highlightWire(g2d, wire, dx, dy, Constant.WIRE_HIGHLIGHT_CLR, r);
			}

			WireEdge edge = sm.getSelectedWireEdge();
			if (edge != null) {
				highlightWire(g2d, edge.getParent(), 0, 0,
						Constant.WIRE_EDGE_HIGHLIGHT_CLR, r);
				highlightWireEdge(g2d, edge, dx, dy, new Color(0, 10, 0), r);
			}

			for (Element elt : sm.getGroupManager().getElements()) {
				highlightElement(g2d, elt, dx, dy, true,
						Constant.ELEMENT_HIGHLIGHT_CLR, r);
			}
			for (Wire ind_wire : sm.getGroupManager().getInducedWires()) {
				highlightWire(g2d, ind_wire, dx, dy,
						Constant.WIRE_HIGHLIGHT_CLR, r);
			}
		}

	}

	class ErrorHighlightPanel extends BufferedPanel implements
			CircuitManagerListener {

		EdgeManager em;

		ErrorHighlightPanel() {
			em = cm.getEdgeManager();
			this.setOpaque(false);
		}

		@Override
		public void update() {
			this.needToChange();
			this.repaint();
		}

		@Override
		void customizedPaint(Graphics2D g2d) {
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, t);
			g2d.setComposite(alphaComp);

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
		}
	}

	class TestPanel extends BufferedPanel {

		TestPanel() {
			this.setOpaque(false);
		}

		@Override
		void customizedPaint(Graphics2D g2d) {

		}

	}

	void highlightWireEdge(Graphics g, WireEdge edge, int dx, int dy,
			Color color, float rad) {
		Graphics2D g2d = (Graphics2D) g;

		LinearGradientPaint lp;
		float[] dist = { 0f, 1f };
		Color[] c = { color, new Color(255, 255, 255, 0) };

		RoutingPoint p1 = edge.getP1();
		RoutingPoint p2 = edge.getP2();

		int x1 = (p1.getX() + dx) * m;
		int y1 = (p1.getY() + dy) * m;
		int x2 = (p2.getX() + dx) * m;
		int y2 = (p2.getY() + dy) * m;
		int x = x2 - x1;
		int y = y2 - y1;
		float mod = (float) Math.sqrt(x * x + y * y);
		float nx = ((float) x) / mod * rad;
		float ny = ((float) y) / mod * rad;
		lp = new LinearGradientPaint((float) x1, (float) y1, ((float) x1) - ny,
				((float) y1) + nx, dist, c,
				MultipleGradientPaint.CycleMethod.REFLECT);
		g2d.setPaint(lp);
		int[] xpoints = { Math.round(x1 - ny), Math.round(x2 - ny),
				Math.round(x2 + ny), Math.round(x1 + ny) };
		int[] ypoints = { Math.round(y1 + nx), Math.round(y2 + nx),
				Math.round(y2 - nx), Math.round(y1 - nx) };
		Polygon poly = new Polygon(xpoints, ypoints, 4);
		g2d.fill(poly);

	}

	void highlightWire(Graphics g, Wire wire, int dx, int dy, Color color,
			float rad) {
		if (wire != null) {
			Graphics2D g2d = (Graphics2D) g;
			LinearGradientPaint lp;
			float[] dist = { 0f, 1f };
			Color[] c = { color, new Color(255, 255, 255, 0) };
			for (WireEdge edge : wire.getRoutingEdges()) {
				RoutingPoint p1 = edge.getP1();
				RoutingPoint p2 = edge.getP2();

				int x1 = (p1.getX() + dx) * m;
				int y1 = (p1.getY() + dy) * m;
				int x2 = (p2.getX() + dx) * m;
				int y2 = (p2.getY() + dy) * m;
				int x = x2 - x1;
				int y = y2 - y1;
				float mod = (float) Math.sqrt(x * x + y * y);
				float nx = ((float) x) / mod * rad;
				float ny = ((float) y) / mod * rad;
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
		} else {
			System.out.println("Cannot paint null Wire!");
		}
	}

	void highlightElement(Graphics g, Element elt, int dx, int dy,
			boolean innerGlow, Color color, float rad) {
		if (elt != null) {
			int inner_glow_coef = innerGlow ? 1 : -1;
			Graphics2D g2d = (Graphics2D) g;
			// If 'elt' is a Fanout, paint the four corners around it
			if (elt instanceof Fanout) {
				Stroke old_stroke = g2d.getStroke();
				Color old_color = g2d.getColor();
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND));
				g2d.setColor(Color.BLACK);
				int x0 = (elt.getX() + dx) * m;
				int y0 = (elt.getY() + dy) * m;
				int m_quater = Math.round(((float) m) / 4);
				int m_half = Math.round(((float) m) / 2);
				for (int i : new int[] { -1, 1 }) {
					for (int j : new int[] { -1, 1 }) {
						g2d.drawLine(x0 + m_half * i, y0 + m_half * j, x0
								+ m_quater * i, y0 + m_half * j);
						g2d.drawLine(x0 + m_half * i, y0 + m_half * j, x0
								+ m_half * i, y0 + m_quater * j);
					}
				}
				g2d.setColor(old_color);
				g2d.setStroke(old_stroke);
			} else {

				RadialGradientPaint radial_paint;
				LinearGradientPaint lp;
				Color[] c = { color, new Color(255, 255, 255, 0) };
				float[] dist = { 0f, 1f };

				if (elt.getHeight() <= 0 && elt.getWidth() <= 0) {
					Point p = elt.getPosition();
					int x = (p.getX() + dx) * m;
					int y = (p.getY() + dy) * m;
					radial_paint = new RadialGradientPaint((float) x,
							(float) y, rad, dist, c);
					g2d.setPaint(radial_paint);
					g2d.fillOval(Math.round(x - rad), Math.round(y - rad),
							Math.round(2 * rad), Math.round(2 * rad));
				} else {
					ArrayList<Point> points = new ArrayList<Point>();
					points.add(new Point(elt.getX() + elt.getWidth(), elt
							.getY()));
					points.add(new Point(elt.getX() + elt.getWidth(), elt
							.getY() + elt.getHeight()));
					points.add(new Point(elt.getX(), elt.getY()
							+ elt.getHeight()));
					points.add(new Point(elt.getX(), elt.getY()));

					Point p1 = new Point(elt.getX(), elt.getY());
					Point p2;
					for (Point point : points) {
						p2 = point;
						int x1 = (p1.getX() + dx) * m;
						int y1 = (p1.getY() + dy) * m;
						int x2 = (p2.getX() + dx) * m;
						int y2 = (p2.getY() + dy) * m;
						int x = x2 - x1;
						int y = y2 - y1;
						float mod = (float) Math.sqrt(x * x + y * y);
						float nx = 2 * ((float) x) / mod * rad;
						float ny = 2 * ((float) y) / mod * rad;
						lp = new LinearGradientPaint((float) x1, (float) y1,
								((float) x1) - ny, ((float) y1) + nx, dist, c,
								MultipleGradientPaint.CycleMethod.NO_CYCLE);
						g2d.setPaint(lp);
						int[] xpoints = {
								Math.round(x1 - ny * inner_glow_coef),
								Math.round(x2 - ny * inner_glow_coef), x2, x1 };
						int[] ypoints = {
								Math.round(y1 + nx * inner_glow_coef),
								Math.round(y2 + nx * inner_glow_coef), y2, y1 };
						Polygon poly = new Polygon(xpoints, ypoints, 4);
						g2d.fill(poly);
						p1 = p2;
					}
				}
			}
		} else {
			System.out.println("Cannot paint null Element!");
		}
	}
}

interface ElementRenderer {

	public void paint(InputGate input, Graphics2D g2d);

	public void paint(OutputGate output, Graphics2D g2d);

	public void paint(EnvironmentGate env, Graphics2D g2d);

	public void paint(NotGate not, Graphics2D g2d);

	public void paint(AndGate and, Graphics2D g2d);

	public void paint(OrGate or, Graphics2D g2d);

	public void paint(NandGate nand, Graphics2D g2d);

	public void paint(NorGate nor, Graphics2D g2d);

	public void paint(XorGate xor, Graphics2D g2d);

	public void paint(CGate c, Graphics2D g2d);

	public void paint(Fanout f, Graphics2D g2d);
}

abstract class BufferedPanel extends JPanel {
	private BufferedImage buf = null;
	private boolean isDirty = true;

	// Image img =
	// Toolkit.getDefaultToolkit().getImage(Constant.IMAGE_PATH+"and.png");
	protected void needToChange() {
		if (buf != null) {
			Graphics2D gc = buf.createGraphics();
			gc.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			gc.fillRect(0, 0, buf.getWidth(), buf.getHeight());
			gc.dispose();
			buf = null;
		}
		isDirty = true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (isDirty) {
			if (buf == null) {
				buf = new BufferedImage(this.getWidth(), this.getHeight(),
						java.awt.image.BufferedImage.TYPE_INT_ARGB);
			}
			Graphics2D gc = buf.createGraphics();
			customizedPaint(gc);
			gc.dispose();
			isDirty = false;
		}
		g2d.drawImage(buf, 0, 0, new Color(255, 255, 255, 0), null);
	}

	abstract void customizedPaint(Graphics2D g2d);
}
