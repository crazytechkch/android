package com.crazytech.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.crazytech.commons.R;

import android.content.Context;
import android.widget.Toast;

public class Util {
	public static Integer randomNumbers(Integer count) {
		List<String> numbers = new ArrayList<String>();
		numbers.add("0");numbers.add("3");numbers.add("5");numbers.add("7");numbers.add("9");
		numbers.add("1");numbers.add("4");numbers.add("6");numbers.add("8");
		numbers.add("2");
		String randomStr = "";
		for (int i = 0; i < count; i++) {
			Collections.shuffle(numbers);
			randomStr += numbers.get(0);
		}
		return Integer.valueOf(randomStr);
	}

	public static String randomCharacters (Integer count) {
		List<String> chars = new ArrayList<String>();
		chars.add("0");chars.add("3");chars.add("5");chars.add("7");chars.add("9");
		chars.add("1");chars.add("4");chars.add("6");chars.add("8");
		chars.add("2");
		chars.add("a");chars.add("b");chars.add("c");chars.add("d");chars.add("e");
		chars.add("f");chars.add("g");chars.add("h");chars.add("i");chars.add("j");
		chars.add("k");chars.add("l");chars.add("m");chars.add("n");chars.add("o");
		chars.add("p");chars.add("q");chars.add("r");chars.add("s");chars.add("t");
		chars.add("u");chars.add("v");chars.add("w");chars.add("x");chars.add("y");
		chars.add("z");
		chars.add("A");chars.add("B");chars.add("C");chars.add("D");chars.add("E");
		chars.add("F");chars.add("G");chars.add("H");chars.add("I");chars.add("J");
		chars.add("K");chars.add("L");chars.add("M");chars.add("N");chars.add("O");
		chars.add("P");chars.add("Q");chars.add("R");chars.add("S");chars.add("T");
		chars.add("U");chars.add("V");chars.add("W");chars.add("X");chars.add("Y");
		chars.add("Z");
		String randomStr = "";
		for (int i = 0; i < count; i++) {
			Collections.shuffle(chars);
			randomStr += chars.get(0);
		}
		return randomStr;
	}
	
}
