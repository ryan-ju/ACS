/**
 * 
 */
package com.asys.simulator;

import java.util.Scanner;

/**
 * @author ryan
 *
 */
public class ParserExperiement {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		while (true){
			String input = in.nextLine();
			AbstractScript script = AbstractScript.parse(input);
			while (script.getNextPairLogging()!=null){
				// loop
			}
			System.out.println(script.historyToString());
			System.out.println();
			System.out.println();
		}
	}

}
