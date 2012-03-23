/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.asys.constants.Constant;
import com.asys.constants.Direction;
import com.asys.editor.model.Element.ElementPortState;
import com.asys.editor.model.SelectionManager.SelectionManagerState;
import com.asys.editor.model.Wire.WireState;
import com.asys.model.components.exceptions.DuplicateElementException;
import com.asys.model.components.exceptions.ElementOverlappingException;
import com.asys.model.components.exceptions.InvalidRoutingPointException;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.OverlappingElementException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;
import com.asys.system.MainApplication;
import com.asys.views.Application;

/**
 * @author ryan
 * 
 */
public class Executor {
	private static Executor ext;
	private CircuitManager cm = CircuitManager.getInstance();
	private ElementManager eltm = cm.getElementManager();
	private WireManager wm = cm.getWireManager();
	private EdgeManager edgm = cm.getEdgeManager();
	private ElementCreationManager ecm = ElementCreationManager.getInstance();
	private SelectionManager sm = SelectionManager.getInstance();
	private MainApplication main = MainApplication.getInstance();
	private ActionQueue queue;

	public static Executor getInstance() {
		if (ext == null)
			ext = new Executor();
		return ext;
	}

	private Executor() {
		this.queue = new ActionQueue(Constant.MAX_ACTION_QUEUE_LENGTH);
	}

	public void execute(Command cmd) {
		final Object[] params = cmd.getParams();
		switch (cmd.getCommandName()) {
		case CREATE_GATE:
			createGate(params);
			break;
		case CREATE_FANOUT:
			createFanout(params);
			break;
		case CREATE_WIRE:
			createWire(params);
			break;
		case CREATE_WIRE_EDGE:
			createWireEdge(params);
			break;
		case DELETE:
			delete(params);
			break;
		case MOVE:
			move(params);
			break;
		case ROTATE:
			rotate(params);
			break;
		case CHANGE_NUMBER_OF_INPORT:
			changeNumberOfInport(params);
			break;
		case CHANGE_NUMBER_OF_OUTPORT:
			changeNumberOfOutport(params);
			break;
		case SELECT_WIRE:
			selectWire(params);
			break;
		case SELECT_WIRE_EDGE:
			selectWireEdge(params);
			break;
		case SELECT_ELEMENT:
			selectElement(params);
			break;
		case SELECT_MULTI_ELEMENT:
			selectMultiElement(params);
			break;
		case SELECT_GROUP_ELEMENT:
			selectGroupElement(params);
			break;
		case SELECT_GROUP_MULTI_ELEMENT:
			selectGroupMultiElement(params);
			break;
		case DESELECT:
			deselect();
			break;
		case UNDO:
			undo();
			break;
		case REDO:
			redo();
			break;
		case COPY:
			copy();
			break;
		case PASTE:
			paste(params);
			break;
		}
	}

	private void createGate(Object[] params) {
		int x = (Integer) params[0];
		int y = (Integer) params[1];
		final Element elt_created = ecm.getCreation();
		elt_created.setPosition(x, y);
		Action create_gate_action = new Action() {
			Element backup_elt;
			SelectionManagerState previous_state;

			@Override
			public boolean run() {
				previous_state = sm.exportState();
				try {
					backup_elt = elt_created;
					eltm.addElement(elt_created);
					cm.setElementManagerChanged();
					sm.deselect();
					cm.fireStateChangedEvent();
					return true;
				} catch (DuplicateElementException e) {
					Application.getInstance().note(
							"Duplicated elements detected!");
				} catch (OverlappingElementException e) {
					Application.getInstance().note("Cannot overlap elements!");
				}
				return false;
			}

			@Override
			public void undo() {
				boolean b = eltm.remove(backup_elt);
				cm.setElementManagerChanged();
				assert b;
				previous_state.restore();
				cm.fireStateChangedEvent();
			}
		};
		queue.enqueue(create_gate_action);
	}

	private void createFanout(Object[] params) {
		final Wire wire = (Wire) params[0];
		final int index = (Integer) params[1];
		final int x = (Integer) params[2];
		final int y = (Integer) params[3];
		final WireEdge edge = wire.getRoutingEdges().get(index);
		// Check if Fanout can be created.
		if (edge.isVertical()) {
			if (index == 0 && y == edge.getP1().getY())
				return; // If the creation position is an Outport, then the
						// Fanout cannot be created.
			if (index == wire.getRoutingEdges().size() - 1
					&& y == edge.getP2().getY())
				return; // If the creation position is an Inport
			if (WireEdge.isOnWireEdge(new Point(x, y), edge))
				;
			else
				return;
		} else { // The edge is horizontal
			if (index == 0 && x == edge.getP1().getX())
				return;// If the creation position is an Outport, then the
						// Fanout cannot be created.
			if (index == wire.getRoutingEdges().size() - 1
					&& x == edge.getP2().getX())
				return;
			if (WireEdge.isOnWireEdge(new Point(x, y), edge))
				;
			else
				return;
		}
		Action action = new Action() {
			Wire wire_deleted, new_wire_1, new_wire_2;
			SelectionManagerState sm_state;
			Fanout fanout;

			@Override
			public boolean run() {
				wire_deleted = wire;
				sm_state = sm.exportState();
				LinkedList<RoutingPoint> rps, rps1, rps2;
				rps = wire.getRoutingPoints();
				rps1 = new LinkedList<RoutingPoint>();
				rps2 = new LinkedList<RoutingPoint>();
				fanout = new Fanout();
				fanout.setPosition(x, y);
				try {
					eltm.addElement(fanout);
				} catch (DuplicateElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OverlappingElementException e) {
					Application.getInstance().note(
							"Fanout overlaps other gates!");
					return false;
				}
				for (int i = 0; i < index; i++) {
					rps1.addLast(new RoutingPoint(rps.get(i)));
				}
				for (int i = index; i < rps.size(); i++) {
					rps2.addLast(new RoutingPoint(rps.get(i)));
				}
				try {
					new_wire_1 = new Wire(wire_deleted.getOutport(),
							fanout.getInport(0), rps1);
				} catch (PortNumberOutOfBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					new_wire_2 = new Wire(fanout.getOutport(0),
							wire_deleted.getInport(), rps2);
				} catch (PortNumberOutOfBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				wm.remove(wire_deleted);
				wm.addWire(new_wire_1);
				wm.addWire(new_wire_2);
				sm.deselect();
				cm.fireStateChangedEvent();
				return true;
			}

			@Override
			public void undo() {
				wire_deleted.getOutport().setWire(wire_deleted);
				wire_deleted.getInport().setWire(wire_deleted);
				wm.remove(new_wire_1);
				wm.remove(new_wire_2);
				wm.addWire(wire_deleted);
				eltm.remove(fanout);
				sm_state.restore();
				sm.fireCircuitStateChanged();
			}

		};
		queue.enqueue(action);
	}

	private void createWire(Object[] params) {
		final Outport op = (Outport) params[0];
		final Inport ip = (Inport) params[1];
		final LinkedList<RoutingPoint> rps = (LinkedList<RoutingPoint>) params[2];
		Action create_wire_action = new Action() {
			private Wire wire;
			SelectionManagerState previous_state;

			@Override
			public boolean run() {
				previous_state = sm.exportState();
				if (op.getWire() == null && ip.getWire() == null) {
					try {
						Wire.validateWireEdges(op, ip, rps);
					} catch (InvalidRoutingPointException e) {
						return false;
					}
					wire = new Wire(op, ip, rps);
					wm.addWire(wire);
					// cm.setEdgeManagerChanged();
					sm.deselect();
					cm.fireStateChangedEvent();
					return true;
				}
				return false;
			}

			@Override
			public void undo() {
				op.setWire(null);
				ip.setWire(null);
				wm.remove(wire);
				cm.setEdgeManagerChanged();
				previous_state.restore();
				sm.fireCircuitStateChanged();
			}

		};
		queue.enqueue(create_wire_action);
	}

	private void createWireEdge(Object[] params) {
		final Wire wire = (Wire) params[0]; // This is the WireEdge on which new
											// edge will be created.
		final int index = (Integer) params[1];
		final int x1 = (Integer) params[2];
		final int y1 = (Integer) params[3];
		final int x2 = (Integer) params[4];
		final int y2 = (Integer) params[5];
		Action create_wire_edge_action = new Action() {
			SelectionManagerState previous_sm_state;
			WireState previous_wire_state;

			@Override
			public boolean run() {
				previous_wire_state = wire.exportState();
				previous_sm_state = sm.exportState();
				boolean successful = wire.addRoutingPoints(index, x1, y1, x2,
						y2);
				if (successful) {
					sm.deselect();
					sm.fireCircuitStateChanged();
				}
				sm.fireCircuitStateChanged();
				return successful;
			}

			@Override
			public void undo() {
				previous_wire_state.restore();
				previous_sm_state.restore();
				sm.fireCircuitStateChanged();
			}

		};
		queue.enqueue(create_wire_edge_action);
	}

	private void delete(Object[] params) {
		if (!sm.isEmpty()) {
			Action action = null;
			if (sm.getSelectedWire() != null) {
				action = new Action() {
					Wire wire = sm.getSelectedWire();
					SelectionManagerState sm_state;
					WireState wire_state;

					@Override
					public boolean run() {
						sm_state = sm.exportState();
						wire_state = wire.exportState();
						wire.getOutport().setWire(null);
						wire.getInport().setWire(null);
						wire.setInport(null);
						wire.setOutport(null);
						wm.remove(wire);
						sm.deselect();
						sm.fireCircuitStateChanged();
						return true;
					}

					@Override
					public void undo() {
						wire_state.restore();
						wm.addWire(wire);
						sm_state.restore();
						sm.fireCircuitStateChanged();
					}

				};
			} else if (!sm.getGroupManager().isEmpty()) {
				action = new Action() {
					SelectionManagerState sm_state;
					ArrayList<Wire> ind_wires, inc_wires;
					ArrayList<Element> elts;
					ArrayList<WireState> wire_states;

					@Override
					public boolean run() {
						sm_state = sm.exportState();
						ind_wires = (ArrayList<Wire>) sm.getGroupManager()
								.getInducedWires().clone();
						inc_wires = sm.getGroupManager()
								.getIncidentWireManager().getAllWires();
						elts = (ArrayList<Element>) sm.getGroupManager()
								.getElements().clone();
						wire_states = new ArrayList<WireState>();
						wm.remove(ind_wires);
						wm.remove(inc_wires);
						eltm.remove(elts);
						for (Wire wire : inc_wires) {
							wire_states.add(wire.exportState());
						}
						for (Wire wire : inc_wires) {
							wire.getOutport().setWire(null);
							wire.getInport().setWire(null);
							wire.setOutport(null);
							wire.setInport(null);
						}
						sm.deselect();
						sm.fireCircuitStateChanged();
						return true;
					}

					@Override
					public void undo() {
						for (WireState state : wire_states) {
							state.restore();
						}
						wm.addWire(ind_wires);
						wm.addWire(inc_wires);
						eltm.addElement(elts);
						sm_state.restore();
						sm.fireCircuitStateChanged();
					}

				};
			}
			queue.enqueue(action);
		} else {
			Application.getInstance().note("Please make a selection first.");
		}

	}

	private void move(Object[] params) {
		final int dx = (Integer) params[0];
		final int dy = (Integer) params[1];
		if (!sm.isEmpty()) {
			Action action = null;
			if (sm.getSelectedWireEdge() != null) {
				action = new Action() {
					WireState wire_state;
					SelectionManagerState sm_state = null;

					@Override
					public boolean run() {
						sm_state = sm.exportState();
						WireEdge edge = sm.getSelectedWireEdge();
						Wire wire = edge.getParent();
						wire_state = wire.exportState();
						LinkedList<WireEdge> old_edges = (LinkedList<WireEdge>) wire
								.getRoutingEdges().clone();
						boolean hasRoutingPointsChanged = wire.moveEdge(
								edge.getIndex(), dx, dy);
						if (hasRoutingPointsChanged) {
							sm.deselect();
						}
						sm.fireCircuitStateChanged();
						return true;
					}

					@Override
					public void undo() {
						wire_state.restore();
						if (sm_state != null) {
							sm_state.restore();
						}
						sm.fireCircuitStateChanged();
					}

				};
			} else if (!sm.getGroupManager().isEmpty()) {
				action = new Action() {
					ArrayList<WireState> wireStates = new ArrayList<WireState>();

					@Override
					public boolean run() {
						try {
							wireStates.clear();
							for (Wire wire : sm.getGroupManager()
									.getIncidentWireManager().getAllWires()) {
								wireStates.add(wire.exportState());
							}
							sm.getGroupManager().move(dx, dy);
						} catch (ElementOverlappingException e) {
							Application.getInstance().note(
									"Overlapping of Elements detected!");
							return false;
						}
						sm.fireCircuitStateChanged();
						return true;
					}

					@Override
					public void undo() {
						try {
							sm.getGroupManager().move(-dx, -dy);
							for (WireState state : wireStates) {
								state.restore();
							}
							sm.fireCircuitStateChanged();
						} catch (ElementOverlappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				};
			}
			queue.enqueue(action);
		}

	}

	/**
	 * A gate is only rotated if the following holds: 1. exactly one gate is
	 * selected 2. if the gate is rotated, it will not overlap any other gates
	 * 
	 * @param params
	 */
	private void rotate(Object[] params) {
		final boolean isClockWise = (Boolean) params[0];
		Action rotate_action = new Action() {
			ArrayList<WireState> wireStates = new ArrayList<WireState>();
			Direction dir_backup;
			Element elt;

			@Override
			public boolean run() {
				if (sm.getGroupManager().getElementSize() == 1) {
					elt = sm.getGroupManager().getElements().get(0);
					int wa = elt.getHeight();
					int ha = elt.getWidth();
					if (eltm.getExcludeElementDictionary().overlapping(
							elt.getX(), elt.getY(), wa, ha)) {
						Application
								.getInstance()
								.note("Cannot rotate, otherwise gates would overlap.");
						return false;
					} else {
						// Back up the old orientation.
						dir_backup = elt.getOrientation();
						// Rotate the Element
						switch (elt.getOrientation()) {
						case RIGHT:
							if (isClockWise) {
								elt.setOrientation(Direction.DOWN);
							} else {
								elt.setOrientation(Direction.UP);
							}
							break;
						case DOWN:
							if (isClockWise) {
								elt.setOrientation(Direction.LEFT);
							} else {
								elt.setOrientation(Direction.RIGHT);
							}
							break;
						case LEFT:
							if (isClockWise) {
								elt.setOrientation(Direction.UP);
							} else {
								elt.setOrientation(Direction.DOWN);
							}
							break;
						case UP:
							if (isClockWise) {
								elt.setOrientation(Direction.RIGHT);
							} else {
								elt.setOrientation(Direction.LEFT);
							}
							break;
						}
						// Back up the incident wire states and adjust the
						// wire's routing points
						for (Wire wire : sm.getGroupManager()
								.getIncidentWireManager().getInComingWires()) {
							wireStates.add(wire.exportState());
							wire.adjustForInport();
						}
						for (Wire wire : sm.getGroupManager()
								.getIncidentWireManager().getOutGoingWires()) {
							wireStates.add(wire.exportState());
							wire.adjustForOutport();
						}
						eltm.getElementDictionary().rotate(elt);
						eltm.getExcludeElementDictionary().rotate(elt);
						sm.fireCircuitStateChanged();
						return true;
					}
				} else if (sm.getGroupManager().getElementSize() <= 0) {
					Application.getInstance().note("Select a gate first.");
					return false;
				} else {
					Application
							.getInstance()
							.note("Too many gates selected.  Can only rotate one gate at a time.");
					return false;
				}
			}

			@Override
			public void undo() {
				elt.setOrientation(dir_backup);
				for (WireState state : wireStates) {
					state.restore();
				}
				sm.fireCircuitStateChanged();
			}

		};
		queue.enqueue(rotate_action);

	}

	private void changeNumberOfInport(Object[] params) {
		final int numIPs = (Integer) params[0];
		if (sm.getGroupManager().getElementSize() == 1) {
			// 'elt' is the current selected Element.
			final Element elt = sm.getGroupManager().getElements().get(0);
			Action change_maxIPs_action = new Action() {
				ElementPortState port_state;
				ArrayList<WireState> wire_states = new ArrayList<WireState>();

				/**
				 * The height/width of 'elt' will be updated as well, depending
				 * on its orientation. If 'elt' would overlap with any other
				 * Element, an error will be shown and the action will be
				 * aborted.
				 * 
				 * Some subclasses of Element has limits on the maximum or
				 * minimum number of inports or outports. If the limits are
				 * violated, then an error will be shown and the action will be
				 * aborted.
				 * 
				 * Some subclasses of Element has fixed number of inports. In
				 * this case, an error will be shown and the action will be
				 * aborted.
				 * 
				 * @return
				 */
				@Override
				public boolean run() {
					if (elt.getNumberOfIPs() == numIPs) {
						return false;
					} else {
						if (elt.canChangeNumIPs()) {
							Rectangle expected_bound = Element.getNewBound(elt,
									numIPs, true);
							boolean overlap = eltm
									.getExcludeElementDictionary().overlapping(
											expected_bound.getX(),
											expected_bound.getY(),
											expected_bound.getWidth(),
											expected_bound.getHeight());
							if (overlap) {
								Application.getInstance().note(
										"There would be overlapping");
								return false;
							} else {
								// Back up the states
								port_state = elt.exportPortState();
								for (Wire wire:sm.getGroupManager().getIncidentWireManager().getAllWires()){
									wire_states.add(wire.exportState());
								}
								for (Wire wire:sm.getGroupManager().getInducedWires()){
									wire_states.add(wire.exportState());
								}
								
								try {
									elt.setNumberOfIPs(numIPs);
									eltm.getElementDictionary().scale(elt);
									eltm.getExcludeElementDictionary().scale(elt);
									for (Wire wire:sm.getGroupManager().getIncidentWireManager().getInComingWires()){
										wire.adjustForInport();
									}
									for (Wire wire:sm.getGroupManager().getIncidentWireManager().getOutGoingWires()){
										wire.adjustForOutport();
									}
									for (Wire wire:sm.getGroupManager().getInducedWires()){
										wire.adjustForInport();
										wire.adjustForOutport();
									}
									sm.fireCircuitStateChanged();
									return true;
								} catch (MaxNumberOfPortsOutOfBoundException e) {
									Application.getInstance().note(
											"The new number is too small");
									return false;
								}
							}
						} else {
							Application.getInstance().note(
									"Cannot change the number of inports");
							return false;
						}
					}
				}

				@Override
				public void undo() {
					port_state.restore();
					for (WireState state:wire_states){
						state.restore();
					}
					sm.fireCircuitStateChanged();
				}

			};
			queue.enqueue(change_maxIPs_action);
		} else {
			Application.getInstance().note(
					"Must select exactly one gate to edit");
		}
	}

	private void changeNumberOfOutport(Object[] params) {
		final int numOPs = (Integer) params[0];
		if (sm.getGroupManager().getElementSize() == 1) {
			// 'elt' is the current selected Element.
			final Element elt = sm.getGroupManager().getElements().get(0);
			Action change_maxIPs_action = new Action() {
				ElementPortState port_state;
				ArrayList<WireState> wire_states = new ArrayList<WireState>();

				/**
				 * The height/width of 'elt' will be updated as well, depending
				 * on its orientation. If 'elt' would overlap with any other
				 * Element, an error will be shown and the action will be
				 * aborted.
				 * 
				 * Some subclasses of Element has limits on the maximum or
				 * minimum number of inports or outports. If the limits are
				 * violated, then an error will be shown and the action will be
				 * aborted.
				 * 
				 * Some subclasses of Element has fixed number of outports. In
				 * this case, an error will be shown and the action will be
				 * aborted.
				 * 
				 * @return
				 */
				@Override
				public boolean run() {
					if (elt.getNumberOfIPs() == numOPs) {
						return false;
					} else {
						if (elt.canChangeNumOPs()) {
							Rectangle expected_bound = Element.getNewBound(elt,
									numOPs, true);
							boolean overlap = eltm
									.getExcludeElementDictionary().overlapping(
											expected_bound.getX(),
											expected_bound.getY(),
											expected_bound.getWidth(),
											expected_bound.getHeight());
							if (overlap) {
								Application.getInstance().note(
										"There would be overlapping");
								return false;
							} else {
								// Back up the states
								port_state = elt.exportPortState();
								for (Wire wire:sm.getGroupManager().getIncidentWireManager().getAllWires()){
									wire_states.add(wire.exportState());
								}
								for (Wire wire:sm.getGroupManager().getInducedWires()){
									wire_states.add(wire.exportState());
								}
								
								try {
									elt.setNumberOfIPs(numOPs);
									eltm.getElementDictionary().scale(elt);
									eltm.getExcludeElementDictionary().scale(elt);
									for (Wire wire:sm.getGroupManager().getIncidentWireManager().getInComingWires()){
										wire.adjustForInport();
									}
									for (Wire wire:sm.getGroupManager().getIncidentWireManager().getOutGoingWires()){
										wire.adjustForOutport();
									}
									for (Wire wire:sm.getGroupManager().getInducedWires()){
										wire.adjustForInport();
										wire.adjustForOutport();
									}
									sm.fireCircuitStateChanged();
									return true;
								} catch (MaxNumberOfPortsOutOfBoundException e) {
									Application.getInstance().note(
											"The new number is too small");
									return false;
								}
							}
						} else {
							Application.getInstance().note(
									"Cannot change the number of inports");
							return false;
						}
					}
				}

				@Override
				public void undo() {
					port_state.restore();
					for (WireState state:wire_states){
						state.restore();
					}
					sm.fireCircuitStateChanged();
				}

			};
			queue.enqueue(change_maxIPs_action);
		} else {
			Application.getInstance().note(
					"Must select exactly one gate to edit");
		}
	}

	private void selectWire(Object[] params) {
		final Wire wire = (Wire) params[0];
		assert wire != null;
		Action select_wire_action = new Action() {
			SelectionManagerState previous_state;

			@Override
			public boolean run() {
				previous_state = sm.exportState();
				sm.deselect();
				sm.select(wire);
				return true;
			}

			@Override
			public void undo() {
				previous_state.restore();
			}
		};
		queue.enqueue(select_wire_action);
	}

	private void selectWireEdge(Object[] params) {
		final WireEdge edge_to_select = (WireEdge) params[0];
		assert edge_to_select != null;
		Action select_wire_edge_action = new Action() {
			SelectionManagerState previous_state;

			@Override
			public boolean run() {
				previous_state = sm.exportState();
				sm.deselect();
				sm.select(edge_to_select);
				return true;
			}

			@Override
			public void undo() {
				previous_state.restore();
			}
		};
		queue.enqueue(select_wire_edge_action);
	}

	private void selectElement(Object[] params) {
		final Element elt_selected = (Element) params[0];
		assert elt_selected != null;
		Action select_element_action = new Action() {
			SelectionManagerState previous_state;

			@Override
			public boolean run() {
				previous_state = sm.exportState();
				sm.deselect();
				sm.select(elt_selected);
				return true;
			}

			@Override
			public void undo() {
				previous_state.restore();
			}

		};
		queue.enqueue(select_element_action);
	}

	private void selectMultiElement(Object[] params) {
		final Element elt_selected = (Element) params[0];
		Action action = new Action() {
			SelectionManagerState previous_state;

			@Override
			public boolean run() {
				previous_state = sm.exportState();
				sm.select(elt_selected);
				return true;
			}

			@Override
			public void undo() {
				previous_state.restore();
			}

		};
		queue.enqueue(action);
	}

	private void selectGroupElement(Object[] params) {
		final List<Element> elts_selected = (List<Element>) params[0];
		Action action = new Action() {
			SelectionManagerState previous_state;

			@Override
			public boolean run() {
				previous_state = sm.exportState();
				sm.deselect();
				sm.select(elts_selected);
				return true;
			}

			@Override
			public void undo() {
				previous_state.restore();
			}

		};
		queue.enqueue(action);
	}

	private void selectGroupMultiElement(Object[] params) {
		final List<Element> elts_selected = (List<Element>) params[0];
		Action action = new Action() {
			SelectionManagerState previous_state;

			@Override
			public boolean run() {
				previous_state = sm.exportState();
				sm.select(elts_selected);
				return true;
			}

			@Override
			public void undo() {
				previous_state.restore();
			}

		};
		queue.enqueue(action);
	}

	private void deselect() {
		Action action = new Action() {
			SelectionManagerState sm_state;

			@Override
			public boolean run() {
				if (!sm.isEmpty()) {
					sm_state = sm.exportState();
					sm.deselect();
					return true;
				}
				return false;
			}

			@Override
			public void undo() {
				sm_state.restore();
			}

		};
		queue.enqueue(action);

	}

	private void undo() {
		queue.revert();

	}

	private void redo() {
		queue.forward();
	}

	private void copy() {
		if (!sm.isEmpty()) {
			if (!sm.getGroupManager().isEmpty()) {
				sm.getGroupManager().copyToClipBoard();
			} else {
				Application.getInstance().note("Only gates can be copied.");
			}
		} else {
			Application.getInstance().note("No gates are selected.");
		}
	}

	private void paste(Object[] params) {
		final int x = (Integer) params[0];
		final int y = (Integer) params[1];
		final ClipBoard cb = ClipBoard.getInstance();
		if (cb.isEmpty()) {
			Application.getInstance().note("Clipboard is empty.");
		} else {
			Action paste_action = new Action() {
				ArrayList<Element> elts_pasted = new ArrayList<Element>();
				ArrayList<Wire> wires_pasted = new ArrayList<Wire>();
				SelectionManagerState sm_state;

				@Override
				public boolean run() {
					CircuitContainer cc = cb.getNewCircuit();
					elts_pasted = cc.getElements();
					wires_pasted = cc.getWires();
					// Shift the Elements and Wires to be pasted down so they
					// are below the current circuit
					int dx = Integer.MIN_VALUE, dy = Integer.MIN_VALUE;
					int max_y; // The maximum y value that the pasted Elements
								// have to be put at
					Rectangle bound = eltm.getBound();
					// It is guaranteed that there will be no overlapping, since
					// all pasted Elements are to the right of the existing
					// Elements.
					if (x > bound.getX() + bound.getWidth()) {
						max_y = y;
					} else { // Pasted Elements will be set to below all
								// existing Elements.
						max_y = Math.max(y, bound.getY() + bound.getHeight());
					}
					for (Element elt : elts_pasted) {
						int dx2 = x - elt.getX();
						if (dx2 > dx) {
							dx = dx2;
						}
						int dy2 = max_y - elt.getY();
						if (dy2 > dy) {
							dy = dy2;
						}
					}
					dx++;
					dy++;
					if (dy > 0) {
						for (Element elt : elts_pasted) {
							elt.setPosition(elt.getX() + dx, elt.getY() + dy);
						}
						for (Wire wire : wires_pasted) {
							wire.move(dx, dy);
						}
					}
					eltm.addElement(elts_pasted);
					wm.addWire(wires_pasted);
					cm.fireStateChangedEvent();
					sm_state = sm.exportState();
					sm.deselect();
					return true;
				}

				@Override
				public void undo() {
					eltm.remove(elts_pasted);
					wm.remove(wires_pasted);
					sm_state.restore();
					cm.fireStateChangedEvent();
				}
			};
			queue.enqueue(paste_action);
		}
	}

	class ActionQueue {
		private final int max_length;
		private final LinkedList<Action> queue;
		private int pt;
		private int num_actions;

		protected ActionQueue(int max_length) {
			this.max_length = max_length;
			this.queue = new LinkedList<Action>();
			pt = -1;
			num_actions = 0;
		}

		/**
		 * Only enqueue an action if it ran successfully.
		 * 
		 * @param action
		 */
		protected void enqueue(Action action) {
			if (action != null) {
				boolean successful = action.run();
				if (successful) {
					pt++;
					int s = queue.size();
					for (int i = pt; i < s; i++) {
						queue.removeLast();
					}
					queue.addLast(action);
					System.out.println("pt =" + pt);
					System.out.println("queue.size() = " + queue.size());
					assert pt == queue.size() - 1;
					for (int i = 0; i < queue.size() - max_length; i++) {
						queue.removeFirst();
						pt--;
					}
					num_actions = queue.size();
				}
			}
		}

		protected boolean canRevert() {
			return pt >= 0;
		}

		protected void revert() {
			if (canRevert()) {
				Action act = queue.get(pt);
				pt--;
				act.undo();
			} else {
				Application.getInstance().note(
						"No more actions to undo.  Current maximum aciton stack size is "
								+ max_length + ".");
			}
		}

		protected boolean canForward() {
			return pt < num_actions - 1;
		}

		protected void forward() {
			pt++;
			Action act = queue.get(pt);
			boolean successful = act.run();
			assert successful;
		}
	}
}
