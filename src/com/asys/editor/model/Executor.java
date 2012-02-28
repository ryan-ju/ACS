/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 *
 */
public class Executor {
	private static Executor ext;
	
	public Executor getInstance(){
		if (ext == null) ext = new Executor();
		return ext;
	}
	
	public void execute(Command cmd){
		
	}
}
