/**
 * 
 */
package com.asys.editor.model;


/**
 * @author ryan
 *
 */
public interface ErrorNotifier {
	public void note(Exception expt);
	public void note(String message);
}
