/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 *
 */
public interface WireCreationManagerListener {
	public void startedCreation();
	public void finishedCreation();
	public void cleared();
	public void addedPoint();
	public void removedLastPoint();
	public void coordinatesChanged();
	public void cancelled();
}
