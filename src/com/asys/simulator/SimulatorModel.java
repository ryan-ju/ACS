/**
 * 
 */
package com.asys.simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import com.asys.constants.LogicValue;
import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.EnvironmentGate;
import com.asys.simulator.AbstractScript.DelayValuePair;
import com.asys.simulator.exceptions.IdExistException;
import com.asys.simulator.exceptions.IdNotExistException;
import com.asys.simulator.exceptions.SchedulerEmptyException;
import com.asys.views.Application;
import com.asys.views.Mode;
import com.asys.views.ModeManagerListener;

/**
 * @author ryan
 *
 */
public class SimulatorModel implements ModeManagerListener {
	private GateFactory gf;
	private PortFactory pf;
	private TransitionEventFactory tef;
	private Scheduler scheduler;
	private EventProcessor ep;
	private Builder b;
	private int unitPerSecond;
	private boolean built = false;
	private boolean isAutomated = true;
	private Timer timer;
	private long counter;
	private boolean running = false;
	private boolean paused;
	private String sheet;
	private String event_id;
	private boolean justStarted = true;
	private ArrayList<SimulatorModelListener> listeners;
	private static SimulatorModel instance;

	public static SimulatorModel getInstance() {
		if (instance == null) {
			instance = new SimulatorModel();
		}
		return instance;
	}

	private SimulatorModel() {
		this.gf = GateFactory.getInstance();
		this.pf = PortFactory.getInstance();
		this.tef = TransitionEventFactory.getInstance();
		this.scheduler = Scheduler.getInstance();
		this.ep = EventProcessor.getInstance();
		this.b = Builder.getInstance();
		this.counter = 0;
		this.unitPerSecond = 1;
		this.listeners = new ArrayList<SimulatorModelListener>();
	}

	public void clear() {
		gf.clear();
		pf.clear();
		tef.clear();
		scheduler.reset();
		ep.reset();
		counter = 0;
		running = false;
		built = false;
		unitPerSecond = 1;
	}

	public void cleanReset() {
		tef.clear();
		scheduler.reset();
		ep.reset();
		b.initialize();
		counter = 0;
		running = false;
		unitPerSecond = 1;
	}

	public void reset() {
		tef.clear();
		scheduler.reset();
		ep.reset();
		isStable(sheet);
		counter = 0;
		running = false;
	}

	public void build() {
		clear();
		b.build(CircuitManager.getInstance());
		scheduler.initialize();
		built = true;
		fireChanged();
	}

	public boolean isStable(String sheet) {
		this.sheet = sheet;
		return b.isStable(sheet);
	}

	public void setPoluted() {
		built = false;
	}

	public boolean isBuilt() {
		return built;
	}

	public void initialize() {
		b.initialize();
		scheduler.initialize();
		built = true;
		fireChanged();
	}

	/**
	 * Add events to the scheduler 
	 */
	public void initiate() {
		for (String gate_id : gf.getGateIds()) {
			Gate gate;
			try {
				gate = gf.getGate(gate_id);
				if (gate.getElement() instanceof EnvironmentGate) {
					DelayValuePair pair = Queries.getScript(gate_id).getNextPair();
					System.out.println("pair = null? " + pair == null);
					if (pair != null) {
						String te_id1 = tef.createTransitionEvent(gate_id, LogicValue.X, pair.getDelay(), 0, new CausativeLink());
						String te_id2 = tef.createTransitionEvent(gate_id, pair.getLogicValue(), pair.getDelay(), 0, new CausativeLink());
						System.out.println("SimulatorModel.initiate()");
						System.out.println(gf.dump());
						scheduler.schedule(te_id1);
						System.out.println("Event \"" + te_id1 + "\" is scheduled during initiating!");
						scheduler.schedule(te_id2);
						System.out.println("Event \"" + te_id1 + "\" is scheduled during initiating!");
					}
				}
			} catch (IdNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IdExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int getUnitPerSecond() {
		return unitPerSecond;
	}

	public boolean setUnitPerSecond(int i) {
		if (i <= 0) {
			return false;
		} else {
			this.unitPerSecond = i;
			return true;
		}
	}

	public void setIsAutomated(boolean isAutomated) {
		this.isAutomated = isAutomated;
	}

	public void start() {
		if (!running) {
			counter = 0;
			justStarted = true;
			initiate();
		}
		if (isAutomated) {
			timer = new Timer(1000 / unitPerSecond, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (justStarted) {
							if (scheduler.isEmpty()) {
								running = false;
								timer.stop();
								System.out.println("I've stopped!");
								Application.getInstance().note("Circuit time = "+counter+" ps.  Stopped.");
								running = false;
							} else {
								event_id = scheduler.getNextTransitionEventId();
								running = true;
							}
							justStarted = false;
							return;
						}
						if (tef.getTransitionEvent(event_id).getCircuitTime() <= counter) {
							List<String> new_events = ep.process(event_id);
							System.out.println("==SimulatorModel.Start()==");
							System.out.println("\tAfter processing event \"" + event_id + "\", we get these new events:");
							for (String new_event : new_events) {
								System.out.println("\t" + new_event);
							}
							if (scheduler.isEmpty()) {
								running = false;
								timer.stop();
								System.out.println("I've stopped!");
								Application.getInstance().note("Circuit time = "+counter+" ps.  Stopped.");
								running = false;
								return;
							} else {
								event_id = scheduler.getNextTransitionEventId();
								running = true;
							}
						}
						System.out.println("I'm running!");
						running = true;
						counter++;
						Application.getInstance().note("Circuit time = "+counter+" ps");
					} catch (IdNotExistException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SchedulerEmptyException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IdExistException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			timer.start();
		}
		running = true;
		paused = false;
	}

	public void stop() {
		if (timer != null && timer.isRunning()) {
			timer.stop();
		}
		running = false;
		paused = false;
		reset();
		fireChanged();
	}

	public void pause() {
		if (timer != null && timer.isRunning()) {
			timer.stop();
			paused = true;
		}
	}

	public void step() {
		if (running && paused) {
			try {
				if (event_id != null) {
					ep.process(event_id);
					if (scheduler.isEmpty()){
						Application.getInstance().note("Circuit time = "+counter+" ps.  Scheduler is empty.");
					}else{
						event_id = scheduler.getNextTransitionEventId();
						counter = tef.getTransitionEvent(event_id).getCircuitTime();
						Application.getInstance().note("Circuit time = "+counter+" ps");
					}
				}
			} catch (IdNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SchedulerEmptyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IdExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void modeChanged(Mode mode) {
		built = false;
	}

	public void addListener(SimulatorModelListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void fireChanged() {
		for (SimulatorModelListener l : listeners) {
			l.update();
		}
	}

	public void dumpState() {
		System.out.println("running = " + running);
		System.out.println("paused = " + paused);
		System.out.println("built = " + built);
		System.out.println("isAutomated = " + isAutomated);
		System.out.println("counter = " + counter);
	}
}
