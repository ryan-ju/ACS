/**
 * 
 */
package com.asys.model.components.exceptions;

import com.asys.constants.ElementPropertyKey;

/**
 * @author ryan
 *
 */
public class NoKeyException extends Exception {
	private ElementPropertyKey key;
	
	public NoKeyException(ElementPropertyKey key){
		this.key = key;
	}
	
	public ElementPropertyKey getKey(){
		return key;
	}
}
