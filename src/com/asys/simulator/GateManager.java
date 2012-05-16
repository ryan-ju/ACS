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
public interface GateManager {
	public Gate getGate(String id) throws IdNotExistException;
	public Set<String> getGateIds();
	public boolean gateIdExists(String id);
}
