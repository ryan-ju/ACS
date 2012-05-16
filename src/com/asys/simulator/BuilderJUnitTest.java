package com.asys.simulator;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.TestInitialiser;
import com.asys.simulator.exceptions.IdNotExistException;

public class BuilderJUnitTest {
	static CircuitManager cm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestInitialiser.initialise();
		cm = CircuitManager.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuild() {
		Builder b = Builder.getInstance();
		b.build(cm);

		GateFactory gf = GateFactory.getInstance();
		PortFactory pf = PortFactory.getInstance();

		System.out.println("Actual number of elements = "
				+ cm.getElementManager().getElements().size());
		System.out.println("Simulator model has number of gate = "
				+ gf.getGateIds().size());
		System.out.println();

		for (String gate_id : gf.getGateIds()) {
			Gate gate = null;
			try {
				gate = gf.getGate(gate_id);
			} catch (IdNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(gate.getGateId() + ", " + gate.getGateName());
			System.out.print("\tInputting from: ");
			/*
			 * Print out a sequence of [a, b, c], where a = input port ID i, b = the
			 * output port ID o associated with i, c = the gate ID g associated
			 * with o
			 */
			for (String ip_id : gate.getInputPortIds()) {
				try {
					System.out.print("[" + ip_id + ", "
							+ pf.getInputPort(ip_id).getOutputPortId() + ", ");
					System.out.print(pf.getOutputPort(
							pf.getInputPort(ip_id).getOutputPortId())
							.getGateId()
							+ "] ");
				} catch (IdNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println();
			System.out.print("\tOutputting to: ");
			String op_id = gate.getOutputPortId();
			if (op_id != null) {
				List<String> ip_ids = null;
				try {
					ip_ids = pf.getOutputPort(op_id).getInputPortIds();
				} catch (IdNotExistException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				/*
				 * Print out a sequence of [a, b, c], where a = output port ID o, b = the
				 * input port ID i associated with o, c = the gate ID g associated
				 * with i
				 */
				for (String ip_idd : ip_ids) {
					try {
						System.out.print("[" + op_id + ", ");
						System.out.print(ip_idd + ", ");
						System.out.print(pf.getInputPort(ip_idd).getGateId()
								+ "] ");
					} catch (IdNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println();
		}
	}

}
