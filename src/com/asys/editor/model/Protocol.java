/**
 * 
 */
package com.asys.editor.model;

import com.asys.simulator.AbstractScript;

/**
 * @author ryan
 *
 */
public class Protocol {
	private String string;
	private AbstractScript script;
	
	public AbstractScript getScript(){
		return script;
	}
	
	public boolean setString(String string){
		if (AbstractScript.isValid(string)){
			this.string = string;
			script = AbstractScript.parse(this.string);
			return true;
		}else{
			return false;
		}
	}
	
	public String getString(){
		return string;
	}
}
