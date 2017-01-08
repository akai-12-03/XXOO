package com.dept.web.general.util;

public class EncodeSwitch {
	
    private static String currentCode="UTF-8";

	public static String getCurrentCode() {
		return currentCode;
	}

	public static void setCurrentCode(String currentCode) {
		EncodeSwitch.currentCode = currentCode;
	}
    
}
