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
import javax.swing.JButton;
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
import com.asys.simulator.SimulatorModel;

/**
 * @author ryan
 * 
 */
public class Application extends JFrame implements ErrorNotifier {
	private static Application instance;
	private Canvas canvas;
	private Repository rep;
	private ModeManager mm;
	private SimulatorModel sm;
	private JTextField error_tf;
	private Timer timer;
	private Builder builder;
	private GateFactory gf;
	private Scheduler scheduler;
	private EventProcessor ep;
	private SettingFrame setting_frame;
	private JToggleButton edit_btn;
	private JToggleButton simulation_btn;
	private JButton build_btn;
	private JButton simulate_btn;
	private JButton set_init_btn;
	private JButton stop_btn;
	private JButton pause_btn;
	private JButton step_btn;

	public static Application getInstance() {
		if (instance == null) {
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
		sm = SimulatorModel.getInstance();
		timer = new Timer(Constant.NOTIFICATION_DELAY,
				getClearNotificationActionListener());
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
		edit_btn = new JToggleButton("Edit Mode");
		simulation_btn = new JToggleButton("Simulation Mode");
		ButtonGroup btn_group = new ButtonGroup();
		btn_group.add(edit_btn);
		btn_group.add(simulation_btn);
		tool_bar.add(edit_btn);
		tool_bar.add(simulation_btn);
		edit_btn.setSelected(true);
		tool_bar.addSeparator();
		content.add(tool_bar, BorderLayout.NORTH);

		// Add another tool bar for holding the buttons for simulation
		JToolBar tool_bar2 = new JToolBar();
		build_btn = new JButton("Build");
		set_init_btn = new JButton("Set...");
		simulate_btn = new JButton("Start");
		stop_btn = new JButton("Stop");
		pause_btn = new JButton("Pause");
		step_btn = new JButton("Step forward");
		tool_bar2.add(build_btn);
		tool_bar2.add(set_init_btn);
		tool_bar2.add(simulate_btn);
		tool_bar2.addSeparator();
		tool_bar2.add(stop_btn);
		tool_bar2.add(pause_btn);
		tool_bar2.add(step_btn);
		tool_bar.add(tool_bar2, BorderLayout.NORTH);
		setVisibleSimulationButtons(false);

		// The split pane on the left side containing the repository and the
		// property viewer
		final JSplitPane left_split_pl = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT);
		left_split_pl.setTopComponent(rep);
		left_split_pl.setBottomComponent(pv);
		left_split_pl.setDividerLocation(200);

		// The main split pane containing 'left_split_pl' and 'canvas'
		final JSplitPane main_split_pl = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT);
		main_split_pl.setDividerLocation(250);
		main_split_pl.setLeftComponent(left_split_pl);
		main_split_pl.setRightComponent(canvas);

		// Add the components to the content panel of this Application
		content.add(main_split_pl, BorderLayout.CENTER);
		content.add(error_tf, BorderLayout.SOUTH);

		// Add menu bar
		// this.setJMenuBar(createMenuBar());

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.mm.addListener(canvas);
		mm.setMode(Mode.EDIT_MODE);

		// Set actions for edit_btn and simulation_btn
		edit_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				left_split_pl.setVisible(true);
				main_split_pl.setDividerLocation(250);
				mm.setMode(Mode.EDIT_MODE);
				setVisibleSimulationButtons(false);
				sm.setPoluted();
			}

		});
		simulation_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				left_split_pl.setVisible(false);
				main_split_pl.setDividerLocation(0);
				builder.build(CircuitManager.getInstance());
				mm.setMode(Mode.SIMULATION_MODE);
				setVisibleSimulationButtons(true);
			}

		});
		set_init_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (setting_frame == null) {
					setting_frame = new SettingFrame();
				}
				setting_frame.setSize(400, 300);
				setting_frame.setVisible(true);
			}
		});
		build_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sm.build();
			}
		});
		simulate_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sm.start();
				setEnabledButtensWhenPlaying(false);
			}
		});
		
		stop_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sm.stop();
				setEnabledButtensWhenPlaying(true);
			}
		});
		pause_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sm.pause();
				setEnabledButtensWhenPlaying(true);
			}
		});
		step_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sm.step();
			}
		});
		
	}

	private void setVisibleSimulationButtons(boolean visible) {
		build_btn.setVisible(visible);
		simulate_btn.setVisible(visible);
		set_init_btn.setVisible(visible);
		stop_btn.setVisible(visible);
		pause_btn.setVisible(visible);
		step_btn.setVisible(visible);
	}
	
	private void setPlayingButtonsEnabled(boolean enabled){
		stop_btn.setEnabled(enabled);
		pause_btn.setEnabled(enabled);
		step_btn.setEnabled(enabled);
	}
	
	private void setEnabledButtensWhenPlaying(boolean enabled){
		edit_btn.setEnabled(enabled);
		simulation_btn.setEnabled(enabled);
		build_btn.setEnabled(enabled);
		set_init_btn.setEnabled(enabled);
		simulate_btn.setEnabled(enabled);
		step_btn.setEnabled(enabled);
	}

	private JMenuBar createMenuBar() {
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
		if (timer.isRunning()) {
			timer.restart();
		} else {
			timer.start();
		}
	}

	private ActionListener getClearNotificationActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Application.this.error_tf.setText("");
				Application.this.error_tf
						.setBackground(Constant.NOTIFIER_NORMAL_BACKGROUND_CLR);
			}

		};
	}

	// Action Manager
	class ActionManager {

	}

	// ====================================================
	// main
	// ====================================================

	public static void main(String[] args) {
		Application.getInstance().setVisible(true);
	}
}
