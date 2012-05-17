/**
 * 
 */
package com.asys.simulator;

/**
 * @author ryan
 *
 */
public class SimulatorModel {
	private GateFactory gf;
	private PortFactory pf;
	private TransitionEventFactory tef;
	private Scheduler scheduler;
	private EventProcessor ep;
	private static SimulatorModel instance;
	
	public static SimulatorModel getInstance(){
		if (instance == null){
			instance = new SimulatorModel();
		}
		return instance;
	}
	
	private SimulatorModel(){
		this.gf = GateFactory.getInstance();
		this.pf = PortFactory.getInstance();
		this.tef = TransitionEventFactory.getInstance();
		this.scheduler = Scheduler.getInstance();
		this.ep = EventProcessor.getInstance();
	}
	
	public void clear(){
		gf.clear();
		pf.clear();
		tef.clear();
		scheduler.reset();
		ep.reset();
	}
	
}
