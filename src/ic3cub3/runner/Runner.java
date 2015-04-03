package ic3cub3.runner;

import ic3cub3.antlr.ParserHelper;
import ic3cub3.ic3.IC3;
import ic3cub3.sat.SAT4J;
import ic3cub3.sat.SATSolver;
import ic3cub3.tests.Problem;
import ic3cub3.tests.actual.*;

import java.io.*;

public class Runner {
	public static int VERBOSE = 0;

	public static void main(String[] args) throws FileNotFoundException, IOException {		
		ParserHelper.parse(new File("src/ic3cub3/rersproblems/Problem1/Problem1.c"));
		System.out.println(ic3cub3.plf.Literal.MAXID());
		System.exit(0);
		Problem[] tests = new Problem[]{new IC3WMIM_1(),new IC3WMIM_2(), new ReachableBadState(true)};
		Problem pr = tests[1];
		long time = System.currentTimeMillis();
		SATSolver solver = new SAT4J();
		IC3 ic3 = new IC3(solver);
		ic3.check(pr.getInitial(), pr.getTransition(), pr.getProperty(),
				pr.getNegatedProperty());
		System.out.println(String.format("Time needed: %dms",
				System.currentTimeMillis() - time));
	}
	
	public static void printv(Object s, int minverbosity){
		if(VERBOSE>=minverbosity)System.out.println(s.toString());
	}
}
