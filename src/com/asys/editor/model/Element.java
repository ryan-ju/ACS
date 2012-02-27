/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.List;

import com.asys.constants.Constant;
import com.asys.constants.Direction;
import com.asys.constants.LogicValue;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.NoWireException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;

/**
 * @author ryan
 * 
 */
public abstract class Element {
	protected int x, y, w, h;
	private ArrayList<Inport> ips;
	private ArrayList<Outport> ops;
	private Property prop;
	private int maxIPs, maxOPs;
	private Direction ort; // Direction of outports

	protected Element() {
		// Do nothing.
	}

	protected Element(Property prop, int maxIPs, int maxOPs, int w) {
		ips = new ArrayList<Inport>();
		ops = new ArrayList<Outport>();
		this.ort = Direction.RIGHT;
		this.prop = prop;
		this.maxIPs = maxIPs;
		this.maxOPs = maxOPs;
		this.w = w;
		init();
	}

	private void init() {
		// Set initial position.
		x = 0;
		y = 0;
		for (int i = 0; i < getMaxIPs(); i++) {
			Inport ip = new Inport(this);
			ips.add(ip);
		}
		for (int i = 0; i < getMaxOPs(); i++) {
			Outport op = new Outport(this);
			ops.add(op);
		}
		updateDimension();
	}

	protected void setup(int x, int y, int w, int h, int maxIPs, int maxOPs,
			Direction ort, ArrayList<Inport> ips, ArrayList<Outport> ops,
			Property prop) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.ips = ips;
		this.ops = ops;
		this.maxIPs = maxIPs;
		this.maxOPs = maxOPs;
		this.ort = ort;
	}

	public int getX() {
		return x;
	}

	protected void setX(int x) {
		this.x = x;
		updatePorts();
	}

	public int getY() {
		return y;
	}

	protected void setY(int y) {
		this.y = y;
		updatePorts();
	}

	public Point getPosition() {
		return new Point(x, y);
	}

	protected void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		updatePorts();
	}

	public int getWidth() {
		return w;
	}

	private void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	private void setHeight(int h) {
		this.h = h;
	}

	public Direction getOrientation() {
		return ort;
	}

	protected void setOrientation(Direction ort) {
		if (Direction.isOrthogonal(this.ort, ort)) {
			int t = h;
			h = w;
			w = t;
		}
		this.ort = ort;
		updatePorts();
	}

	public int getMaxIPs() {
		return maxIPs;
	}

	public int getNumWiredIPs() {
		int i = 0;
		for (Inport ip : ips) {
			if (ip.getWire() != null) {
				i++;
			}
		}
		return i;
	}

	protected void setMaxIPs(int maxIPs)
			throws MaxNumberOfPortsOutOfBoundException {
		if (maxIPs > getMaxIPs()) { // Need to create more inports.
			for (int i = getMaxIPs(); i < maxIPs; i++) {
				Inport ip = new Inport(this);
				ips.add(ip);
			}
			this.maxIPs = maxIPs;
			updateDimension();
		}
		// Delete some unwired inports.
		if (maxIPs < getMaxIPs() && maxIPs >= getNumWiredIPs()) {
			int i = 0, target = getMaxIPs() - maxIPs;
			while (i < target) {
				int j = getIndexOfNextUnwiredInport();
				ips.remove(j);
				i++;
			}
			this.maxIPs = maxIPs;
			updateDimension();
		}
		if (maxIPs < getNumWiredIPs()) {
			throw new MaxNumberOfPortsOutOfBoundException(getNumWiredIPs());
		}
		assert getMaxIPs() == ips.size();
	}

	private int getIndexOfNextUnwiredInport() {
		for (int i = 0; i < ips.size(); i++) {
			if (ips.get(i).getWire() == null) {
				return i;
			}
		}
		return -1;
	}

	public int getMaxOPs() {
		return maxOPs;
	}

	public int getNumWiredOPs() {
		int i = 0;
		for (Outport op : ops) {
			if (op.getWire() != null) {
				i++;
			}
		}
		return i;
	}

	protected void setMaxOPs(int maxOPs)
			throws MaxNumberOfPortsOutOfBoundException {
		if (maxOPs > getMaxOPs()) { // Need to create more outports.
			for (int i = getMaxOPs(); i < maxOPs; i++) {
				Outport op = new Outport(this);
				ops.add(op);
			}
			this.maxOPs = maxOPs;
			updateDimension();
		}
		// Delete some unwired outports.
		if (maxOPs < getMaxOPs() && maxOPs >= getNumWiredOPs()) {
			int i = 0, target = getMaxOPs() - maxOPs;
			while (i < target) {
				int j = getIndexOfNextUnwiredOutport();
				ops.remove(j);
				i++;
			}
			this.maxOPs = maxOPs;
			updateDimension();
		}
		if (maxOPs < getNumWiredOPs()) {
			throw new MaxNumberOfPortsOutOfBoundException(getNumWiredOPs());
		}
		assert getMaxOPs() == ops.size();
	}

	private int getIndexOfNextUnwiredOutport() {
		for (int i = 0; i < ops.size(); i++) {
			if (ops.get(i).getWire() == null) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Inports are placed symmetrically on one side of an element. Since the
	 * side of the element always has an even length, if there is an odd number
	 * of inports, all available points are occupied; if there is an even number
	 * of inports, then the middle positive is not occupied.
	 * 
	 * @param ip
	 */
	public void setPositionOfInport(Inport ip) {
		int i = ips.indexOf(ip);
		int m = getMaxIPs();
		int t;
		int gap;
		if (i >= 0) {
			boolean firstHalf = 2 * i < m;
			switch (ort) {
			case UP:
				t = w - 1;
				gap = t - m;
				if (firstHalf) {
					ip.setPosition(new Point(x + 1 + i, y + h));
				} else {
					ip.setPosition(new Point(x + 1 + gap + i, y + h));
				}
				break;
			case DOWN:
				t = w - 1;
				gap = t - m;
				if (firstHalf) {
					ip.setPosition(new Point(x + 1 + i, y));
				} else {
					ip.setPosition(new Point(x + 1 + gap + i, y));
				}
				break;
			case LEFT:
				t = h - 1;
				gap = t - m;
				if (firstHalf) {
					ip.setPosition(new Point(x + w, y + 1 + i));
				} else {
					ip.setPosition(new Point(x + w, y + 1 + gap + i));
				}
				break;
			case RIGHT:
				t = h - 1;
				gap = t - m;
				if (firstHalf) {
					ip.setPosition(new Point(x, y + 1 + i));
				} else {
					ip.setPosition(new Point(x, y + 1 + gap + i));
				}
				break;
			default:
				assert false;
			}
		}
	}

	public void setPositionOfOutport(Outport op) {
		int i = ops.indexOf(op);
		if (i >= 0) {
			switch (ort) {
			case UP:
				op.setPosition(new Point(x + w / 2, y));
				break;
			case DOWN:
				op.setPosition(new Point(x + w / 2, y + h));
				break;
			case LEFT:
				op.setPosition(new Point(x, y + h / 2));
				break;
			case RIGHT:
				op.setPosition(new Point(x + w, y + h / 2));
				break;
			default:
				assert false;
			}
		}
	}

	protected void updatePorts() {
		for (Inport ip : ips) {
			setPositionOfInport(ip);
		}
		for (Outport op : ops) {
			setPositionOfOutport(op);
		}
	}

	protected void updateDimension() {
		int max, new_w, new_h;
		switch (ort) {
		case UP:
		case DOWN:
			max = Math.max(maxIPs, maxOPs);
			if (max % 2 == 0) {
				new_w = max + 2;
			} else {
				new_w = max + 1;
			}
			setWidth(Math.max(new_w, Constant.MIN_GATE_HEIGHT)); // This is
																	// RIGHT!
																	// Should be
																	// MIN_GATE_HEIGHT,
																	// not
																	// WIDTH!
			break;
		case LEFT:
		case RIGHT:
			max = Math.max(maxIPs, maxOPs);
			if (max % 2 == 0) {
				new_h = max + 2;
			} else {
				new_h = max + 1;
			}
			setHeight(Math.max(new_h, Constant.MIN_GATE_HEIGHT)); // Set height
																	// so all
																	// ports can
			break;
		}
		updatePorts();
	}

	public Property getProperty() {
		return prop;
	}

	protected int getIndexOfInport(Inport p) {
		return ips.indexOf(p);
	}

	protected int getIndexOfOutport(Outport p) {
		return ops.indexOf(p);
	}

	public Inport getInport(int i) throws PortNumberOutOfBoundException {
		if (i >= maxIPs) {
			throw new PortNumberOutOfBoundException(maxIPs);
		}
		return ips.get(i);
	}

	public Outport getOutport(int i) throws PortNumberOutOfBoundException {
		if (i >= maxOPs) {
			throw new PortNumberOutOfBoundException(maxOPs);
		}
		return ops.get(i);
	}

	protected List<Inport> getInports() {
		return ips;
	}

	protected void setInports(ArrayList<Inport> ips) {
		this.ips = ips;
	}

	protected List<Outport> getOutports() {
		return ops;
	}

	protected void setOutports(ArrayList<Outport> ops) {
		this.ops = ops;
	}

	List<LogicValue> getInputs() {
		ArrayList<LogicValue> inputs = new ArrayList<LogicValue>();
		for (Inport ip : ips) {
			try {
				inputs.add(ip.getValue());
			} catch (NoWireException e) {
				continue;
			}
		}
		return inputs;
	}

	/**
	 * This method copyies all the states of 'elt' to 'elt_cp'. Important: 'elt'
	 * and 'elt_cp' must be of the same type.
	 * 
	 * This method is only used by the 'copy' method of Element's subclasses.
	 * 
	 * Note that any port copied has no wires connected to it, since the
	 * condition
	 * "Any wire can only be connected to at most one Inport and at most one Outport"
	 * must be maintained.  It is up to the user to add wires if desired.
	 * 
	 * @param elt
	 * @param elt_cp
	 */
	protected static void copy(Element elt, Element elt_cp) {
		ArrayList<Inport> ips_cp = new ArrayList<Inport>();
		ArrayList<Outport> ops_cp = new ArrayList<Outport>();
		Property prop_cp;
		for (Inport ip : elt.ips) {
			ips_cp.add(ips_cp.size(), new Inport(elt_cp));
		}
		for (Outport op : elt.ops) {
			ops_cp.add(ops_cp.size(), new Outport(elt_cp));
		}
		prop_cp = elt.prop.copy();
		elt_cp.setup(elt.x, elt.y, elt.w, elt.h, elt.maxIPs, elt.maxOPs,
				elt.ort, ips_cp, ops_cp, prop_cp);
	}

	abstract public Element copy();

	abstract public LogicValue evaluate();

	abstract public void accept(ElementVisitor ev);
}
