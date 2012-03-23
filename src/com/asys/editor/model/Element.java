/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;

import com.asys.constants.Constant;
import com.asys.constants.Direction;
import com.asys.constants.ElementPropertyKey;
import com.asys.constants.LogicValue;
import com.asys.editor.model.Port.PortState;
import com.asys.model.components.exceptions.InvalidPropertyException;
import com.asys.model.components.exceptions.InvalidPropertyKeyException;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.NoKeyException;
import com.asys.model.components.exceptions.NoWireException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;

/**
 * @author ryan
 * 
 */
public abstract class Element {
	protected int x, y, w, h, old_x, old_y, old_w, old_h;
	private ArrayList<Inport> ips;
	private ArrayList<Outport> ops;
	private Property prop;
	private int numIPs, numOPs;
	private Direction ort; // Direction of outports
	private boolean canChangeNumIPs;
	private boolean canChangeNumOPs;

	protected Element() {
		canChangeNumIPs = true;
		canChangeNumOPs = true;
	}

	protected Element(Property prop, int numIPs, int numOPs, int w,
			boolean canChangeMaxIPs, boolean canChangeMaxOPs) {
		ips = new ArrayList<Inport>();
		ops = new ArrayList<Outport>();
		this.ort = Direction.RIGHT;
		this.prop = prop;
		try {
			prop.setProperty(ElementPropertyKey.NUM_INPORT, numIPs);
			prop.setProperty(ElementPropertyKey.NUM_OUTPORT, numOPs);		
		} catch (InvalidPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.numIPs = numIPs;
		this.numOPs = numOPs;
		this.w = w;
		this.old_w = w;
		this.canChangeNumIPs = canChangeMaxIPs;
		this.canChangeNumOPs = canChangeMaxOPs;
		init();
	}

	private void init() {
		// Set initial position.
		x = 0;
		y = 0;
		for (int i = 0; i < getNumberOfIPs(); i++) {
			Inport ip = new Inport(this);
			ips.add(ip);
		}
		for (int i = 0; i < getNumberOfOPs(); i++) {
			Outport op = new Outport(this);
			ops.add(op);
		}
		updateDimension();
	}

	protected void setup(int x, int y, int w, int h, int numIPs, int numOPs,
			Direction ort, ArrayList<Inport> ips, ArrayList<Outport> ops,
			Property prop, boolean canChangeMaxIPs, boolean canChangeMaxOPs) {
		assert ips.size() <= numIPs && ops.size() <= numOPs;
		try {
			assert (Integer) prop.getProperty(ElementPropertyKey.NUM_INPORT) == numIPs && (Integer) prop.getProperty(ElementPropertyKey.NUM_OUTPORT) == numOPs;
		} catch (InvalidPropertyKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.old_x = this.x;
		this.x = x;
		this.old_y = this.y;
		this.y = y;
		this.old_w = this.w;
		this.w = w;
		this.old_h = this.h;
		this.h = h;
		this.ips = ips;
		this.ops = ops;
		this.numIPs = numIPs;
		this.numOPs = numOPs;
		this.prop = prop;
		this.ort = ort;
		this.canChangeNumIPs = canChangeMaxIPs;
		this.canChangeNumOPs = canChangeMaxOPs;
	}

	public int getX() {
		return x;
	}

	public int getOldX() {
		return old_x;
	}

	protected void setX(int x) {
		this.old_x = this.x;
		this.x = x;
		updatePositionOfPorts();
	}

	public int getY() {
		return y;
	}

	public int getOldY() {
		return old_y;
	}

	protected void setY(int y) {
		this.old_y = this.y;
		this.y = y;
		updatePositionOfPorts();
	}

	public Point getPosition() {
		return new Point(x, y);
	}

	public Point getOldPosition() {
		return new Point(old_x, old_y);
	}

	protected void setPosition(int x, int y) {
		this.old_x = this.x;
		this.old_y = this.y;
		this.x = x;
		this.y = y;
		updatePositionOfPorts();
	}

	public int getWidth() {
		return w;
	}

	public int getIntrinsicWidth() {
		switch (ort) {
		case UP:
		case DOWN:
			return getHeight();
		case LEFT:
		case RIGHT:
			return getWidth();
		}
		return -1;
	}

	public int getOldWidth() {
		return old_w;
	}

	private void setWidth(int w) {
		this.old_w = this.w;
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public int getIntrinsicHeight() {
		switch (ort) {
		case UP:
		case DOWN:
			return getWidth();
		case LEFT:
		case RIGHT:
			return getHeight();
		}
		return -1;
	}

	public int getOldHeight() {
		return old_h;
	}

	private void setHeight(int h) {
		this.old_h = this.h;
		this.h = h;
	}

	public Direction getOrientation() {
		return ort;
	}

	protected void setOrientation(Direction ort) {
		if (Direction.isOrthogonal(this.ort, ort)) {
			int t = h;
			setHeight(w);
			setWidth(t);
		}
		this.ort = ort;
		updatePositionOfPorts();
	}

	public boolean canChangeNumIPs() {
		return canChangeNumIPs;
	}

	public int getNumberOfIPs() {
		return numIPs;
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

	protected void setNumberOfIPs(int numIPs)
			throws MaxNumberOfPortsOutOfBoundException {
		if (canChangeNumIPs) {
			if (numIPs > getNumberOfIPs()) { // Need to create more inports.
				for (int i = getNumberOfIPs(); i < numIPs; i++) {
					Inport ip = new Inport(this);
					ips.add(ip);
				}
				subSetNumberOfIPs(numIPs);
				updateDimension();
			}
			// Delete some unwired inports.
			if (numIPs < getNumberOfIPs() && numIPs >= getNumWiredIPs()) {
				int i = 0, target = getNumberOfIPs() - numIPs;
				while (i < target) {
					int j = getIndexOfNextUnwiredInport();
					ips.remove(j);
					i++;
				}
				subSetNumberOfIPs(numIPs);
				updateDimension();
			}
			if (numIPs < getNumWiredIPs()) {
				throw new MaxNumberOfPortsOutOfBoundException(getNumWiredIPs());
			}
			assert getNumberOfIPs() == ips.size();
		}
	}

	private int getIndexOfNextUnwiredInport() {
		for (int i = 0; i < ips.size(); i++) {
			if (ips.get(i).getWire() == null) {
				return i;
			}
		}
		return -1;
	}

	public boolean canChangeNumOPs() {
		return canChangeNumOPs;
	}

	public int getNumberOfOPs() {
		return numOPs;
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

	protected void setNumberOfOPs(int numOPs)
			throws MaxNumberOfPortsOutOfBoundException {
		if (canChangeNumOPs) {
			if (numOPs > getNumberOfOPs()) { // Need to create more outports.
				for (int i = getNumberOfOPs(); i < numOPs; i++) {
					Outport op = new Outport(this);
					ops.add(op);
				}
				subSetNumberOfOPs(numOPs);
				updateDimension();
			}
			// Delete some unwired outports.
			if (numOPs < getNumberOfOPs() && numOPs >= getNumWiredOPs()) {
				int i = 0, target = getNumberOfOPs() - numOPs;
				while (i < target) {
					int j = getIndexOfNextUnwiredOutport();
					ops.remove(j);
					i++;
				}
				subSetNumberOfOPs(numOPs);
				updateDimension();
			}
			if (numOPs < getNumWiredOPs()) {
				throw new MaxNumberOfPortsOutOfBoundException(getNumWiredOPs());
			}
			assert getNumberOfOPs() == ops.size();
		}
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
		int m = getNumberOfIPs();
		int t;
		int gap;
		if (i >= 0) {
			boolean firstHalf = 2 * i < m;
			switch (ort) {
			case UP:
				if (m == 1) {
					ip.setPosition(new Point(x + w / 2, y + h));
				} else {
					t = w - 1;
					gap = t - m;
					if (firstHalf) {
						ip.setPosition(new Point(x + 1 + i, y + h));
					} else {
						ip.setPosition(new Point(x + 1 + gap + i, y + h));
					}
				}
				break;
			case DOWN:
				if (m == 1) {
					ip.setPosition(new Point(x + w / 2, y));
				} else {
					t = w - 1;
					gap = t - m;
					if (firstHalf) {
						ip.setPosition(new Point(x + 1 + i, y));
					} else {
						ip.setPosition(new Point(x + 1 + gap + i, y));
					}
				}
				break;
			case LEFT:
				if (m == 1) {
					ip.setPosition(new Point(x + w, y + h / 2));
				} else {
					t = h - 1;
					gap = t - m;
					if (firstHalf) {
						ip.setPosition(new Point(x + w, y + 1 + i));
					} else {
						ip.setPosition(new Point(x + w, y + 1 + gap + i));
					}
				}
				break;
			case RIGHT:
				if (m == 1) {
					ip.setPosition(new Point(x, y + h / 2));
				} else {
					t = h - 1;
					gap = t - m;
					if (firstHalf) {
						ip.setPosition(new Point(x, y + 1 + i));
					} else {
						ip.setPosition(new Point(x, y + 1 + gap + i));
					}
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
			// Wire wire = op.getWire();
			// if (wire != null) {
			// wire.adjustForOutport();
			// }
		}
	}

	protected void updatePositionOfPorts() {
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
			max = Math.max(numIPs, numOPs);
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
			max = Math.max(numIPs, numOPs);
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
		updatePositionOfPorts();
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
		if (i >= numIPs) {
			throw new PortNumberOutOfBoundException(numIPs);
		}
		return ips.get(i);
	}

	public Outport getOutport(int i) throws PortNumberOutOfBoundException {
		if (i >= numOPs) {
			throw new PortNumberOutOfBoundException(numOPs);
		}
		return ops.get(i);
	}

	public ArrayList<Inport> getInports() {
		return ips;
	}

	protected void setInports(ArrayList<Inport> ips) {
		this.ips = ips;
	}

	public ArrayList<Outport> getOutports() {
		return ops;
	}

	protected void setOutports(ArrayList<Outport> ops) {
		this.ops = ops;
	}

	ArrayList<LogicValue> getInputs() {
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

	public ElementPortState exportPortState() {
		return new ElementPortState();
	}

	private void importPortState(ElementPortState state) {
		this.setWidth(state.getWidth());
		this.setHeight(state.getHeight());
		this.prop = state.getProperty();
		this.setInports(state.getInports());
		this.setOutports(state.getOutports());
		this.numIPs = state.getNumIPs();
		this.numOPs = state.getNumOPs();
		for (PortState s:state.portStates){
			s.restore();
		}
	}
	
	private void subSetNumberOfIPs(int num){
		this.numIPs = num;
		try {
			this.prop.setProperty(ElementPropertyKey.NUM_INPORT, num);
		} catch (InvalidPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void subSetNumberOfOPs(int num){
		this.numOPs = num;
		try {
			this.prop.setProperty(ElementPropertyKey.NUM_OUTPORT, num);
		} catch (InvalidPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method copies all the states of 'elt' to 'elt_cp'. Important: 'elt'
	 * and 'elt_cp' must be of the same type.
	 * 
	 * This method is only used by the 'copy' method of Element's subclasses.
	 * 
	 * Note that any port copied has no wires connected to it, since the
	 * condition
	 * "Any wire can only be connected to at most one Inport and at most one Outport"
	 * must be maintained. It is up to the user to add wires if desired.
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
		elt_cp.setup(elt.x, elt.y, elt.w, elt.h, elt.numIPs, elt.numOPs,
				elt.ort, ips_cp, ops_cp, prop_cp, elt.canChangeNumIPs,
				elt.canChangeNumOPs);
	}

	/**
	 * This methods is used to calculate the expected bound of 'elt' if the
	 * the number of inports/outports is set to 'num'.
	 * 
	 * The new dimension is calculated by the following rule:
	 * 
	 * 1. Only one dimension will be updated.
	 * 
	 * 2. The new dimension is the maximum calculated from the number of inports
	 * and the number of outports, and the predefined minimum.
	 * 
	 * 3. The dimension will always be even. Both ends will have one excessive
	 * unit, and the middle position will be occupied by a port if the number of
	 * ports is odd, empty if even.
	 * 
	 * @param elt
	 * @param num
	 * @param isInport
	 * @return
	 */
	protected static Rectangle getNewBound(Element elt, int num,
			boolean isInport) {
		if (elt != null) {
			int max, new_w, new_h;
			if (isInport) {
				switch (elt.getOrientation()) {
				case UP:
				case DOWN:
					if (isInport) {
						max = Math.max(num, elt.getNumberOfOPs());
					} else {
						max = Math.max(elt.getNumberOfIPs(), num);
					}
					if (max % 2 == 0) {
						new_w = max + 2;
					} else {
						new_w = max + 1;
					}

					return new Rectangle(elt.getX(), elt.getY(), Math.max(
							new_w, Constant.MIN_GATE_HEIGHT), elt.getHeight());
				case LEFT:
				case RIGHT:
					if (isInport) {
						max = Math.max(num, elt.getNumberOfOPs());
					} else {
						max = Math.max(elt.getNumberOfIPs(), num);
					}
					if (max % 2 == 0) {
						new_h = max + 2;
					} else {
						new_h = max + 1;
					}
					return new Rectangle(elt.getX(), elt.getY(),
							elt.getWidth(), Math.max(new_h,
									Constant.MIN_GATE_HEIGHT));
				}
			}
		}
		return null;
	}

	abstract public Element copy();

	/**
	 * This method evaluates the new output.
	 */
	abstract public void evaluate();

	/**
	 * This method returns the current output. Output is updated only when
	 * evaluate() is called.
	 * 
	 * @return
	 */
	abstract public LogicValue getOutput();

	abstract public void accept(ElementVisitor ev);

	class ElementPortState {
		private int w, h;
		private int maxIPs, maxOPs;
		private Property prop;
		private ArrayList<Inport> ips;
		private ArrayList<Outport> ops;
		private ArrayList<PortState> portStates;

		ElementPortState() {
			this.maxIPs = Element.this.numIPs;
			this.maxOPs = Element.this.numOPs;
			this.w = Element.this.w;
			this.h = Element.this.h;
			this.prop = Element.this.prop.copy();
			ips = new ArrayList<Inport>();
			ops = new ArrayList<Outport>();
			portStates = new ArrayList<PortState>();
			ips.addAll(Element.this.ips);
			ops.addAll(Element.this.ops);
			for (Port port : ips) {
				portStates.add(port.exportState());
			}
			for (Port port : ops) {
				portStates.add(port.exportState());
			}
		}

		public int getWidth() {
			return w;
		}

		public int getHeight() {
			return h;
		}
		
		public Property getProperty(){
			return prop;
		}

		public int getNumIPs() {
			return maxIPs;
		}

		public int getNumOPs() {
			return maxOPs;
		}

		public ArrayList<Inport> getInports() {
			return ips;
		}

		public ArrayList<Outport> getOutports() {
			return ops;
		}

		public void restore() {
			Element.this.importPortState(this);
		}
	}
}
