/**
 * 
 */
package com.asys.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.Timer;

import com.asys.constants.Constant;
import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.ErrorNotifier;
import com.asys.editor.model.SelectionManager;
import com.asys.editor.model.TestInitialiser;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;
import com.asys.simulator.Builder;
import com.asys.simulator.EventProcessor;
import com.asys.simulator.GateFactory;
import com.asys.simulator.Scheduler;

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
	private Builder builder;
	private GateFactory gf;
	private Scheduler scheduler;
	private EventProcessor ep;

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
		builder = Builder.getInstance();
		gf = GateFactory.getInstance();
		scheduler = Scheduler.getInstance();
		ep = EventProcessor.getInstance();
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
		
		// Add a tool bar just below the menu bar
		JToolBar tool_bar = new JToolBar();
		JToggleButton edit_btn = new JToggleButton("Edit Mode");
		JToggleButton simulation_btn = new JToggleButton("Simulation Mode");
		ButtonGroup btn_group = new ButtonGroup();
		btn_group.add(edit_btn);
		btn_group.add(simulation_btn);
		tool_bar.add(edit_btn);
		tool_bar.add(simulation_btn);
		edit_btn.setSelected(true);
		tool_bar.addSeparator();
		content.add(tool_bar, BorderLayout.NORTH);
		
		// The split pane on the left side containing the repository and the property viewer
		final JSplitPane left_split_pl = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		left_split_pl.setTopComponent(rep);
		left_split_pl.setBottomComponent(pv);
		left_split_pl.setDividerLocation(200);
		
		// The main split pane containing 'left_split_pl' and 'canvas'
		final JSplitPane main_split_pl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		main_split_pl.setDividerLocation(250);
		main_split_pl.setLeftComponent(left_split_pl);
		main_split_pl.setRightComponent(canvas);

		// Add the components to the content panel of this Application
		content.add(main_split_pl, BorderLayout.CENTER);
		content.add(error_tf, BorderLayout.SOUTH);

		// Add menu bar
		this.setJMenuBar(createMenuBar());
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.mm.addListener(canvas);
		mm.setMode(Mode.EDIT_MODE);
		
		// Set actions for edit_btn and simulation_btn
		edit_btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				left_split_pl.setVisible(true);
				main_split_pl.setDividerLocation(250);
				mm.setMode(Mode.EDIT_MODE);
			}
			
		});
		simulation_btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				left_split_pl.setVisible(false);
				main_split_pl.setDividerLocation(0);
				builder.build(CircuitManager.getInstance());
				mm.setMode(Mode.SIMULATION_MODE);
			}
			
		});
	}
	
	private JMenuBar createMenuBar(){
		// Create a menu bar
		JMenuBar menuBar = new JMenuBar();
		// Create "File" menu
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem menuItem = new JMenuItem("New circuit");
		fileMenu.add(menuItem);
		menuItem = new JMenuItem("Save circuit");
		fileMenu.add(menuItem);
		menuItem = new JMenuItem("Save circuit as...");
		fileMenu.add(menuItem);
		fileMenu.addSeparator();
		menuItem = new JMenuItem("Exit");
		fileMenu.add(menuItem);
		
		// Create "Canvas" menu
		JMenu canvasMenu = new JMenu("Canvas");
		menuBar.add(canvasMenu);
		menuItem = new JMenuItem("Expand vertically");
		canvasMenu.add(menuItem);
		menuItem = new JMenuItem("Expand horizontally");
		canvasMenu.add(menuItem);
		menuItem = new JMenuItem("Expand in both directions");
		canvasMenu.add(menuItem);
		canvasMenu.addSeparator();
		menuItem = new JMenuItem("Pack canvas");
		canvasMenu.add(menuItem);
		
		return menuBar;
	}

	private void setupTest() {
		try {
			TestInitialiser.c_gate();
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
	
	// Action Manager
	class ActionManager{
		
	}
	
	// ====================================================
	// main
	// ====================================================

	public static void main(String[] args) {
		Application.getInstance().setVisible(true);
	}
}
