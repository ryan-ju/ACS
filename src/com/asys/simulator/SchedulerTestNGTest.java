package com.asys.simulator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.asys.constants.LogicValue;
import com.asys.editor.model.AndGate;
import com.asys.editor.model.NandGate;
import com.asys.editor.model.NorGate;
import com.asys.editor.model.OrGate;
import com.asys.editor.model.XorGate;
import com.asys.simulator.exceptions.IdExistException;
import com.asys.simulator.exceptions.IdNotExistException;
import com.asys.simulator.exceptions.InvalidElementException;
import com.asys.simulator.exceptions.SchedulerEmptyException;

public class SchedulerTestNGTest {
	Scheduler scheduler;
	GateFactory gf;
	TransitionEventFactory tef;
	String and1_id, or1_id, nand1_id, nor1_id, xor1_id;
	String t1, t2, t3, t4, t5, t6, t7;

	@BeforeClass
	public void setup() {
		scheduler = Scheduler.getInstance();
		gf = GateFactory.getInstance();
		tef = TransitionEventFactory.getInstance();

		try {
			and1_id = gf.createGate(new AndGate());
			or1_id = gf.createGate(new OrGate());
			nand1_id = gf.createGate(new NandGate());
			nor1_id = gf.createGate(new NorGate());
			xor1_id = gf.createGate(new XorGate());
		} catch (InvalidElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scheduler.initialize();
	}

	@Test
	public void scheduleFirst() {
		System.out.println("=========Schedule test one==========");
		String t1 = tef.createTransitionEvent(and1_id, LogicValue.X, 10, 0,
				null);
		String t2 = tef.createTransitionEvent(nand1_id, LogicValue.X, 20, 1,
				null);
		String t3 = tef.createTransitionEvent(and1_id, LogicValue.ONE, 30, 2,
				null);
		String t4 = tef.createTransitionEvent(nor1_id, LogicValue.X, 40, 5,
				null);
		String t5 = tef.createTransitionEvent(and1_id, LogicValue.X, 20, 4,
				null);
		String t6 = tef
				.createTransitionEvent(or1_id, LogicValue.X, 50, 6, null);
		String t7 = tef.createTransitionEvent(or1_id, LogicValue.ZERO, 60, 7,
				null);
		try {
			scheduler.schedule(t1);
			System.out.println(t1+" scheduled");
			scheduler.schedule(t2);
			System.out.println(t2+" scheduled");
			scheduler.schedule(t3);
			System.out.println(t3+" scheduled");
			scheduler.schedule(t4);
			System.out.println(t4+" scheduled");
			scheduler.schedule(t5);
			System.out.println(t5+" scheduled");
			scheduler.schedule(t6);
			System.out.println(t6+" scheduled");
			scheduler.schedule(t7);
			System.out.println(t7+" scheduled");
		} catch (IdNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
		
		try {
			while (!scheduler.isEmpty()){
				String e1 = scheduler.getNextTransitionEventId();
				System.out.println(e1);
			}
		} catch (IdNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SchedulerEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
	}
	
	@Test
	public void scheduleSecond() {
		System.out.println("=========Schedule test one==========");
		String t1 = tef.createTransitionEvent(and1_id, LogicValue.X, 10, 0,
				null);
		String t2 = tef.createTransitionEvent(nand1_id, LogicValue.X, 20, 1,
				null);
		String t3 = tef.createTransitionEvent(and1_id, LogicValue.ONE, 30, 2,
				null);
		String t4 = tef.createTransitionEvent(nor1_id, LogicValue.X, 40, 5,
				null);
		String t5 = tef.createTransitionEvent(and1_id, LogicValue.X, 20, 4,
				null);
		String t6 = tef
				.createTransitionEvent(or1_id, LogicValue.X, 50, 6, null);
		String t7 = tef.createTransitionEvent(or1_id, LogicValue.ZERO, 60, 7,
				null);
		try {
			String e;
			
			scheduler.schedule(t1);
			System.out.println(t1+" scheduled");
			scheduler.schedule(t2);
			System.out.println(t2+" scheduled");
			scheduler.schedule(t3);
			System.out.println(t3+" scheduled");
			scheduler.schedule(t4);
			System.out.println(t4+" scheduled");
			e = scheduler.getNextTransitionEventId();
			System.out.println(e+" is descheduled");
			scheduler.schedule(t5);
			System.out.println(t5+" scheduled");
			e = scheduler.getNextTransitionEventId();
			System.out.println(e+" is descheduled");
			scheduler.schedule(t6);
			System.out.println(t6+" scheduled");
			scheduler.schedule(t7);
			System.out.println(t7+" scheduled");
			e = scheduler.getNextTransitionEventId();
			System.out.println(e+" is descheduled");
			e = scheduler.getNextTransitionEventId();
			System.out.println(e+" is descheduled");
			e = scheduler.getNextTransitionEventId();
			System.out.println(e+" is descheduled");
		} catch (IdNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SchedulerEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
