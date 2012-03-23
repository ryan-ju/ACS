/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 *
 */
public interface WireEdgeCreationManagerListener {
	public void started();
	public void coordinatesChanged();
	public void cancelled();
	public void finished();
}
