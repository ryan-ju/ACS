package com.asys.simulator;

import org.testng.annotations.Test;

import com.asys.editor.model.TestInitialiser;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;

public class InitialConfigurationParserTestNGTest {
 
	@Test
	public void test() throws PortNumberOutOfBoundException, MaxNumberOfPortsOutOfBoundException{
		TestInitialiser.simple_gates_small();
		String str = "g1 0 \n g2 1 \n g3 x \n g4 x \n g5 1 \n g6 0";
		System.out.println(InitialConfigurationParser.visualize(InitialConfigurationParser.parse(str)));
	}
}
