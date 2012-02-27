/**
 * 
 */
package com.asys.editor.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;

/**
 * @author ryan
 * 
 */
public class TestElement {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFanout() {
		Fanout fo = new Fanout();
		AndGate and = new AndGate();
		try {
			fo.setMaxOPs(5);
			assert fo.getMaxOPs() == 5;
		} catch (MaxNumberOfPortsOutOfBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Outport op3;
		try {
			op3 = fo.getOutport(3);
			Outport op4 = fo.getOutport(4);
			op3.setWire(new Wire(op3, and.getInport(0), null));
			op4.setWire(new Wire(op4, and.getInport(1), null));
			try {
				fo.setMaxOPs(1);
			} catch (MaxNumberOfPortsOutOfBoundException e) {
				System.out
						.println("Detected max number out of bound.  Current fo's # wired outports = "
								+ fo.getNumWiredOPs());
			}
			fo.setMaxOPs(2);
			Outport op0 = fo.getOutport(0);
			Outport op1 = fo.getOutport(1);
			assert op0.getWire() != null;
			assert op1.getWire() != null;
			assert fo.getOutports().size() == 2;
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MaxNumberOfPortsOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testAndGate() {
		AndGate a1, a2, a3, a4;
		InputGate i1, i2, i3, i4, i5, i6, i7, i8;
		Wire w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12;
		Fanout f1;
		a1 = new AndGate();
		a2 = new AndGate();
		a3 = new AndGate();
		a4 = new AndGate();
		i1 = new InputGate();
		i2 = new InputGate();
		i3 = new InputGate();
		i4 = new InputGate();
		i5 = new InputGate();
		i6 = new InputGate();
		i7 = new InputGate();
		i8 = new InputGate();
		f1 = new Fanout();
		try {
			a3.setMaxIPs(3);
			a4.setMaxIPs(10);
		} catch (MaxNumberOfPortsOutOfBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			w1 = new Wire(i1.getOutport(0), a1.getInport(0), null);
			w2 = new Wire(i2.getOutport(0), a1.getInport(1), null);
			w3 = new Wire(i3.getOutport(0), a2.getInport(0), null);
			w4 = new Wire(i4.getOutport(0), a2.getInport(1), null);
			w5 = new Wire(i5.getOutport(0), a4.getInport(1), null);
			w6 = new Wire(i6.getOutport(0), f1.getInport(0), null);
			w7 = new Wire(a1.getOutport(0), a3.getInport(0), null);
			w8 = new Wire(a2.getOutport(0), a3.getInport(2), null);
			w9 = new Wire(a3.getOutport(0), a4.getInport(0), null);
			w10 = new Wire(f1.getOutport(0), a4.getInport(2), null);
			w11 = new Wire(f1.getOutport(1), a4.getInport(3), null);
			w12 = new Wire(a4.getOutport(0), a3.getInport(1), null);
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			a3.setMaxIPs(5);
			a3.setMaxIPs(3);
		} catch (MaxNumberOfPortsOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			a4.setMaxIPs(3);
		} catch (MaxNumberOfPortsOutOfBoundException e) {
			System.out.println("Detected max # inports out of bound.  Current a4's # wire inports = "+a4.getNumWiredIPs());
		}
		
	}

}
