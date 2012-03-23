/**
 * 
 */
package com.asys.views;

import java.util.ArrayList;

/**
 * @author ryan
 *
 */
public class ModeManager {
	private static ModeManager instance;
	private ArrayList<ModeManagerListener> listeners;
	private Mode mode;
	
	public static ModeManager getInstance(){
		if (instance == null){
			instance = new ModeManager();
		}
		return instance;
	}
	
	private ModeManager(){
		this.listeners = new ArrayList<ModeManagerListener>();
	}
	
	public Mode getMode(){
		return mode;
	}
	
	public void setMode(Mode mode){
		this.mode = mode;
		this.fireModeChanged();
	}
	
	public void addListener(ModeManagerListener lst){
		if (!listeners.contains(lst)){
			listeners.add(lst);
		}
	}
	
	public void removeAllListener(){
		listeners.clear();
	}
	
	private void fireModeChanged(){
		for (ModeManagerListener lst:listeners){
			lst.modeChanged(mode);
		}
	}
}
