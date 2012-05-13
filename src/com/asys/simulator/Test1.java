package com.asys.simulator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "%Hello%%World%%How%%Are%%You%";
		Pattern root_pattern = Pattern.compile("%\\w*%"); // Eg, %var1%
		Matcher m = root_pattern.matcher(str);
//		boolean b = m.matches();
//		System.out.println(b);
		m.find();
		m.find();
		System.out.println(m.start());
		System.out.println(m.end());
		System.out.println(m.group());
	}

}
