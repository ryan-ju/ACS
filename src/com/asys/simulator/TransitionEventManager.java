/**
 * 
 */
package com.asys.simulator;

import com.asys.simulator.exceptions.IdNotExistException;

/**
 * @author ryan
 *
 */
public interface TransitionEventManager {
	public TransitionEvent getTransitionEvent(String id);
	public boolean transitionEventIdExists(String id);
	public boolean isStartEvent(String id) throws IdNotExistException;
	public void removeEventsAfter(long time);
	public void clear();
}
