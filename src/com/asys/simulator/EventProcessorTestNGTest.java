package com.asys.simulator;

import java.util.ArrayList;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.asys.constants.ElementPropertyKey;
import com.asys.constants.LogicValue;
import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.Protocol;
import com.asys.editor.model.TestInitialiser;
import com.asys.model.components.exceptions.InvalidPropertyKeyException;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;
import com.asys.simulator.exceptions.IdExistException;
import com.asys.simulator.exceptions.IdNotExistException;
import com.asys.simulator.exceptions.SchedulerEmptyException;

public class EventProcessorTestNGTest {
	private CircuitManager cm;
	private GateFactory gf;
	private TransitionEventFactory tef;
	private Builder b;
	private Scheduler s;
	private EventProcessor p;
	private SimulatorModel sm;

	@BeforeClass
	public void setup() {
		cm = CircuitManager.getInstance();
		gf = GateFactory.getInstance();
		tef = TransitionEventFactory.getInstance();
		b = Builder.getInstance();
		s = Scheduler.getInstance();
		p = EventProcessor.getInstance();
		sm = SimulatorModel.getInstance();
		
	}

//	@Test
	public void processEventsInSmallSimpleCircuit() throws IdNotExistException, IdExistException, SchedulerEmptyException {
		//====================================================
		// Setup
		//====================================================
		try {
			TestInitialiser.simple_gates_small();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MaxNumberOfPortsOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		b.build(cm);
		System.out.println("==========GateFactory dump==========");
		System.out.println(gf.dump());

		/*
		 * g2|xor0|ip:(g2):5, ip:(g2):6|op:(g2):2
		 * g1|or0|ip:(g1):2, ip:(g1):3, ip:(g1):4|op:(g1):1
		 * g0|and0|ip:(g0):0, ip:(g0):1|op:(g0):0
		 * g6|input2||op:(g6):5
		 * g5|input1||op:(g5):4
		 * g4|input0||op:(g4):3
		 * g3|nand0||null
		 */

		// Set initial values for gates
		Gate g0, g1, g2, g3, g4, g5, g6;
		try {
			g2 = gf.getGate("g2");
			g1 = gf.getGate("g1");
			g0 = gf.getGate("g0");
			g6 = gf.getGate("g6");
			g5 = gf.getGate("g5");
			g4 = gf.getGate("g4");
			g3 = gf.getGate("g3");

			g2.setCurrentLogicValue(LogicValue.ONE);
			g1.setCurrentLogicValue(LogicValue.ZERO);
			g0.setCurrentLogicValue(LogicValue.ZERO);
			g6.setCurrentLogicValue(LogicValue.ONE);
			g5.setCurrentLogicValue(LogicValue.ZERO);
			g4.setCurrentLogicValue(LogicValue.ONE);
			g3.setCurrentLogicValue(LogicValue.ONE);
			
			// Initialize the scheduler
			s.initialize();
		} catch (IdNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//====================================================
		// Test
		//====================================================
		
		CausativeLink link1 = new CausativeLink();
		String t1 = tef.createTransitionEvent("g6", LogicValue.X, 5, 100, link1);
		link1.add(t1);
		CausativeLink link2 = new CausativeLink();
		String t2 = tef.createTransitionEvent("g5", LogicValue.X, 6, 100, link2);
		link2.add(t2);
		CausativeLink link3 = new CausativeLink();
		String t3 = tef.createTransitionEvent("g4", LogicValue.X, 7, 100, link3);
		link3.add(t3);
		CausativeLink link4 = new CausativeLink();
		String t4 = tef.createTransitionEvent("g6", LogicValue.ZERO, 6, 100, link4);
		link4.add(t4);
		CausativeLink link5 = new CausativeLink();
		String t5 = tef.createTransitionEvent("g5", LogicValue.ONE, 9, 100, link5);
		link4.add(t5);
		CausativeLink link6 = new CausativeLink();
		String t6 = tef.createTransitionEvent("g4", LogicValue.ZERO, 10, 100, link6);
		link4.add(t6);
		
		s.schedule(t1);
		s.schedule(t2);
		s.schedule(t3);
		s.schedule(t4);
		s.schedule(t5);
		s.schedule(t6);
		
		int i = 0;
		while (i<1000 && !s.isEmpty()){
			System.out.println("Step "+i);
			String event_id = s.getNextTransitionEventId();
			TransitionEvent event = tef.getTransitionEvent(event_id);
			String gate_id = event.getGateId();
			Gate gate = gf.getGate(gate_id);
			System.out.println("\tEvent \""+event_id+"\" has been descheduled.  Gate \""+gate.getGateId()+", "+gate.getGateName()+"\" has value "+gate.getCurrentLogicValue().toString());
			System.out.println("\tEvent \""+event_id+"\" has causative link:");
			CausativeLink link = tef.getTransitionEvent(event_id).getCausativeLink();
			for (String e_id:link.getTransitionEventIds()){
				TransitionEvent e = tef.getTransitionEvent(e_id);
				System.out.println("\t\t"+e.getGateId()+" changes to "+e.getNewValue());
			}
			ArrayList<String> event_scheduled_ids = p.process(event_id);
			System.out.println("\tEvent \""+event_id+"\" has been processed.  Gate \""+gate.getGateId()+", "+gate.getGateName()+"\" has value "+gate.getCurrentLogicValue().toString());
			System.out.println("\tNew events scheduled: ");
			for (String new_event_id:event_scheduled_ids){
				System.out.println("\t\t"+new_event_id);
			}
			System.out.println("\tScheduler size = "+s.getTotalNumberOfEventsOnScheduler());
			i++;
		}
		
		cm.clear();
		sm.clear();
	}
	
//	@Test
	public void testEnvGate() throws IdNotExistException, IdExistException, SchedulerEmptyException, InvalidPropertyKeyException{
		try {
			TestInitialiser.env_gate();
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MaxNumberOfPortsOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		b.build(cm);
		System.out.println("==========GateFactory dump==========");
		System.out.println(gf.dump());
		
		// Set initial values for gates
		Gate g0, g1;
		try {

			g0 = gf.getGate("g0");
			g1 = gf.getGate("g1");

			g0.setCurrentLogicValue(LogicValue.ONE);
			g1.setCurrentLogicValue(LogicValue.ZERO);

			// Initialize the scheduler
			s.initialize();
		} catch (IdNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//====================================================
		// Test
		//====================================================
		
		CausativeLink link1 = new CausativeLink();
		String t1 = tef.createTransitionEvent("g0", LogicValue.X, 5, 100, link1);
		link1.add(t1);
		CausativeLink link2 = new CausativeLink();
		String t2 = tef.createTransitionEvent("g0", LogicValue.ZERO, 10, 100, link2);
		link1.add(t2);
		
		s.schedule(t1);
		s.schedule(t2);
		
		int i = 0;
		while (i<3000 && !s.isEmpty()){
			System.out.println("Step "+i);
			String event_id = s.getNextTransitionEventId();
			TransitionEvent event = tef.getTransitionEvent(event_id);
			String gate_id = event.getGateId();
//			Gate gate = gf.getGate(gate_id);
//			System.out.println("\tEvent \""+event_id+"\" has been descheduled.  Gate \""+gate.getGateId()+", "+gate.getGateName()+"\" has value "+gate.getCurrentLogicValue().toString());
			ArrayList<String> event_scheduled_ids = p.process(event_id);
			System.out.println(tef.getSize());
//			System.out.println("\tEvent \""+event_id+"\" has been processed.  Gate \""+gate.getGateId()+", "+gate.getGateName()+"\" has value "+gate.getCurrentLogicValue().toString());
//			System.out.println("\tNew events scheduled: ");
//			for (String new_event_id:event_scheduled_ids){
//				System.out.println("\t\t"+new_event_id);
//			}
//			System.out.println("\tScheduler size = "+s.getTotalNumberOfEventsOnScheduler());
			i++;
		}
//		System.out.println(((Protocol) (gf.getGate("g1").getElement().getProperty().getProperty(ElementPropertyKey.PROTOCOL))).getScript().historyToString());
	}
	
	//TODO unfinished
//	@Test
	public void simpleSmallCircuitWithEnv() throws IdNotExistException, IdExistException, SchedulerEmptyException{
		//====================================================
		// Setup
		//====================================================
		try {
			TestInitialiser.simple_gates_small();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MaxNumberOfPortsOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		b.build(cm);
		System.out.println("==========GateFactory dump==========");
		System.out.println(gf.dump());

		/*
		 * g2|xor0|ip:(g2):5, ip:(g2):6|op:(g2):2
		 * g1|or0|ip:(g1):2, ip:(g1):3, ip:(g1):4|op:(g1):1
		 * g0|and0|ip:(g0):0, ip:(g0):1|op:(g0):0
		 * g6|input2||op:(g6):5
		 * g5|input1||op:(g5):4
		 * g4|input0||op:(g4):3
		 * g3|nand0||null
		 */

		// Set initial values for gates
		Gate g0, g1, g2, g3, g4, g5, g6;
		try {
			g2 = gf.getGate("g2");
			g1 = gf.getGate("g1");
			g0 = gf.getGate("g0");
			g6 = gf.getGate("g6");
			g5 = gf.getGate("g5");
			g4 = gf.getGate("g4");
			g3 = gf.getGate("g3");

			g2.setCurrentLogicValue(LogicValue.ONE);
			g1.setCurrentLogicValue(LogicValue.ZERO);
			g0.setCurrentLogicValue(LogicValue.ZERO);
			g6.setCurrentLogicValue(LogicValue.ONE);
			g5.setCurrentLogicValue(LogicValue.ZERO);
			g4.setCurrentLogicValue(LogicValue.ONE);
			g3.setCurrentLogicValue(LogicValue.ONE);
			
			// Initialize the scheduler
			s.initialize();
		} catch (IdNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//====================================================
		// Test
		//====================================================
		
		CausativeLink link1 = new CausativeLink();
		String t1 = tef.createTransitionEvent("g6", LogicValue.X, 5, 100, link1);
		link1.add(t1);
		CausativeLink link2 = new CausativeLink();
		String t2 = tef.createTransitionEvent("g5", LogicValue.X, 6, 100, link2);
		link2.add(t2);
		CausativeLink link3 = new CausativeLink();
		String t3 = tef.createTransitionEvent("g4", LogicValue.X, 7, 100, link3);
		link3.add(t3);
		CausativeLink link4 = new CausativeLink();
		String t4 = tef.createTransitionEvent("g6", LogicValue.ZERO, 6, 100, link4);
		link4.add(t4);
		CausativeLink link5 = new CausativeLink();
		String t5 = tef.createTransitionEvent("g5", LogicValue.ONE, 9, 100, link5);
		link4.add(t5);
		CausativeLink link6 = new CausativeLink();
		String t6 = tef.createTransitionEvent("g4", LogicValue.ZERO, 10, 100, link6);
		link4.add(t6);
		
		s.schedule(t1);
		s.schedule(t2);
		s.schedule(t3);
		s.schedule(t4);
		s.schedule(t5);
		s.schedule(t6);
		
		int i = 0;
		while (i<1000 && !s.isEmpty()){
			System.out.println("Step "+i);
			String event_id = s.getNextTransitionEventId();
			TransitionEvent event = tef.getTransitionEvent(event_id);
			String gate_id = event.getGateId();
			Gate gate = gf.getGate(gate_id);
			System.out.println("\tEvent \""+event_id+"\" has been descheduled.  Gate \""+gate.getGateId()+", "+gate.getGateName()+"\" has value "+gate.getCurrentLogicValue().toString());
			ArrayList<String> event_scheduled_ids = p.process(event_id);
			System.out.println("\tEvent \""+event_id+"\" has been processed.  Gate \""+gate.getGateId()+", "+gate.getGateName()+"\" has value "+gate.getCurrentLogicValue().toString());
			System.out.println("\tNew events scheduled: ");
			for (String new_event_id:event_scheduled_ids){
				System.out.println("\t\t"+new_event_id);
			}
			System.out.println("\tScheduler size = "+s.getTotalNumberOfEventsOnScheduler());
			i++;
		}
		
		cm.clear();
		sm.clear();
	}
	
	@Test
	public void testCGate() throws IdNotExistException, IdExistException, SchedulerEmptyException, InvalidPropertyKeyException{
		try {
			TestInitialiser.c_gate();
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MaxNumberOfPortsOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		b.build(cm);
		System.out.println("==========GateFactory dump==========");
		System.out.println(gf.dump());
		
//		==========GateFactory dump==========
//			g2|input0||op:(g2):2
//			g1|not1|ip:(g1):1|op:(g1):1
//			g0|not0|ip:(g0):0|op:(g0):0
//			g4|output0|ip:(g4):4|null
//			g3|c0|ip:(g3):2, ip:(g3):3|op:(g3):3
		
		// Set initial values for gates
		Gate g0, g1, g2, g3, g4;
		try {

			g2 = gf.getGate("g2");
			g1 = gf.getGate("g1");
			g0 = gf.getGate("g0");
			g4 = gf.getGate("g4");
			g3 = gf.getGate("g3");

			g0.setCurrentLogicValue(LogicValue.ONE);
			g1.setCurrentLogicValue(LogicValue.ZERO);
			g2.setCurrentLogicValue(LogicValue.ZERO);
			g3.setCurrentLogicValue(LogicValue.ONE);
			g4.setCurrentLogicValue(LogicValue.ONE);
			// Initialize the scheduler
			s.initialize();
		} catch (IdNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//====================================================
		// Test
		//====================================================
		
		CausativeLink link1 = new CausativeLink();
		String t1 = tef.createTransitionEvent("g2", LogicValue.X, 10, 100, link1);
		link1.add(t1);
		CausativeLink link2 = new CausativeLink();
		String t2 = tef.createTransitionEvent("g2", LogicValue.ONE, 10, 100, link2);
		link1.add(t2);
		
		s.schedule(t1);
		s.schedule(t2);
		
		int i = 0;
		while (i<3000 && !s.isEmpty()){
			System.out.println("Step "+i);
			String event_id = s.getNextTransitionEventId();
			TransitionEvent event = tef.getTransitionEvent(event_id);
			String gate_id = event.getGateId();
			Gate gate = gf.getGate(gate_id);
			System.out.println("\tEvent \""+event_id+"\" has been descheduled.  Gate \""+gate.getGateId()+", "+gate.getGateName()+"\" has value "+gate.getCurrentLogicValue().toString());
			System.out.println("\t Before processing, hard set list contains:");
			for (String str:p.getHardSetList()){
				System.out.println("\t\t"+str);
			}
			ArrayList<String> event_scheduled_ids = p.process(event_id);
			System.out.println("\t[TransitionEventFactory has size = "+tef.getSize()+"]");
			System.out.println("\tEvent \""+event_id+"\" has been processed.  Gate \""+gate.getGateId()+", "+gate.getGateName()+"\" has value "+gate.getCurrentLogicValue().toString());
			System.out.println("\tNew events scheduled: ");
			for (String new_event_id:event_scheduled_ids){
				System.out.println("\t\t"+new_event_id);
			}
			System.out.println("\t After processing, hard set list contains:");
			for (String str:p.getHardSetList()){
				System.out.println("\t\t"+str);
			}
			System.out.println("\tScheduler size = "+s.getTotalNumberOfEventsOnScheduler());
			i++;
		}
	}
}
