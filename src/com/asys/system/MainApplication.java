/**
 * 
 */
package com.asys.system;

import com.asys.editor.model.ErrorNotifier;

/**
 * @author ryan
 * 
 */
public class MainApplication {
	private static MainApplication main = new MainApplication();
	private ErrorNotifier notifier = new ErrorNotifier(){
		@Override
		public void note(Exception expt) {
			System.out.println("Notified Exception (don't panic if you see this message, it's normal at testing stage): " );
			System.out.println();
		}

		@Override
		public void note(String message) {
			System.out.println("Notified Exception (don't panic if you see this message, it's normal at testing stage): " );
			System.out.println(message);
			System.out.println();
		}
	};

	public static MainApplication getInstance() {
		return main;
	}

	public ErrorNotifier getErrorNotifier() {
		return notifier;
	}
}
