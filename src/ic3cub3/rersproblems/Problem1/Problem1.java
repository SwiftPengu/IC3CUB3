package ic3cub3.rersproblems.Problem1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class Problem1 {
	public Random random = new Random();
	static BufferedReader stdin = new BufferedReader(new InputStreamReader(
			System.in));

	private String[] inputs = { "A", "B", "E", "D", "C" };

	public static int a160 = 3;
	public static int a61 = 8;
	public static int a58 = 4;
	public static int a120 = 2;
	public static int a23 = 16;
	public static int a172 = 11;
	public static int a138 = 5;
	public static int a161 = 12;
	public static int a92 = 10;
	public static int a95 = 9;
	public static int a145 = 9;
	public static int a197 = 14;
	public static int a75 = 14;
	public static int a186 = 2;
	public static int a52 = 9;
	public static int a113 = 12;
	public static int a3 = 16;
	public static int a38 = 2;
	public static int a143 = 10;
	public static int a97 = 11;
	public static int a64 = 10;
	public static int a25 = 8;
	public static int a194 = 15;
	public static int a119 = 13;
	public static int a9 = 11;
	public static int a49 = 7;
	public static int a96 = 6;
	public static int a43 = 11;
	public static int a72 = 12;
	public static int a192 = 4;
	public static int a80 = 4;
	public static int a171 = 7;
	public static int a54 = 5;
	public static int a59 = 3;
	public static int a63 = 8;
	public static int a89 = 7;
	public static int a134 = 3;
	public static int a34 = 11;
	public static int a94 = 4;
	public static int a149 = 4;
	public static boolean cf = true;
	public static int a184 = 7;
	public static int a180 = 3;

	private void errorCheck() {
		if ((((a94 == 6) && (a161 == 10)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_0");
		}
		if ((((a172 == 11) && (a94 == 7)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_1");
		}
		if ((((a192 == 6) && (a75 == 11)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_2");
		}
		if ((((a34 == 5) && (a145 == 8)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_3");
		}
		if ((((a89 == 8) && (a75 == 14)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_4");
		}
		if ((((a96 == 4) && (a113 == 7)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_5");
		}
		if ((((a119 == 8) && (a94 == 6)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_6");
		}
		if ((((a119 == 12) && (a94 == 6)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_7");
		}
		if ((((a9 == 9) && (a194 == 8)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_8");
		}
		if ((((a89 == 6) && (a59 == 5)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_9");
		}
		if ((((a89 == 11) && (a75 == 14)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_10");
		}
		if ((((a61 == 4) && (a161 == 13)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_11");
		}
		if ((((a54 == 5) && (a161 == 9)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_12");
		}
		if ((((a192 == 3) && (a75 == 11)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_13");
		}
		if ((((a184 == 1) && (a113 == 9)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_14");
		}
		if ((((a52 == 9) && (a113 == 8)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_15");
		}
		if ((((a143 == 14) && (a59 == 8)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_16");
		}
		if ((((a95 == 9) && (a161 == 14)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_17");
		}
		if ((((a49 == 8) && (a145 == 5)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_18");
		}
		if ((((a186 == 1) && (a94 == 2)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_19");
		}
		if ((((a58 == 8) && (a194 == 10)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_20");
		}
		if ((((a94 == 2) && (a194 == 13)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_21");
		}
		if ((((a52 == 6) && (a94 == 5)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_22");
		}
		if ((((a138 == 5) && (a113 == 6)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_23");
		}
		if ((((a138 == 6) && (a113 == 6)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_24");
		}
		if ((((a72 == 16) && (a113 == 13)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_25");
		}
		if ((((a161 == 11) && (a194 == 14)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_26");
		}
		if ((((a161 == 14) && (a75 == 9)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_27");
		}
		if ((((a94 == 3) && (a194 == 13)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_28");
		}
		if ((((a120 == 6) && (a197 == 16)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_29");
		}
		if ((((a23 == 14) && (a194 == 11)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_30");
		}
		if ((((a96 == 7) && (a113 == 7)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_31");
		}
		if ((((a52 == 7) && (a197 == 13)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_32");
		}
		if ((((a180 == 8) && (a145 == 3)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_33");
		}
		if ((((a89 == 6) && (a145 == 9)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_34");
		}
		if ((((a120 == 4) && (a94 == 1)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_35");
		}
		if ((((a119 == 13) && (a94 == 6)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_36");
		}
		if ((((a61 == 7) && (a161 == 13)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_37");
		}
		if ((((a61 == 3) && (a75 == 15)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_38");
		}
		if ((((a120 == 3) && (a194 == 9)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_39");
		}
		if ((((a94 == 7) && (a194 == 13)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_40");
		}
		if ((((a184 == 6) && (a145 == 7)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_41");
		}
		if ((((a63 == 4) && (a59 == 2)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_42");
		}
		if ((((a61 == 10) && (a75 == 15)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_43");
		}
		if ((((a58 == 7) && (a113 == 11)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_44");
		}
		if ((((a3 == 15) && (a59 == 1)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_45");
		}
		if ((((a58 == 2) && (a194 == 10)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_46");
		}
		if ((((a25 == 13) && (a161 == 11)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_47");
		}
		if ((((a72 == 16) && (a113 == 10)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_48");
		}
		if ((((a149 == 3) && (a197 == 15)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_49");
		}
		if ((((a171 == 4) && (a145 == 2)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_50");
		}
		if ((((a184 == 7) && (a145 == 7)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_51");
		}
		if ((((a80 == 5) && (a145 == 6)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_52");
		}
		if ((((a94 == 4) && (a194 == 13)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_53");
		}
		if ((((a172 == 10) && (a197 == 14)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_54");
		}
		if ((((a34 == 11) && (a145 == 8)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_55");
		}
		if ((((a92 == 7) && (a161 == 12)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_56");
		}
		if ((((a120 == 7) && (a94 == 1)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_57");
		}
		if ((((a96 == 7) && (a59 == 3)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_58");
		}
		if ((((a145 == 3) && (a197 == 10)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_59");
		}
		if ((((a43 == 10) && (a59 == 6)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_60");
		}
		if ((((a72 == 9) && (a113 == 10)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_61");
		}
		if ((((a72 == 14) && (a113 == 10)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_62");
		}
		if ((((a134 == 6) && (a94 == 3)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_63");
		}
		if ((((a72 == 10) && (a113 == 10)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_64");
		}
		if ((((a120 == 2) && (a94 == 1)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_65");
		}
		if ((((a134 == 7) && (a59 == 4)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_66");
		}
		if ((((a58 == 6) && (a194 == 10)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_67");
		}
		if ((((a96 == 2) && (a113 == 7)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_68");
		}
		if ((((a38 == 6) && (a75 == 10)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_69");
		}
		if ((((a180 == 7) && (a145 == 3)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_70");
		}
		if ((((a43 == 12) && (a59 == 6)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_71");
		}
		if ((((a38 == 7) && (a75 == 10)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_72");
		}
		if ((((a61 == 3) && (a161 == 13)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_73");
		}
		if ((((a25 == 11) && (a197 == 11)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_74");
		}
		if ((((a160 == 4) && (a75 == 13)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_75");
		}
		if ((((a25 == 7) && (a161 == 11)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_76");
		}
		if ((((a61 == 8) && (a161 == 13)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_77");
		}
		if ((((a58 == 4) && (a113 == 11)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_78");
		}
		if ((((a52 == 6) && (a113 == 8)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_79");
		}
		if ((((a171 == 3) && (a145 == 2)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_80");
		}
		if ((((a58 == 3) && (a113 == 11)) && (a97 == 8))) {
			cf = (false);
			throw new IllegalStateException("error_81");
		}
		if ((((a160 == 8) && (a197 == 9)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_82");
		}
		if ((((a25 == 8) && (a161 == 11)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_83");
		}
		if ((((a34 == 4) && (a145 == 8)) && (a97 == 12))) {
			cf = (false);
			throw new IllegalStateException("error_84");
		}
		if ((((a145 == 4) && (a197 == 10)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_85");
		}
		if ((((a120 == 5) && (a94 == 1)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_86");
		}
		if ((((a89 == 9) && (a59 == 5)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_87");
		}
		if ((((a94 == 1) && (a161 == 10)) && (a97 == 10))) {
			cf = (false);
			throw new IllegalStateException("error_88");
		}
		if ((((a145 == 6) && (a197 == 10)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_89");
		}
		if ((((a52 == 4) && (a197 == 13)) && (a97 == 6))) {
			cf = (false);
			throw new IllegalStateException("error_90");
		}
		if ((((a134 == 3) && (a59 == 4)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_91");
		}
		if ((((a89 == 10) && (a75 == 14)) && (a97 == 11))) {
			cf = (false);
			throw new IllegalStateException("error_92");
		}
		if ((((a186 == 2) && (a94 == 2)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_93");
		}
		if ((((a64 == 11) && (a59 == 7)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_94");
		}
		if ((((a134 == 0) && (a94 == 3)) && (a97 == 9))) {
			cf = (false);
			throw new IllegalStateException("error_95");
		}
		if ((((a113 == 6) && (a194 == 15)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_96");
		}
		if ((((a113 == 12) && (a194 == 15)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_97");
		}
		if ((((a58 == 5) && (a194 == 10)) && (a97 == 7))) {
			cf = (false);
			throw new IllegalStateException("error_98");
		}
		if ((((a134 == 1) && (a59 == 4)) && (a97 == 13))) {
			cf = (false);
			throw new IllegalStateException("error_99");
		}
	}

	private void calculateOutputm2(String input) {
		if ((cf && (input.equals("E")))) {
			cf = (false);
			a120 = (6);
			a194 = (9);
			a97 = (7);
			System.out.println("T");
		}
	}

	private void calculateOutputm1(String input) {
		if ((cf && (a145 == 8))) {
			calculateOutputm2(input);
		}
	}

	private void calculateOutputm4(String input) {
		if (((input.equals("D")) && cf)) {
			cf = (false);
			a192 = (8);
			a75 = (11);
			a97 = (11);
			System.out.println("T");
		}
	}

	private void calculateOutputm3(String input) {
		if ((cf && (a172 == 8))) {
			calculateOutputm4(input);
		}
	}

	private void calculateOutputm6(String input) {
		if ((cf && (input.equals("E")))) {
			cf = (false);
			a120 = (6);
			a194 = (9);
			System.out.println("V");
		}
	}

	private void calculateOutputm5(String input) {
		if (((a9 == 5) && cf)) {
			calculateOutputm6(input);
		}
	}

	private void calculateOutputm8(String input) {
		if ((cf && (input.equals("B")))) {
			cf = (false);
			a145 = (8);
			a197 = (10);
			a97 = (6);
			System.out.println("W");
		}
	}

	private void calculateOutputm9(String input) {
		if ((cf && (input.equals("C")))) {
			cf = (false);
			a23 = (15);
			a194 = (11);
			System.out.println("W");
		}
	}

	private void calculateOutputm7(String input) {
		if (((a120 == 6) && cf)) {
			calculateOutputm8(input);
		}
		if (((a120 == 8) && cf)) {
			calculateOutputm9(input);
		}
	}

	private void calculateOutputm11(String input) {
		if (((input.equals("D")) && cf)) {
			cf = (false);
			a134 = (6);
			a94 = (3);
			a97 = (9);
			System.out.println("U");
		}
		if (((input.equals("B")) && cf)) {
			cf = (false);
			a94 = (3);
			a194 = (13);
			System.out.println("W");
		}
		if ((cf && (input.equals("C")))) {
			cf = (false);
			a43 = (10);
			a59 = (6);
			a97 = (13);
			System.out.println("V");
		}
		if (((input.equals("E")) && cf)) {
			cf = (false);
			a119 = (13);
			a94 = (6);
			a97 = (9);
			System.out.println("V");
		}
		if ((cf && (input.equals("A")))) {
			cf = (false);
			a120 = (8);
			a194 = (9);
			System.out.println("T");
		}
	}

	private void calculateOutputm10(String input) {
		if ((cf && (a23 == 15))) {
			calculateOutputm11(input);
		}
	}

	private void calculateOutputm13(String input) {
		if ((cf && (input.equals("E")))) {
			cf = (false);
			a80 = (4);
			a145 = (6);
			a97 = (12);
			System.out.println("T");
		}
		if (((input.equals("B")) && cf)) {
			cf = (false);
			a180 = (7);
			a145 = (3);
			a97 = (12);
			System.out.println("T");
		}
		if (((input.equals("A")) && cf)) {
			cf = (false);
			a171 = (7);
			a145 = (2);
			a97 = (12);
			System.out.println("U");
		}
	}

	private void calculateOutputm12(String input) {
		if ((cf && (a184 == 4))) {
			calculateOutputm13(input);
		}
	}

	private void calculateOutputm15(String input) {
		if (((input.equals("E")) && cf)) {
			cf = (false);
			a143 = (10);
			a59 = (8);
			a97 = (13);
			System.out.println("Y");
		}
		if (((input.equals("B")) && cf)) {
			cf = (false);
			a9 = (5);
			a194 = (8);
			a97 = (7);
			System.out.println("X");
		}
	}

	private void calculateOutputm14(String input) {
		if (((a134 == 7) && cf)) {
			calculateOutputm15(input);
		}
	}

	private void calculateOutputm17(String input) {
		if ((cf && (input.equals("B")))) {
			cf = (false);
			a89 = (6);
			a59 = (5);
			a97 = (13);
			System.out.println("V");
		}
		if ((cf && (input.equals("C")))) {
			cf = (false);
			a94 = (2);
			a194 = (13);
			a97 = (7);
			System.out.println("W");
		}
		if ((cf && (input.equals("E")))) {
			cf = (false);
			a160 = (3);
			a75 = (13);
			a97 = (11);
			System.out.println("T");
		}
		if ((cf && (input.equals("D")))) {
			cf = (false);
			a134 = (3);
			a59 = (4);
			a97 = (13);
			System.out.println("V");
		}
		if (((input.equals("A")) && cf)) {
			cf = (false);
			a113 = (6);
			a194 = (15);
			a97 = (7);
			System.out.println("T");
		}
	}

	private void calculateOutputm18(String input) {
		if (((input.equals("A")) && cf)) {
			cf = (false);
			a184 = (4);
			a113 = (9);
			a97 = (8);
			System.out.println("W");
		}
	}

	private void calculateOutputm16(String input) {
		if ((cf && (a119 == 6))) {
			calculateOutputm17(input);
		}
		if ((cf && (a119 == 11))) {
			calculateOutputm18(input);
		}
	}

	private void calculateOutputm20(String input) {
		if (((input.equals("E")) && cf)) {
			cf = (false);
			a119 = (11);
			a94 = (6);
			a97 = (9);
			System.out.println("Y");
		}
	}

	private void calculateOutputm19(String input) {
		if ((cf && (a61 == 5))) {
			calculateOutputm20(input);
		}
	}

	private void calculateOutputm22(String input) {
		if ((cf && (input.equals("E")))) {
			cf = (false);
			a172 = (8);
			a197 = (14);
			a97 = (6);
			System.out.println("W");
		}
		if (((input.equals("A")) && cf)) {
			cf = (false);
			a172 = (8);
			a197 = (14);
			a97 = (6);
			System.out.println("W");
		}
	}

	private void calculateOutputm21(String input) {
		if ((cf && (a192 == 8))) {
			calculateOutputm22(input);
		}
	}

	private void calculateOutputm24(String input) {
		if (((input.equals("E")) && cf)) {
			cf = (false);
			a49 = (8);
			a145 = (5);
			a97 = (12);
			System.out.println("W");
		}
		if ((cf && (input.equals("A")))) {
			cf = (false);
			a61 = (5);
			a161 = (13);
			a97 = (10);
			System.out.println("V");
		}
		if ((cf && (input.equals("C")))) {
			cf = (false);
			a72 = (16);
			a113 = (13);
			a97 = (8);
			System.out.println("X");
		}
		if ((cf && (input.equals("B")))) {
			cf = (false);
			a134 = (0);
			a94 = (3);
			a97 = (9);
			System.out.println("T");
		}
		if (((input.equals("D")) && cf)) {
			cf = (false);
			a61 = (10);
			a75 = (15);
			System.out.println("W");
		}
	}

	private void calculateOutputm23(String input) {
		if (((a160 == 3) && cf)) {
			calculateOutputm24(input);
		}
	}

	private void calculateOutputm26(String input) {
		if ((cf && (input.equals("D")))) {
			cf = (false);
			a80 = (4);
			a145 = (6);
			a97 = (12);
			System.out.println("U");
		}
		if (((input.equals("C")) && cf)) {
			cf = (false);
			a134 = (7);
			a94 = (3);
			a97 = (9);
			System.out.println("W");
		}
	}

	private void calculateOutputm25(String input) {
		if (((a89 == 7) && cf)) {
			calculateOutputm26(input);
		}
	}

	private void calculateOutputm28(String input) {
		if (((input.equals("C")) && cf)) {
			cf = (false);
			a61 = (5);
			a161 = (13);
			a97 = (10);
			System.out.println("V");
		}
		if ((cf && (input.equals("E")))) {
			cf = (false);
			a184 = (3);
			a145 = (7);
			a97 = (12);
			System.out.println("V");
		}
		if (((input.equals("A")) && cf)) {
			cf = (false);
			a64 = (7);
			a59 = (7);
			a97 = (13);
			System.out.println("Y");
		}
	}

	private void calculateOutputm27(String input) {
		if (((a72 == 14) && cf)) {
			calculateOutputm28(input);
		}
	}

	private void calculateOutputm30(String input) {
		if ((cf && (input.equals("B")))) {
			cf = (false);
			a96 = (7);
			a113 = (7);
			a97 = (8);
			System.out.println("Y");
		}
		if ((cf && (input.equals("D")))) {
			cf = (false);
			a119 = (6);
			a94 = (6);
			a97 = (9);
			System.out.println("U");
		}
		if ((cf && (input.equals("C")))) {
			cf = (false);
			a25 = (11);
			a197 = (11);
			a97 = (6);
			System.out.println("V");
		}
		if (((input.equals("E")) && cf)) {
			cf = (false);
			a186 = (2);
			a94 = (2);
			a97 = (9);
			System.out.println("V");
		}
		if (((input.equals("A")) && cf)) {
			cf = (false);
			a61 = (7);
			a161 = (13);
			a97 = (10);
			System.out.println("U");
		}
	}

	private void calculateOutputm29(String input) {
		if ((cf && (a171 == 7))) {
			calculateOutputm30(input);
		}
	}

	private void calculateOutputm32(String input) {
		if ((cf && (input.equals("E")))) {
			cf = (false);
			a72 = (14);
			a75 = (16);
			a97 = (11);
			System.out.println("X");
		}
	}

	private void calculateOutputm31(String input) {
		if ((cf && (a80 == 4))) {
			calculateOutputm32(input);
		}
	}

	private void calculateOutputm34(String input) {
		if ((cf && (input.equals("D")))) {
			cf = (false);

			System.out.println("Z");
		}
		if ((cf && (input.equals("A")))) {
			cf = (false);
			a80 = (4);
			a145 = (6);
			System.out.println("U");
		}
	}

	private void calculateOutputm33(String input) {
		if (((a184 == 3) && cf)) {
			calculateOutputm34(input);
		}
	}

	private void calculateOutputm36(String input) {
		if ((cf && (input.equals("D")))) {
			cf = (false);
			a120 = (8);
			a194 = (9);
			a97 = (7);
			System.out.println("Z");
		}
	}

	private void calculateOutputm35(String input) {
		if (((a134 == 5) && cf)) {
			calculateOutputm36(input);
		}
	}

	private void calculateOutputm38(String input) {
		if (((input.equals("D")) && cf)) {
			cf = (false);

			System.out.println("U");
		}
		if ((cf && (input.equals("C")))) {
			cf = (false);
			a172 = (8);
			a197 = (14);
			a97 = (6);
			System.out.println("W");
		}
	}

	private void calculateOutputm37(String input) {
		if (((a64 == 7) && cf)) {
			calculateOutputm38(input);
		}
	}

	private void calculateOutputm40(String input) {
		if ((cf && (input.equals("A")))) {
			cf = (false);
			a134 = (5);
			a59 = (4);
			System.out.println("X");
		}
	}

	private void calculateOutputm39(String input) {
		if (((a143 == 10) && cf)) {
			calculateOutputm40(input);
		}
	}

	public void calculateOutput(String input) {
		cf = (true);
		if (((a97 == 6) && cf)) {
			if ((cf && (a197 == 10))) {
				calculateOutputm1(input);
			}
			if (((a197 == 14) && cf)) {
				calculateOutputm3(input);
			}
		}
		if (((a97 == 7) && cf)) {
			if (((a194 == 8) && cf)) {
				calculateOutputm5(input);
			}
			if (((a194 == 9) && cf)) {
				calculateOutputm7(input);
			}
			if ((cf && (a194 == 11))) {
				calculateOutputm10(input);
			}
		}
		if (((a97 == 8) && cf)) {
			if (((a113 == 9) && cf)) {
				calculateOutputm12(input);
			}
		}
		if (((a97 == 9) && cf)) {
			if ((cf && (a94 == 3))) {
				calculateOutputm14(input);
			}
			if (((a94 == 6) && cf)) {
				calculateOutputm16(input);
			}
		}
		if (((a97 == 10) && cf)) {
			if ((cf && (a161 == 13))) {
				calculateOutputm19(input);
			}
		}
		if ((cf && (a97 == 11))) {
			if (((a75 == 11) && cf)) {
				calculateOutputm21(input);
			}
			if ((cf && (a75 == 13))) {
				calculateOutputm23(input);
			}
			if ((cf && (a75 == 14))) {
				calculateOutputm25(input);
			}
			if (((a75 == 16) && cf)) {
				calculateOutputm27(input);
			}
		}
		if (((a97 == 12) && cf)) {
			if (((a145 == 2) && cf)) {
				calculateOutputm29(input);
			}
			if (((a145 == 6) && cf)) {
				calculateOutputm31(input);
			}
			if (((a145 == 7) && cf)) {
				calculateOutputm33(input);
			}
		}
		if (((a97 == 13) && cf)) {
			if ((cf && (a59 == 4))) {
				calculateOutputm35(input);
			}
			if (((a59 == 7) && cf)) {
				calculateOutputm37(input);
			}
			if (((a59 == 8) && cf)) {
				calculateOutputm39(input);
			}
		}

		errorCheck();
		if (cf)
			throw new IllegalArgumentException(
					"Current state has no transition for this input!");
	}

	public static void main(String[] args) throws Exception {
		// init system and input reader
		Problem1 eca = new Problem1();

		// main i/o-loop
		while (true) {
			// read input
			String input = stdin.readLine();
			try {
				// operate eca engine output =
				eca.calculateOutput(input);
			} catch (IllegalArgumentException e) {
				System.err.println("Invalid input: " + e.getMessage());
			}
		}
	}
}