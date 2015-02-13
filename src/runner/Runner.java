package runner;

import ic3.IC3;

import java.io.IOException;

import plf.Formula;
import plf.Literal;
import sat.Logic2CNF;
import sat.SATSolver;


public class Runner {
	public static int VERBOSE = 1;
	
	public static void main(String[] args){		
		try {			
			long time = System.currentTimeMillis();
			//init formulae
			Literal x1 = new Literal();
			Literal x2 = new Literal();
			Literal x1p = x1.getPrimed();
			Literal x2p = x2.getPrimed();
			
			//List<Formula> F = new ArrayList<Formula>();
			Formula F0 = x1.not().and(x2.not()); //F0 = I
			Formula P = x1.not().or(x2);
			
			//TRANS
			/*Formula TA = x1.not().and(x2.not()).and(x1p.not().and(x2p.not()));//00 -> 00
			TA=TA.or(x1.not().and(x2.not()).and(x1p.not().and(x2p))); //00 -> 01
			TA=TA.or(x1.not().and(x2.not()).and(x1p.and(x2p))); //00 -> 11
			TA=TA.or(x1.and(x2).and(x1p.and(x2p.not()))); //11 -> 10
			TA=TA.or(x1.and(x2.not()).and(x1p.and(x2p.not()))); //10 -> 10*/
			
			//T from stanford
			Formula TB = x1.or(x2.not()).or(x2p);
			TB = TB.and(x1.or(x2).or(x1p.not()));
			TB = TB.and(x1.not().or(x1p));
			TB = TB.and(x1.not().or(x2p.not()));
			TB = TB.and(x2.or(x2p.not()));
			
			//TRANS in CNF (WolframAlpha of TA)		
			/*Formula TC = x1.not().or(x1p).and(
					x1.not().or(x2p.not())).and(
					x1.or(x1p.not()).or(x2p)).and(
					x1.or(x2.not()));*/
			
			//TA <=> TB <=> TC
			
			SATSolver l2c = new Logic2CNF();
			IC3 ic3 = new IC3(l2c);
			
			ic3.check(F0.tseitinTransform(), TB.tseitinTransform(), P.tseitinTransform(),P.tseitinTransform(true));
			
			System.out.println(String.format("Time needed: %dms",System.currentTimeMillis()-time));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
