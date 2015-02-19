package runner;

import ic3.IC3;
import plf.Literal;
import plf.cnf.Clause;
import plf.cnf.Cube;
import sat.SAT4J;
import sat.SATSolver;


public class Runner {
	public static int VERBOSE = 1;
	
	public static void main(String[] args){
			long time = System.currentTimeMillis();
			//init formulae
			Literal x1 = new Literal();
			Literal x2 = new Literal();
			Literal x1p = x1.getPrimed();
			Literal x2p = x2.getPrimed();
			
			//List<Formula> F = new ArrayList<Formula>();
			//Formula F0 = x1.not().and(x2.not()); //F0 = I
			Cube F0 = new Cube(x1.not(),x2.not());
			//Formula P = x1.not().or(x2);
			Clause P = new Clause(x1.not(),x2);
			
			//TRANS
			/*Formula TA = x1.not().and(x2.not()).and(x1p.not().and(x2p.not()));//00 -> 00
			TA=TA.or(x1.not().and(x2.not()).and(x1p.not().and(x2p))); //00 -> 01
			TA=TA.or(x1.not().and(x2.not()).and(x1p.and(x2p))); //00 -> 11
			TA=TA.or(x1.and(x2).and(x1p.and(x2p.not()))); //11 -> 10
			TA=TA.or(x1.and(x2.not()).and(x1p.and(x2p.not()))); //10 -> 10*/
			
			//T from the stanford paper
			/*Formula TB = x1.or(x2.not()).or(x2p);
			TB = TB.and(x1.or(x2).or(x1p.not()));
			TB = TB.and(x1.not().or(x1p));
			TB = TB.and(x1.not().or(x2p.not()));
			TB = TB.and(x2.or(x2p.not()));*/
			
			Cube TB = new Cube(new Clause(x1,x2.not(),x2p),
						new Clause(x1,x2,x1p.not()),
						new Clause(x1.not(),x1p),
						new Clause(x1.not(),x2p.not()),
						new Clause(x2,x2p.not()));
			
			//TRANS in CNF (WolframAlpha of TA)		
			/*Formula TC = x1.not().or(x1p).and(
					x1.not().or(x2p.not())).and(
					x1.or(x1p.not()).or(x2p)).and(
					x1.or(x2.not()));*/
			
			//TA <=> TB <=> TC
			
			SATSolver solver = new SAT4J();
			IC3 ic3 = new IC3(solver);

			ic3.check(F0, TB, P.asCube(),P.not());
			
			System.out.println(String.format("Time needed: %dms",System.currentTimeMillis()-time));
	}
}
