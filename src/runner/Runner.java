package runner;

import ic3.IC3;

import java.io.IOException;

import plf.Formula;
import plf.Variable;
import sat.Logic2CNF;

public class Runner {
	public static int VERBOSE = 1;
	
	public static void main(String[] args){		
		try {
			int k = 0;
			Logic2CNF l2c = new Logic2CNF();
			
			//init formulae
			Formula x1 = new Variable(1);
			Formula x2 = new Variable(2);
			Formula x1p = new Variable(3);
			Formula x2p = new Variable(4);
			
			//List<Formula> F = new ArrayList<Formula>();
			Formula F0 = x1.not().and(x2.not()); //F0 = I
			
			Formula P = x1.not().or(x2);
			Formula Pprime = x1p.not().or(x2p); 
			
			//TRANS
			Formula T = x1.not().and(x2.not()).and(x1p.not().and(x2p.not()));//00 -> 00
			T=T.or(x1.not().and(x2).and(x1p.not().and(x2p))); //00 -> 01
			T=T.or(x1.not().and(x2).and(x1p.and(x2p))); //00 -> 11
			T=T.or(x1.and(x2).and(x1p.and(x2p.not()))); //11 -> 10
			T=T.or(x1.and(x2.not()).and(x1p.and(x2p.not()))); //10 -> 10
			
			IC3 ic3 = new IC3(new Logic2CNF());
			ic3.check(F0, T, P);
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
