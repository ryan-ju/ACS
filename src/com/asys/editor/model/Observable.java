package com.asys.editor.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
	protected List<Listener> ls;
	
	public Observable(){
		this.ls = new ArrayList<Listener>();
	}
	
	public void addListener(Listener lst){
		ls.add(lst);
	}
	
	public void removeAllListener(){
		ls.clear();
	}
	
	abstract public void fireStateChanged();
}
