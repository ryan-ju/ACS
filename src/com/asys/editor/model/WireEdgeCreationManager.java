/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;

import com.asys.constants.CommandName;

/**
 * @author ryan
 * 
 */
public class WireEdgeCreationManager {
	private static WireEdgeCreationManager wecm;
	private ArrayList<WireEdgeCreationManagerListener> listeners;
	private Wire wire;
	private int index, x1, y1, x2, y2, wx1, wy1, wx2, wy2;
	private boolean isCreating = false, isCancelled, vertical;

	public static WireEdgeCreationManager getInstance() {
		if (wecm == null) {
			wecm = new WireEdgeCreationManager();
		}
		return wecm;
	}

	private WireEdgeCreationManager() {
		listeners = new ArrayList<WireEdgeCreationManagerListener>();
	}

	/**
	 * 
	 * Start creating wire edge by creating the first routing point (x, y) on
	 * the 'index'th edge of 'wire'
	 * 
	 * @param wire
	 * @param index
	 * @param x
	 * @param y
	 */
	public void start(Wire wire, int index, int x1, int y1) {
		WireEdge edge = wire.getRoutingEdges().get(index);
		wx1 = Math.min(edge.getP1().getX(), edge.getP2().getX());
		wx2 = Math.max(edge.getP1().getX(), edge.getP2().getX());
		wy1 = Math.min(edge.getP1().getY(), edge.getP2().getY());
		wy2 = Math.max(edge.getP1().getY(), edge.getP2().getY());
		if (edge.isVertical()) {
			if (wy1 < y1 && y1 < wy2) {
				startSub(wire, index, true, x1, y1);
			}
		} else {
			assert wy1 == wy2;
			if (wx1 < x1 && x1 < wx2) {
				startSub(wire, index, false, x1, y1);
			}
		}
	}

	/**
	 * 
	 * @param edge
	 * @param x
	 * @param y
	 */
	public void start(WireEdge edge, int x1, int y1) {
		wx1 = Math.min(edge.getP1().getX(), edge.getP2().getX());
		wx2 = Math.max(edge.getP1().getX(), edge.getP2().getX());
		wy1 = Math.min(edge.getP1().getY(), edge.getP2().getY());
		wy2 = Math.max(edge.getP1().getY(), edge.getP2().getY());
		if (edge.isVertical()) {
			if (wy1 < y1 && y1 < wy2) {
				startSub(edge.getParent(), edge.getIndex(), true, x1, y1);
			}
		} else {
			assert wy1 == wy2;
			if (wx1 < x1 && x1 < wx2) {
				startSub(edge.getParent(), edge.getIndex(), false, x1, y1);
			}
		}
	}

	private void startSub(Wire wire, int index, boolean vertical, int x1, int y1) {
		this.wire = wire;
		this.index = index;
		this.vertical = vertical;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x1;
		this.y2 = y1;
		isCreating = true;
		isCancelled = false;
		fireStarted();
	}

	public int getX1() {
		return x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		if (vertical) {
			// Do nothing
		} else {
			if (wx1 < x2 && x2 < wx2) {
				this.x2 = x2;
				fireCoordinatesChanged();
			}
		}
	}

	public int getY1() {
		return y1;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		if (vertical) {
			if (wy1 < y2 && y2 < wy2) {
				this.y2 = y2;
				fireCoordinatesChanged();
			}
		} else {
			// Do nothing
		}
	}

	public Wire getWire() {
		return wire;
	}

	public int getIndex() {
		return index;
	}

	public void cancel() {
		this.x1 = -1;
		this.y1 = -1;
		this.y2 = -1;
		this.x2 = -1;
		this.vertical = false;
		this.isCreating = false;
		this.isCancelled = true;
		fireCancelled();
	}

	public void finish() {
		Command cmd = Command.getInstance();
		cmd.setCommandName(CommandName.CREATE_WIRE_EDGE);
		cmd.setParams(new Object[] { wire, index, x1, y1, x2, y2 });
		Executor.getInstance().execute(cmd);
		this.x1 = -1;
		this.y1 = -1;
		this.y2 = -1;
		this.x2 = -1;
		this.vertical = false;
		this.isCreating = false;
		this.isCancelled = false;
		fireFinished();
	}

	public boolean isCreating() {
		return isCreating;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void addListener(WireEdgeCreationManagerListener lst) {
		if (!listeners.contains(lst)) {
			listeners.add(lst);
		}
	}

	public void removeAllListeners() {
		listeners.clear();
	}

	private void fireStarted() {
		for (WireEdgeCreationManagerListener lst : listeners) {
			lst.started();
		}
	}

	private void fireCoordinatesChanged() {
		for (WireEdgeCreationManagerListener lst : listeners) {
			lst.coordinatesChanged();
		}
	}

	private void fireCancelled() {
		for (WireEdgeCreationManagerListener lst : listeners) {
			lst.cancelled();
		}
	}

	private void fireFinished() {
		for (WireEdgeCreationManagerListener lst : listeners) {
			lst.finished();
		}
	}

}
