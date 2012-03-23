package com.asys.editor.model;

public interface ElementCreationManagerListener {
	public void started();
	public void finished();
	public void cancelled();
	public void changedCoordinates();
}
