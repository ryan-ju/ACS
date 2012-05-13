/**
 * 
 */
package com.asys.simulator;

/**
 * @author ryan
 *
 */
public interface AbstractScriptParser {
	public boolean isValid(String string);
	public AbstractScript parse(String string);
}
