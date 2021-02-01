package com.jenkin.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IDGenerator {

	public final static int idlen = 32;
	static Random random;
	static {
		random = new Random(System.currentTimeMillis());
	}

	public synchronized static String newGUID() {
		String dateStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String randomStr = Math.abs(random.nextLong()) + "";
		String s = dateStr + randomStr;
		int len = idlen - s.length();
		for (int i = 0; i < len; i++)
			s += "0";
		if (s.length() > idlen)
			s = s.substring(0, idlen);
		return s;
	}

	public synchronized static String newNo(String prefix) {
		return prefix + newGUID().substring(prefix.length());
	}


}
