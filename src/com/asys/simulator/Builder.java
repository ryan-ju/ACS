/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.CircuitUtilities;
import com.asys.editor.model.Element;
import com.asys.editor.model.Fanout;
import com.asys.editor.model.Inport;
import com.asys.editor.model.Outport;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;
import com.asys.simulator.exceptions.IdExistException;
import com.asys.simulator.exceptions.IdNotExistException;
import com.asys.simulator.exceptions.InvalidElementException;

/**
 * @author ryan
 * 
 */
public class Builder {
	private GateFactory gate_factory;
	private PortFactory port_factory;
	private static Builder instance;

	public static Builder getInstance() {
		if (instance == null) {
			instance = new Builder();
		}
		return instance;
	}

	private Builder() {
		this.gate_factory = GateFactory.getInstance();
		this.port_factory = PortFactory.getInstance();
	}

	public void build(CircuitManager circuit_manager) {
		ArrayList<Element> elements = circuit_manager.getElementManager()
				.getElements();
		HashMap<Element, String> temp_elt_map = new HashMap<Element, String>();
		HashMap<Inport, String> temp_ip_map = new HashMap<Inport, String>();
		HashMap<Outport, String> temp_op_map = new HashMap<Outport, String>();
		// Create the gates, input ports and output ports
		for (Element element : elements) {
			if (!(element instanceof Fanout)) {
				String gate_id = null;
				try {
					gate_id = gate_factory.createGate(element);
				} catch (InvalidElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				temp_elt_map.put(element, gate_id);

				Gate gate = null;
				try {
					gate = gate_factory.getGate(gate_id);
				} catch (IdNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (Inport ip : element.getInports()) {
					if (ip != null && ip.getWire() != null) { // The Inport is
																// connected
						String input_port_id = port_factory.createInputPort(
								gate_id, ip);
						temp_ip_map.put(ip, input_port_id);
						try {
							gate.addInputPortId(input_port_id);
						} catch (IdNotExistException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (element.getOutports().size() > 0) {
					Outport op = element.getOutports().get(0);
					if (op.getWire() != null) { // The Outport is connected
						String output_port_id = port_factory.createOutputPort(
								gate_id, op);
						gate.setOutputPordId(output_port_id);
						temp_op_map.put(op, output_port_id);
					}
				}
			}
		}

		// Establish the relationship between the gates, input ports and output
		// ports
		for (Element element : elements) {
			System.out
					.println("==========================================================================================");
			if (!(element instanceof Fanout)) {
				List<Object[]> children_objs = CircuitUtilities
						.childrenBeyondFanout(element);
				System.out.println("**************************************");
				System.out
						.println("* List of input port IDs to be added to the output port of gate \""
								+ temp_elt_map.get(element) + "\"");
				System.out.println("**************************************");
				for (Object[] connection : children_objs) {
					Inport ip = (Inport) connection[0];
					System.out.println("* " + temp_ip_map.get(ip));
				}
				System.out.println("**************************************");
				System.out.println();
				for (Object[] connection : children_objs) {
					Inport ip = (Inport) connection[0];
					Outport op = (Outport) connection[1];
					String input_port_id = temp_ip_map.get(ip);
					OutputPort opp = null;
					String opp_id = null;
					try {
						opp_id = temp_op_map.get(op);
						System.out.println("Opp_id == " + opp_id);
						opp = port_factory.getOutputPort(opp_id);
					} catch (IdNotExistException e) {
						System.out
								.println("=======================================================");
						System.out.println("Gate factory dump:");
						System.out.println(gate_factory.dump());
						System.out
								.println("=======================================================");
						System.out.println();
						System.out
								.println("=======================================================");
						System.out.println("Port factory dump:");
						System.out.println(port_factory.dump());
						System.out
								.println("=======================================================");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						System.out.println("Output port " + opp_id
								+ " will add input port " + input_port_id);
						// Add input_port_id to opp
						opp.addInputPortId(input_port_id);
						// Add opp_id to input_port_id's target
						port_factory.getInputPort(input_port_id).setOutputPortId(opp_id);
					} catch (IdExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IdNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
