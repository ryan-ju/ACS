/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 *
 */
public interface Action {
	/**
	 * 
	 * @return true - if the action ran successfully.
	 */
	public boolean run();
	public void undo();
}
