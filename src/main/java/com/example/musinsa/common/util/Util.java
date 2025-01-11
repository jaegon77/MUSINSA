package com.example.musinsa.common.util;

import java.text.NumberFormat;
import java.util.Locale;

public class Util {
	public static String PriceFormatterUtil(int price) {
		return NumberFormat.getNumberInstance(Locale.KOREA).format(price);
	}
}
