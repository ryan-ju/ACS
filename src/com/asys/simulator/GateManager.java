/**
 * 
 */
package com.asys.simulator;

/**
 * @author ryan
 *
 */
public interface GateManager {
	public Gate getGate(String id);
	public boolean gateIdExists(String id);
}
