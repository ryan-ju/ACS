/**
 * 
 */
package com.asys.simulator;

import java.util.Set;

import com.asys.simulator.exceptions.IdNotExistException;

/**
 * @author ryan
 *
 */
public interface PortManager {
	public InputPort getInputPort(String input_port_id) throws IdNotExistException;
	public OutputPort getOutputPort(String output_port_id) throws IdNotExistException;
	public boolean containsInputPortId(String input_port_id);
	public boolean containsOutputPortId(String output_port_id);	
	public Set<String> getInputPortIds();
	public Set<String> getOutputPortIds();
	public boolean isInputPort(String port_id) throws IdNotExistException;
}
