package com.hpe.cmwa.util;

import java.text.DecimalFormat;

public class HelperDouble {

	static DecimalFormat df = new DecimalFormat("#.00");

	public static double formatTwoPoint(double tp) {

		String strTp = df.format(tp);

		return Double.parseDouble(strTp);
	}

	public static void main(String[] args) {
		double a = 22d;
		double b = 22.343d;
		double c = 34.34834d;
		double d = 36666.6666d;

		System.out.println(formatTwoPoint(a));
		System.out.println(formatTwoPoint(b));
		System.out.println(formatTwoPoint(c));
		System.out.println(formatTwoPoint(d));
		System.out.println(formatTwoPoint(3931.231d));
	}

}
