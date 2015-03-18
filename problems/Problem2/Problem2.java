import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class Problem2 {
	public Random random = new Random();
	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	private String[] inputs = {"E","A","C","B","D"};

	public static int a83 = 4;
	public static int a87 = 133;
	public static int a193 = 23;
	public static int a132 = 13;
	public static int a200 = -121;
	public static int a81 = 9;
	public static int a182 = 6;
	public static int a168 = 2;
	public static int a80 = 16;
	public static int a196 = 15;
	public static int a19 = 8;
	public static int a136 = 5;
	public static int a78 = 2;
	public static int a41 = 7;
	public static int a39 = 8;
	public static int a51 = 11;
	public static int a181 = 10;
	public static int a59 = 6;
	public static boolean cf = true;
	public static int a1 = 131;
	public static int a133 = 7;
	public static int a152 = 398;
	public static int a47 = -201;
	public static int a0 = 16;
	public static int a178 = 5;
	public static int a118 = 319;
	public static int a142 = 373;
	public static int a153 = 3;
	public static int a112 = 421;
	public static int a96 = 12;
	public static int a134 = 8;
	public static int a71 = 9;
	public static int a18 = 4;
	public static int a110 = 14;
	public static int a23 = 8;
	public static int a62 = 10;
	public static int a50 = 457;
	public static int a116 = -129;
	public static int a6 = 122;
	public static int a89 = 7;
	public static int a91 = 14;
	public static int a173 = 5;
	public static int a155 = 493;
	public static int a99 = 165;
	public static int a194 = 5;
	public static int a55 = 443;
	public static int a174 = -168;
	public static int a138 = 11;
	public static int a198 = 9;


	private void errorCheck() {
	    if((((a41==10) && (a71==12)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_0" );
	    }
	    if((((a134==9) && (a71==10)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_1" );
	    }
	    if(((  ((-5 < a99) && (148 >= a99))  &&  452 < a50 ) && (a110==14))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_2" );
	    }
	    if(((  ((-156 < a174) && (-121 >= a174))  && (a83==4)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_3" );
	    }
	    if((((a89==4) && (a133==11)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_4" );
	    }
	    if((((a96==11) && (a136==4)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_5" );
	    }
	    if((((a181==7) &&  a6 <=  127 ) && (a110==16))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_6" );
	    }
	    if((((a80==16) &&   ((277 < a50) && (452 >= a50)) ) && (a110==14))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_7" );
	    }
	    if((((a80==16) && (a133==10)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_8" );
	    }
	    if((((a81==11) && (a80==13)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_9" );
	    }
	    if((((a198==9) && (a136==7)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_10" );
	    }
	    if((((a153==0) && (a91==7)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_11" );
	    }
	    if((((a182==6) && (a91==13)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_12" );
	    }
	    if((((a89==3) && (a133==11)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_13" );
	    }
	    if((((a0==13) && (a71==13)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_14" );
	    }
	    if((((a23==9) && (a91==8)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_15" );
	    }
	    if((( 366 < a142  && (a91==11)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_16" );
	    }
	    if((((a80==9) &&   ((277 < a50) && (452 >= a50)) ) && (a110==14))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_17" );
	    }
	    if(((  ((86 < a152) && (253 >= a152))  &&   ((92 < a50) && (277 >= a50)) ) && (a110==14))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_18" );
	    }
	    if((( 45 < a200  && (a133==14)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_19" );
	    }
	    if((((a182==8) && (a136==9)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_20" );
	    }
	    if((((a96==10) && (a136==4)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_21" );
	    }
	    if((((a132==15) && (a136==8)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_22" );
	    }
	    if((((a51==7) && (a80==16)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_23" );
	    }
	    if((((a59==8) && (a83==7)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_24" );
	    }
	    if((( 490 < a155  && (a71==9)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_25" );
	    }
	    if((((a198==11) && (a136==7)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_26" );
	    }
	    if((((a23==11) && (a91==8)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_27" );
	    }
	    if((((a182==8) && (a91==13)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_28" );
	    }
	    if((((a62==9) &&   ((127 < a6) && (220 >= a6)) ) && (a110==16))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_29" );
	    }
	    if((((a178==3) && (a71==6)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_30" );
	    }
	    if((((a138==12) && (a136==11)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_31" );
	    }
	    if((((a81==9) && (a80==13)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_32" );
	    }
	    if((((a194==7) && (a83==5)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_33" );
	    }
	    if(((  ((-121 < a174) && (44 >= a174))  && (a83==6)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_34" );
	    }
	    if((((a80==9) && (a133==10)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_35" );
	    }
	    if((((a178==5) && (a71==6)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_36" );
	    }
	    if((((a39==1) && (a83==3)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_37" );
	    }
	    if(((  ((-124 < a116) && (59 >= a116))  && (a80==12)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_38" );
	    }
	    if((((a133==9) && (a80==14)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_39" );
	    }
	    if((( 146 < a193  && (a133==9)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_40" );
	    }
	    if((((a89==7) && (a71==8)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_41" );
	    }
	    if((((a19==8) && (a133==7)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_42" );
	    }
	    if((( a99 <=  -72  &&  452 < a50 ) && (a110==14))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_43" );
	    }
	    if((( 153 < a116  && (a80==12)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_44" );
	    }
	    if((((a78==3) && (a80==15)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_45" );
	    }
	    if((((a80==13) && (a133==10)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_46" );
	    }
	    if((((a194==10) && (a91==14)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_47" );
	    }
	    if((((a18==7) && (a136==5)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_48" );
	    }
	    if((((a178==10) && (a71==6)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_49" );
	    }
	    if((((a39==3) && (a83==3)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_50" );
	    }
	    if((((a194==5) && (a91==14)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_51" );
	    }
	    if((((a41==3) && (a71==12)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_52" );
	    }
	    if((((a18==3) && (a136==5)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_53" );
	    }
	    if((( a47 <=  -186  && (a91==12)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_54" );
	    }
	    if((((a41==9) && (a71==12)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_55" );
	    }
	    if((((a138==13) && (a136==11)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_56" );
	    }
	    if((((a0==12) && (a71==13)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_57" );
	    }
	    if((((a81==10) && (a80==13)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_58" );
	    }
	    if((((a96==9) && (a136==4)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_59" );
	    }
	    if((((a89==5) && (a71==8)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_60" );
	    }
	    if(((  ((95 < a112) && (270 >= a112))  && (a91==9)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_61" );
	    }
	    if((((a96==13) && (a136==4)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_62" );
	    }
	    if((((a132==8) && (a71==7)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_63" );
	    }
	    if(((  ((136 < a87) && (316 >= a87))  && (a80==11)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_64" );
	    }
	    if(((  ((312 < a55) && (432 >= a55))  && (a91==10)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_65" );
	    }
	    if((((a59==5) && (a83==7)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_66" );
	    }
	    if(((  ((127 < a6) && (220 >= a6))  && (a83==2)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_67" );
	    }
	    if((((a182==11) && (a91==13)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_68" );
	    }
	    if((( a112 <=  95  && (a91==9)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_69" );
	    }
	    if(((  ((-121 < a174) && (44 >= a174))  && (a83==4)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_70" );
	    }
	    if((((a196==12) && (a83==1)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_71" );
	    }
	    if((((a173==8) &&  a50 <=  92 ) && (a110==14))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_72" );
	    }
	    if((((a51==10) && (a80==16)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_73" );
	    }
	    if((((a89==8) && (a133==11)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_74" );
	    }
	    if((((a39==7) && (a83==3)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_75" );
	    }
	    if((((a182==5) && (a136==9)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_76" );
	    }
	    if((((a198==7) && (a136==7)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_77" );
	    }
	    if((( a193 <=  -14  && (a133==13)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_78" );
	    }
	    if((((a132==14) && (a71==7)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_79" );
	    }
	    if((( a55 <=  149  && (a91==10)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_80" );
	    }
	    if((((a132==15) && (a71==7)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_81" );
	    }
	    if((((a181==8) &&  a6 <=  127 ) && (a110==16))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_82" );
	    }
	    if((((a39==8) && (a83==3)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_83" );
	    }
	    if(((  ((59 < a116) && (153 >= a116))  && (a80==12)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_84" );
	    }
	    if(((  ((214 < a1) && (319 >= a1))  && (a133==8)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_85" );
	    }
	    if((((a182==4) && (a136==9)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_86" );
	    }
	    if(((  ((-14 < a193) && (45 >= a193))  && (a133==9)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_87" );
	    }
	    if(((  ((-156 < a174) && (-121 >= a174))  && (a83==6)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_88" );
	    }
	    if((((a39==4) && (a83==3)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_89" );
	    }
	    if((((a134==11) && (a71==10)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_90" );
	    }
	    if((((a178==4) && (a71==6)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_91" );
	    }
	    if((((a194==3) && (a83==5)) && (a110==9))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_92" );
	    }
	    if((((a168==9) &&  325 < a6 ) && (a110==16))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_93" );
	    }
	    if((((a178==7) && (a71==6)) && (a110==11))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_94" );
	    }
	    if(((  ((150 < a1) && (214 >= a1))  && (a133==8)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_95" );
	    }
	    if((((a80==14) && (a133==10)) && (a110==15))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_96" );
	    }
	    if((((a198==12) && (a136==7)) && (a110==10))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_97" );
	    }
	    if((((a194==8) && (a91==14)) && (a110==12))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_98" );
	    }
	    if((((a51==13) && (a80==16)) && (a110==13))){
	    	cf = (false);
	    	throw new IllegalStateException( "error_99" );
	    }
	}private  void calculateOutputm2(String input) {
    if(((input.equals("C")) && cf)){
    	 cf = (false);
    	a181 = (13);
    	a6 = (((((((  a6) * a6)% 14999) + -16707) + 27580) / 5) * -5);
    	a110 = (16);
    	  System.out.println(  "Y"  );  
    } 
}private  void calculateOutputm3(String input) {
    if((cf && (input.equals("D")))){
    	 cf = (false);
    	a138 = (14);
    	a136 = (11);
    	a110 = (10);
    	  System.out.println(  "T"  );  
    } 
}private  void calculateOutputm1(String input) {
    if((cf &&   ((220 < a6) && (325 >= a6)) )){
    	calculateOutputm2(input);
    } 
    if((cf &&  325 < a6 )){
    	calculateOutputm3(input);
    } 
}private  void calculateOutputm5(String input) {
    if(((input.equals("C")) && cf)){
    	 cf = (false);
    	a181 = (6);
    	a6 = (((((  a6) % 15063)- 14935) + 0) - 1);
    	a110 = (16);
    	  System.out.println(  "U"  );  
    } 
    if((cf && (input.equals("B")))){
    	 cf = (false);
    	a138 = (14);
    	a136 = (11);
    	a110 = (10);
    	  System.out.println(  "T"  );  
    } 
    if(((input.equals("D")) && cf)){
    	 cf = (false);
    	a138 = (14);
    	a136 = (11);
    	a110 = (10);
    	  System.out.println(  "T"  );  
    } 
}private  void calculateOutputm4(String input) {
    if((cf && (a59==4))){
    	calculateOutputm5(input);
    } 
}private  void calculateOutputm7(String input) {
    if(((input.equals("B")) && cf)){
    	 cf = (false);
    	a89 = (3);
    	a133 = (11);
    	a110 = (15);
    	  System.out.println(  "V"  );  
    } 
    if(((input.equals("C")) && cf)){
    	 cf = (false);
    	a51 = (14);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "Z"  );  
    } 
    if(((input.equals("A")) && cf)){
    	 cf = (false);
    	a173 = (2);
    	a50 = (((((  a50) % 15046)- 14953) - 0) / 5);
    	a110 = (14);
    	  System.out.println(  "T"  );  
    } 
    if(((input.equals("E")) && cf)){
    	 cf = (false);
    	a181 = (13);
    	a6 = (((((  a6) * 1) + 0) / 5) - 19790);
    	a110 = (16);
    	  System.out.println(  "Y"  );  
    } 
    if((cf && (input.equals("D")))){
    	 cf = (false);
    	a132 = (15);
    	a71 = (7);
    	a110 = (11);
    	  System.out.println(  "V"  );  
    } 
}private  void calculateOutputm6(String input) {
    if(((a18==4) && cf)){
    	calculateOutputm7(input);
    } 
}private  void calculateOutputm9(String input) {
    if(((input.equals("C")) && cf)){
    	 cf = (false);
    	a182 = (7);
    	a136 = (9);
    	  System.out.println(  "X"  );  
    } 
    if(((input.equals("A")) && cf)){
    	 cf = (false);

    	  System.out.println(  "U"  );  
    } 
    if(((input.equals("E")) && cf)){
    	 cf = (false);

    	  System.out.println(  "U"  );  
    } 
}private  void calculateOutputm8(String input) {
    if(((a132==10) && cf)){
    	calculateOutputm9(input);
    } 
}private  void calculateOutputm11(String input) {
    if(((input.equals("E")) && cf)){
    	 cf = (false);
    	a80 = (16);
    	a50 = ((((((  a50) + 0) % 87)+ 365) + -25822) - -25822);
    	a110 = (14);
    	  System.out.println(  "U"  );  
    } 
    if((cf && (input.equals("D")))){
    	 cf = (false);
    	a193 = (((((  a193) / 5) % 29)- -16) + -1);
    	a133 = (9);
    	a110 = (15);
    	  System.out.println(  "X"  );  
    } 
    if(((input.equals("B")) && cf)){
    	 cf = (false);

    	  System.out.println(  "Z"  );  
    } 
    if((cf && (input.equals("A")))){
    	 cf = (false);
    	a181 = (13);
    	a6 = ((((  a6) / 5) + -22100) * 1);
    	a110 = (16);
    	  System.out.println(  "Y"  );  
    } 
    if((cf && (input.equals("C")))){
    	 cf = (false);
    	a51 = (7);
    	a80 = (16);
    	a110 = (13);
    	  System.out.println(  "Z"  );  
    } 
}private  void calculateOutputm12(String input) {
    if(((input.equals("A")) && cf)){
    	 cf = (false);
    	a132 = (10);
    	a136 = (8);
    	  System.out.println(  "U"  );  
    } 
}private  void calculateOutputm10(String input) {
    if((cf && (a182==6))){
    	calculateOutputm11(input);
    } 
    if((cf && (a182==7))){
    	calculateOutputm12(input);
    } 
}private  void calculateOutputm14(String input) {
    if((cf && (input.equals("E")))){
    	 cf = (false);
    	a182 = (6);
    	a136 = (9);
    	  System.out.println(  "Z"  );  
    } 
}private  void calculateOutputm15(String input) {
    if((cf && (input.equals("E")))){
    	 cf = (false);
    	a182 = (7);
    	a136 = (9);
    	  System.out.println(  "X"  );  
    } 
    if((cf && (input.equals("B")))){
    	 cf = (false);
    	a181 = (6);
    	a6 = (((((  a6) + 0) % 15063)- 14935) / 5);
    	a110 = (16);
    	  System.out.println(  "U"  );  
    } 
    if((cf && (input.equals("A")))){
    	 cf = (false);
    	a51 = (11);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "V"  );  
    } 
}private  void calculateOutputm13(String input) {
    if((cf && (a138==11))){
    	calculateOutputm14(input);
    } 
    if((cf && (a138==14))){
    	calculateOutputm15(input);
    } 
}private  void calculateOutputm17(String input) {
    if((cf && (input.equals("A")))){
    	 cf = (false);
    	a181 = (13);
    	a6 = ((((((  a6) % 15063)+ -14935) - 0) + 1907) + -1907);
    	a110 = (16);
    	  System.out.println(  "Y"  );  
    } 
    if((cf && (input.equals("C")))){
    	 cf = (false);
    	a51 = (11);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "V"  );  
    } 
}private  void calculateOutputm16(String input) {
    if(((a89==4) && cf)){
    	calculateOutputm17(input);
    } 
}private  void calculateOutputm19(String input) {
    if(((input.equals("B")) && cf)){
    	 cf = (false);
    	a181 = (13);
    	a6 = (((((  a6) % 15063)- 14935) - 2) + -1);
    	a110 = (16);
    	  System.out.println(  "Y"  );  
    } 
    if(((input.equals("A")) && cf)){
    	 cf = (false);
    	a51 = (14);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "Z"  );  
    } 
}private  void calculateOutputm20(String input) {
    if(((input.equals("B")) && cf)){
    	 cf = (false);
    	a181 = (13);
    	a6 = ((((  a6) / 5) - 13384) / 5);
    	a110 = (16);
    	  System.out.println(  "Y"  );  
    } 
    if(((input.equals("E")) && cf)){
    	 cf = (false);
    	a51 = (14);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "Z"  );  
    } 
    if(((input.equals("A")) && cf)){
    	 cf = (false);
    	a81 = (6);
    	a80 = (13);
    	a110 = (13);
    	  System.out.println(  "U"  );  
    } 
}private  void calculateOutputm18(String input) {
    if((cf && (a134==5))){
    	calculateOutputm19(input);
    } 
    if(((a134==10) && cf)){
    	calculateOutputm20(input);
    } 
}private  void calculateOutputm22(String input) {
    if(((input.equals("C")) && cf)){
    	 cf = (false);
    	a51 = (14);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "Z"  );  
    } 
}private  void calculateOutputm21(String input) {
    if((cf && (a23==7))){
    	calculateOutputm22(input);
    } 
}private  void calculateOutputm24(String input) {
    if((cf && (input.equals("D")))){
    	 cf = (false);

    	  System.out.println(  "U"  );  
    } 
    if(((input.equals("A")) && cf)){
    	 cf = (false);
    	a51 = (11);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "V"  );  
    } 
    if((cf && (input.equals("E")))){
    	 cf = (false);

    	  System.out.println(  "U"  );  
    } 
}private  void calculateOutputm23(String input) {
    if((  ((185 < a142) && (366 >= a142))  && cf)){
    	calculateOutputm24(input);
    } 
}private  void calculateOutputm26(String input) {
    if((cf && (input.equals("C")))){
    	 cf = (false);
    	a181 = (13);
    	a6 = ((((((  a6) / 5) * 5) + 0) % 15063)- 14935);
    	a110 = (16);
    	  System.out.println(  "T"  );  
    } 
}private  void calculateOutputm27(String input) {
    if((cf && (input.equals("B")))){
    	 cf = (false);
    	a89 = (4);
    	a71 = (8);
    	a110 = (11);
    	  System.out.println(  "U"  );  
    } 
    if(((input.equals("D")) && cf)){
    	 cf = (false);
    	a142 = (((((((  a142) - 0) % 90)- -276) * 5) % 90)+ 212);
    	a91 = (11);
    	a110 = (12);
    	  System.out.println(  "U"  );  
    } 
    if((cf && (input.equals("C")))){
    	 cf = (false);
    	a173 = (9);
    	a50 = (((((  a50) * 1) % 15046)+ -14953) - 2);
    	a110 = (14);
    	  System.out.println(  "U"  );  
    } 
}private  void calculateOutputm28(String input) {
    if((cf && (input.equals("B")))){
    	 cf = (false);
    	a173 = (2);
    	a50 = (((((  a50) % 15046)- 14953) + -1) / 5);
    	a110 = (14);
    	  System.out.println(  "T"  );  
    } 
    if(((input.equals("D")) && cf)){
    	 cf = (false);
    	a81 = (6);
    	a80 = (13);
    	  System.out.println(  "U"  );  
    } 
    if((cf && (input.equals("C")))){
    	 cf = (false);
    	a138 = (11);
    	a136 = (11);
    	a110 = (10);
    	  System.out.println(  "Z"  );  
    } 
}private  void calculateOutputm25(String input) {
    if((cf && (a51==7))){
    	calculateOutputm26(input);
    } 
    if((cf && (a51==11))){
    	calculateOutputm27(input);
    } 
    if(((a51==14) && cf)){
    	calculateOutputm28(input);
    } 
}private  void calculateOutputm30(String input) {
    if((cf && (input.equals("D")))){
    	 cf = (false);
    	a23 = (7);
    	a91 = (8);
    	a110 = (12);
    	  System.out.println(  "U"  );  
    } 
    if((cf && (input.equals("A")))){
    	 cf = (false);
    	a134 = (10);
    	a71 = (10);
    	a110 = (11);
    	  System.out.println(  "Z"  );  
    } 
    if((cf && (input.equals("C")))){
    	 cf = (false);
    	a6 = (((((  a6) - 0) % 52)- -272) - -2);
    	a83 = (2);
    	a110 = (9);
    	  System.out.println(  "X"  );  
    } 
}private  void calculateOutputm29(String input) {
    if(((a81==6) && cf)){
    	calculateOutputm30(input);
    } 
}private  void calculateOutputm32(String input) {
    if(((input.equals("C")) && cf)){
    	 cf = (false);
    	a134 = (5);
    	a71 = (10);
    	a110 = (11);
    	  System.out.println(  "V"  );  
    } 
    if((cf && (input.equals("A")))){
    	 cf = (false);
    	a18 = (4);
    	a136 = (5);
    	a110 = (10);
    	  System.out.println(  "U"  );  
    } 
    if((cf && (input.equals("B")))){
    	 cf = (false);
    	a118 = (((((((  a118) * a50)% 14999) % 14812)- -15187) + 0) - -1);
    	a133 = (12);
    	a110 = (15);
    	  System.out.println(  "U"  );  
    } 
}private  void calculateOutputm33(String input) {
    if(((input.equals("B")) && cf)){
    	 cf = (false);
    	a138 = (14);
    	a136 = (11);
    	a110 = (10);
    	  System.out.println(  "T"  );  
    } 
}private  void calculateOutputm31(String input) {
    if(((a173==2) && cf)){
    	calculateOutputm32(input);
    } 
    if((cf && (a173==9))){
    	calculateOutputm33(input);
    } 
}private  void calculateOutputm35(String input) {
    if((cf && (input.equals("A")))){
    	 cf = (false);
    	a51 = (7);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "Y"  );  
    } 
    if((cf && (input.equals("C")))){
    	 cf = (false);
    	a51 = (14);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "Z"  );  
    } 
    if((cf && (input.equals("E")))){
    	 cf = (false);
    	a138 = (14);
    	a136 = (11);
    	a110 = (10);
    	  System.out.println(  "T"  );  
    } 
}private  void calculateOutputm34(String input) {
    if(( 148 < a99  && cf)){
    	calculateOutputm35(input);
    } 
}private  void calculateOutputm37(String input) {
    if((cf && (input.equals("E")))){
    	 cf = (false);
    	a174 = (((((((  a174) * a118)% 14999) % 82)+ -37) - 1) + -1);
    	a83 = (6);
    	a110 = (9);
    	  System.out.println(  "T"  );  
    } 
    if((cf && (input.equals("C")))){
    	 cf = (false);

    	  System.out.println(  "U"  );  
    } 
    if(((input.equals("A")) && cf)){
    	 cf = (false);
    	a51 = (14);
    	a80 = (10);
    	a110 = (13);
    	  System.out.println(  "Z"  );  
    } 
    if((cf && (input.equals("B")))){
    	 cf = (false);
    	a87 = (((((((((  a87) * a118)% 14999) % 89)+ 226) - 9101) * 3) % 89)- -243);
    	a80 = (11);
    	a110 = (13);
    	  System.out.println(  "Y"  );  
    } 
    if((cf && (input.equals("D")))){
    	 cf = (false);

    	  System.out.println(  "U"  );  
    } 
}private  void calculateOutputm36(String input) {
    if(( 375 < a118  && cf)){
    	calculateOutputm37(input);
    } 
}private  void calculateOutputm39(String input) {
    if((cf && (input.equals("B")))){
    	 cf = (false);
    	a59 = (4);
    	a83 = (7);
    	a110 = (9);
    	  System.out.println(  "V"  );  
    } 
    if(((input.equals("A")) && cf)){
    	 cf = (false);
    	a181 = (11);
    	  System.out.println(  "V"  );  
    } 
    if((cf && (input.equals("D")))){
    	 cf = (false);
    	a6 = (((((((  a6) * a6)% 14999) % 14837)+ 15162) - 21650) - -21651);
    	a83 = (2);
    	a110 = (9);
    	  System.out.println(  "T"  );  
    } 
}private  void calculateOutputm40(String input) {
    if((cf && (input.equals("E")))){
    	 cf = (false);

    	  System.out.println(  "V"  );  
    } 
    if(((input.equals("C")) && cf)){
    	 cf = (false);

    	  System.out.println(  "V"  );  
    } 
    if((cf && (input.equals("A")))){
    	 cf = (false);

    	  System.out.println(  "V"  );  
    } 
}private  void calculateOutputm41(String input) {
    if(((input.equals("E")) && cf)){
    	 cf = (false);

    	  System.out.println(  "Y"  );  
    } 
}private  void calculateOutputm38(String input) {
    if((cf && (a181==6))){
    	calculateOutputm39(input);
    } 
    if(((a181==11) && cf)){
    	calculateOutputm40(input);
    } 
    if(((a181==13) && cf)){
    	calculateOutputm41(input);
    } 
}
public  void calculateOutput(String input) {
 	cf = (true);
    if(((a110==9) && cf)){
    	if(((a83==2) && cf)){
    		calculateOutputm1(input);
    	} 
    	if((cf && (a83==7))){
    		calculateOutputm4(input);
    	} 
    } 
    if(((a110==10) && cf)){
    	if(((a136==5) && cf)){
    		calculateOutputm6(input);
    	} 
    	if((cf && (a136==8))){
    		calculateOutputm8(input);
    	} 
    	if((cf && (a136==9))){
    		calculateOutputm10(input);
    	} 
    	if((cf && (a136==11))){
    		calculateOutputm13(input);
    	} 
    } 
    if(((a110==11) && cf)){
    	if((cf && (a71==8))){
    		calculateOutputm16(input);
    	} 
    	if((cf && (a71==10))){
    		calculateOutputm18(input);
    	} 
    } 
    if((cf && (a110==12))){
    	if((cf && (a91==8))){
    		calculateOutputm21(input);
    	} 
    	if(((a91==11) && cf)){
    		calculateOutputm23(input);
    	} 
    } 
    if(((a110==13) && cf)){
    	if((cf && (a80==10))){
    		calculateOutputm25(input);
    	} 
    	if((cf && (a80==13))){
    		calculateOutputm29(input);
    	} 
    } 
    if(((a110==14) && cf)){
    	if(( a50 <=  92  && cf)){
    		calculateOutputm31(input);
    	} 
    	if((cf &&  452 < a50 )){
    		calculateOutputm34(input);
    	} 
    } 
    if((cf && (a110==15))){
    	if(((a133==12) && cf)){
    		calculateOutputm36(input);
    	} 
    } 
    if(((a110==16) && cf)){
    	if((cf &&  a6 <=  127 )){
    		calculateOutputm38(input);
    	} 
    } 

    errorCheck();
    if(cf)
    	throw new IllegalArgumentException("Current state has no transition for this input!");
}

public static void main(String[] args) throws Exception 
	{
	     // init system and input reader
            Problem2 eca = new Problem2();

			// main i/o-loop
            while(true)
            {
            	//read input
                String input = stdin.readLine();
                try{
                	 //operate eca engine output = 
                	 eca.calculateOutput(input);
                } catch(IllegalArgumentException e){
    	    		System.err.println("Invalid input: " + e.getMessage());
                }
	    	}
	}
}