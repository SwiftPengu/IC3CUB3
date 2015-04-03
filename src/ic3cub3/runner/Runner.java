package ic3cub3.runner;

import ic3cub3.antlr.ParserHelper;
import ic3cub3.ic3.IC3;
import ic3cub3.sat.SAT4J;
import ic3cub3.sat.SATSolver;
import ic3cub3.tests.ProblemSet;

import java.io.*;

public class Runner {
	public static int VERBOSE = 0;

	public static void main(String[] args) throws FileNotFoundException, IOException {		
		ProblemSet problems = ParserHelper.parse(new File("src/ic3cub3/rersproblems/Problem1/Problem1.c"));
		
		System.out.println("Starting with model checking...");
		SATSolver solver = new SAT4J();
		IC3 ic3 = new IC3(solver);
		for(int i = 0;i<problems.getProperties().size();i++){
			System.out.println("Checking property "+(i+1));
			boolean result = ic3.check(problems.getInitial(), problems.getTransition(), problems.getProperties().get(i).getProperty(), problems.getProperties().get(i).getNegatedProperty());
			System.out.println(result);
		}
		
		System.out.println("Done");
		
		/*Problem[] tests = new Problem[]{new IC3WMIM_1(),new IC3WMIM_2(), new ReachableBadState(true)};
		Problem pr = tests[1];
		long time = System.currentTimeMillis();
		SATSolver solver = new SAT4J();
		IC3 ic3 = new IC3(solver);
		ic3.check(pr.getInitial(), pr.getTransition(), pr.getProperty(),
				pr.getNegatedProperty());
		System.out.println(String.format("Time needed: %dms",
				System.currentTimeMillis() - time));*/
	}
	
	public static void printv(Object s, int minverbosity){
		if(VERBOSE>=minverbosity)System.out.println(s.toString());
	}
}
