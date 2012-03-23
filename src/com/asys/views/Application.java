/**
 * 
 */
package com.asys.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.asys.constants.Constant;
import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.ErrorNotifier;
import com.asys.editor.model.SelectionManager;
import com.asys.editor.model.TestInitialiser;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;

/**
 * @author ryan
 * 
 */
public class Application extends JFrame implements ErrorNotifier {
	private static Application instance;
	private Canvas canvas;
	private Repository rep;
	private ModeManager mm;
	private JTextField error_tf;
	private Timer timer;

	public static Application getInstance(){
		if (instance == null){
			instance = new Application();
		}
		return instance;
	}
	
	private Application() {
		setupTest();
		init();
	}

	private void init() {
		timer = new Timer(Constant.NOTIFICATION_DELAY, getClearNotificationActionListener());
		mm = ModeManager.getInstance();
		this.setSize(800, 600);
		canvas = new Canvas();
		rep = new Repository();
		rep.setMinimumSize(new Dimension(100, 0));
		PropertyViewer pv = new PropertyViewer();
		SelectionManager.getInstance().addListener(pv.getPropertyTableModel());
		error_tf = new JTextField();
		error_tf.setText("Initially...");
		error_tf.setEditable(false);
		
		JPanel content = (JPanel) this.getContentPane();
		
		// The split pane on the left side containing the repository and the property viewer
		JSplitPane left_split_pl = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		left_split_pl.setTopComponent(rep);
		left_split_pl.setBottomComponent(pv);
		left_split_pl.setDividerLocation(200);
		
		// The main split pane containing 'left_split_pl' and 'canvas'
		JSplitPane main_split_pl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		main_split_pl.setDividerLocation(250);
		main_split_pl.setLeftComponent(left_split_pl);
		main_split_pl.setRightComponent(canvas);

		content.add(main_split_pl, BorderLayout.CENTER);
		content.add(error_tf, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.mm.addListener(canvas);
		mm.setMode(Mode.EDIT_MODE);
	}

	private void setupTest() {
		try {
			TestInitialiser.initialise();
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MaxNumberOfPortsOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setupCircuit(CircuitManager cm) {
		CircuitManager.setCircuitManager(cm);
	}

	// ====================================================
	// Methods of ErrorNotifier
	// ====================================================

	@Override
	public void note(Exception expt) {
		note(expt.getMessage());
	}

	@Override
	public void note(String message) {
		error_tf.setText(message);
		error_tf.setBackground(Constant.NOTIFIER_ALERT_BACKGROUND_CLR);
		if (timer.isRunning()){
			timer.restart();
		}else{
			timer.start();
		}
	}
	
	private ActionListener getClearNotificationActionListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Application.this.error_tf.setText("");
				Application.this.error_tf.setBackground(Constant.NOTIFIER_NORMAL_BACKGROUND_CLR);				
			}
			
		};
	}

	// ====================================================
	// main
	// ====================================================

	public static void main(String[] args) {
		Application.getInstance().setVisible(true);
	}
}
