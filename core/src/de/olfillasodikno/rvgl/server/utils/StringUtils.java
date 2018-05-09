package de.olfillasodikno.rvgl.server.utils;

import java.util.Random;

public class StringUtils {
	
	private static final Random rnd = new Random();
	
	private static final String symbols = "ABCDEFGHKPWXY23478";
	
	private StringUtils() {}
	
	public static String random(int length) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< length; i++) {
			sb.append(symbols.charAt(rnd.nextInt(symbols.length())));			
		}
		return sb.toString();
	}
	
}
