/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ryan
 * 
 */
public class AbstractScriptJUnitTest {

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

	
	public void testIsValidValid() {
		String[] strings = new String[] { "<12, 0>", "<100, 1>", "<1112, x>",
				"<2223, X>", "<112, x><999, 0>    <666, x>",
				"loop 10 (<556, 0>     <551, X>)",
				"loop 1 (<112, 1> <11, 1>) loop 2 (<111, 1><112, x>)",
				"<112, x> loop 10 (<112, 1>)",
				"loop 10 (loop 20 (loop 455 (<  111, 1><555, x>loop 3 (<222, 0>)  )))" };
		System.out
				.println("========================================================");
		for (int i = 0; i < strings.length; i++) {
			try {
				assert AbstractScript.isValid(strings[i]);
				System.out.println(strings[i] + " is valid");
				System.out
						.println("---------------------------------------------------");
			} catch (Exception e) {
				System.out.println("String under test is: " + strings[i]);
			}
		}
		System.out
				.println("========================================================");
	}

	
	public void testIsValidInvalid() {
		String[] strings = new String[] { "<12, 2>", "<100, a>", "<1112, b>",
				"<2223, !>", "<112, x>999, 0><666, x>",
				"loop 10 (<556, 0, 551, X>)",
				"lop 1 (<112, 1> <11, 1>) loop 2 (<111, 1><112, x>)",
				"<112, x> loop (<112, 1>)",
				"loop 10 loop 20 (loop 455 (<111, 1><555, x>loop 3(<222, 0>))))" };
		System.out
				.println("========================================================");
		for (int i = 0; i < strings.length; i++) {
			try {
				assert !AbstractScript.isValid(strings[i]);
				System.out.println(strings[i] + " is invalid");
				System.out
						.println("---------------------------------------------------");
			} catch (Exception e) {
				System.out.println("String under test is: " + strings[i]);
			}
		}
		System.out
				.println("========================================================");
	}

	@Test
	public void testParse() {
		ArrayList<Pair<String, String>> testList = new ArrayList<Pair<String, String>>();
		testList.add(new Pair<String, String>("<12, 0>", "<12,0>"));
		testList.add(new Pair<String, String>("<100,   1>", "<100,1>"));
		testList.add(new Pair<String, String>("<101,   x>", "<101,X>"));
		testList.add(new Pair<String, String>("<110,   X>", "<110,X>"));
		testList.add(new Pair<String, String>("loop 3 (<556, 0>     <551, X>)",
				"<556,0><551,X><556,0><551,X><556,0><551,X>"));
		testList.add(new Pair<String, String>("<112, x> loop 7 (<112, 1>)",
				"<112,X><112,1><112,1><112,1><112,1><112,1><112,1><112,1>"));
		testList.add(new Pair<String, String>(
				"loop 3 (loop 2 (loop 3 (<  111, 1><555, x>loop 4 (<222, 0>)  )))",
				"<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"
						+ "<111,1><555,X><222,0><222,0><222,0><222,0>"));
		System.out.println("testList.size()=="+testList.size());
		for (int i = 0; i < testList.size(); i++) {
			AbstractScript script = AbstractScript
					.parse(testList.get(i).input);
			while (script.getNextPairLogging() != null) {
				// loop
			}
			System.out.println("History is:            "+script.historyToString());
			System.out.println("Expectated history is: "+testList.get(i).expectation);
			assert (script.historyToString()
					.equals(testList.get(i).expectation));
			System.out.println("Script \""+testList.get(i).input+"\" passed the test.");
			System.out.println("----------------------------------------------------------");
		}
	}

	class Pair<S, T> {
		S input;
		T expectation;

		Pair(S input, T expectation) {
			this.input = input;
			this.expectation = expectation;
		}
	}
}
