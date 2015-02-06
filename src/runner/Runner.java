package runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logic2cnf.Logic2CNF;
import plf.Formula;
import plf.Variable;

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
			System.out.println(Pprime.not()); 
			
			//TRANS
			Formula T = x1.not().and(x2.not()).and(x1p.not().and(x2p.not()));//00 -> 00
			T=T.or(x1.not().and(x2).and(x1p.not().and(x2p))); //00 -> 01
			T=T.or(x1.not().and(x2).and(x1p.and(x2p))); //00 -> 11
			T=T.or(x1.and(x2).and(x1p.and(x2p.not()))); //11 -> 10
			T=T.or(x1.and(x2.not()).and(x1p.and(x2p.not()))); //10 -> 10
			
			//I => P (check UNSAT)
			l2c.solve(F0.implies(P).not());
			
			//I ^ T => P (check UNSAT)
			l2c.solve(F0.and(T).implies(P).not());
			
			//F1  = P
			Formula F1 = P;
			
			k = 1;
						
			//Get counterexample of Fk ^ T => P'
			//check sat of F1 ^ T ^ p'
			//l2c.solve(F1.and(T).and(Pprime));
			l2c.solve(F1.and(T).implies(Pprime).not());
			l2c.solve(F1.and(T).implies(Pprime.not()).not());
			l2c.solve(F1.and(T).and(Pprime.not()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
