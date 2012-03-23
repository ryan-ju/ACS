/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.LinkedList;

import com.asys.constants.CommandName;
import com.asys.constants.Direction;

/**
 * @author ryan
 * 
 */
public class WireCreationManager {
	private static WireCreationManager wcm;
	private CircuitManager cm;
	private LinkedList<RoutingPoint> rps;
	private ArrayList<WireCreationManagerListener> listeners;
	private int x, y;
	private Inport ip, ip_temp;
	private Outport op;
	private boolean vertical;
	private boolean creating;

	public static WireCreationManager getInstance() {
		if (wcm == null) {
			wcm = new WireCreationManager();
		}
		return wcm;
	}

	private WireCreationManager() {
		this.listeners = new ArrayList<WireCreationManagerListener>();
		this.cm = CircuitManager.getInstance();
		this.rps = new LinkedList<RoutingPoint>();
		creating = false;
	}

	public void addRoutingPoint() {
		if (op == null) {
			System.out.println("Cannot create wire.  No Outport selected!");
			return;
		}

		RoutingPoint rp_last;
		if (rps.isEmpty()) {
			rp_last = op.getPosition();
		} else {
			rp_last = rps.getLast();
		}
		if (rp_last.getX() == x && rp_last.getY() == y) {
			System.out
					.println("New RoutingPoint's position overlaps with the last point!");
			return;
		} else {
			if (ip_temp == null) {
				rps.add(new RoutingPoint(x, y));
				vertical = !vertical;
				fireAddedPoint();
			} else { // An Inport has been selected
				setInport();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public LinkedList<RoutingPoint> getRoutingPoints() {
		return (LinkedList<RoutingPoint>) rps.clone();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		if (vertical) {
			// Do nothing
		} else {
			this.x = x;
			Inport ip_t = cm.getElementManager().getInportAt(this.x, this.y);
			if (ip_t != null && ip_t.getWire() == null) {
				ip_temp = ip_t;
			} else {
				ip_temp = null;
			}
			fireCoordinatesChanged();
		}
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		if (vertical) {
			this.y = y;
			Inport ip_t = cm.getElementManager().getInportAt(this.x, this.y);
			if (ip_t != null && ip_t.getWire() == null) {
				ip_temp = ip_t;
			} else {
				ip_temp = null;
			}
			fireCoordinatesChanged();
		} else {
			// Do nothing
		}
	}

	public void removeLastRoutingPoint() {
		if (rps.isEmpty()) {
			clear();
		} else {
			RoutingPoint rp_rm = rps.removeLast();
			vertical = !vertical;
			if (vertical) {
				x = rp_rm.getX();
			} else {
				y = rp_rm.getY();
			}
			fireRemovedLastPoint();
		}
	}

	public Outport getOutport() {
		return op;
	}

	public void setOutport(Outport op) {
		this.op = op;
		this.x = op.getPosition().getX();
		this.y = op.getPosition().getY();
		Direction ort = op.getParent().getOrientation();
		switch (ort) {
		case UP:
		case DOWN:
			vertical = true;
			break;
		case LEFT:
		case RIGHT:
			vertical = false;
			break;
		}
		creating = true;
		fireStartedCreation();
	}

	public Inport getInport() {
		return ip;
	}

	public void setInport() {
		this.ip = ip_temp;
		creating = false;
		Command cmd = Command.getInstance();
		cmd.setCommandName(CommandName.CREATE_WIRE);
		cmd.setParams(new Object[] { getOutport(),
				getInport(), getRoutingPoints() });
		Executor.getInstance().execute(cmd);
		clear();
		fireFinishedCreation();
	}

	public Inport getIPTemp() {
		return ip_temp;
	}

	public void clear() {
		op = null;
		ip = null;
		ip_temp = null;
		rps.clear();
		creating = false;
		fireCleared();
	}

	public void cancel() {
		op = null;
		ip = null;
		ip_temp = null;
		rps.clear();
		creating = false;
		fireCancelled();
	}

	/**
	 * 
	 * @return true - if the wire is still being created
	 */
	public boolean isCreating() {
		return creating;
	}

	public boolean isCancelled() {
		return !creating && ip == null;
	}

	public void addListener(WireCreationManagerListener lst) {
		if (!listeners.contains(lst)) {
			listeners.add(lst);
		}
	}

	private void fireFinishedCreation() {
		for (WireCreationManagerListener lst : listeners) {
			lst.finishedCreation();
		}
	}

	private void fireCleared() {
		for (WireCreationManagerListener lst : listeners) {
			lst.cleared();
		}
	}

	private void fireAddedPoint() {
		for (WireCreationManagerListener lst : listeners) {
			lst.addedPoint();
		}
	}

	private void fireRemovedLastPoint() {
		for (WireCreationManagerListener lst : listeners) {
			lst.removedLastPoint();
		}
	}

	private void fireStartedCreation() {
		for (WireCreationManagerListener lst : listeners) {
			lst.startedCreation();
		}
	}

	private void fireCoordinatesChanged() {
		for (WireCreationManagerListener lst : listeners) {
			lst.coordinatesChanged();
		}
	}

	private void fireCancelled() {
		for (WireCreationManagerListener lst : listeners) {
			lst.cancelled();
		}
	}

}
