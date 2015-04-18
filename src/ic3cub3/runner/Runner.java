package ic3cub3.runner;

import ic3cub3.ic3.IC3;
import ic3cub3.sat.SAT4J;
import ic3cub3.sat.SATSolver;
import ic3cub3.tests.*;
import ic3cub3.tests.actual.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Runner {
	public static int VERBOSE = 0;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		/*ParserHelper ph = new ParserHelper();
		ProblemSet problems = ph.parse(new File("src/ic3cub3/rersproblems/problemtest.c"));
		
		System.out.println("Starting with model checking...");
		SATSolver solver = new SAT4J();
		IC3 ic3 = new IC3(solver);
		for(int i = 0;i<problems.getProperties().size();i++){
			System.out.println("Checking property "+(i+1));
			List<ProofObligation> result = ic3.check(problems.getInitial(), problems.getTransition(), problems.getProperties().get(i).getProperty(), problems.getProperties().get(i).getNegatedProperty());
			result.stream().map(ProofObligation::getCTI).map(ph.getGenerator()::convertState).forEach(System.out::println);
			System.exit(0);
		}
		
		System.out.println("Done");*/
		
		ProblemSet[] problems = new ProblemSet[]{new IC3WMIM_1(), new IC3WMIM_2(), new ReachableBadState()};
		SATSolver solver = new SAT4J();
		IC3 ic3 = new IC3(solver);
		for(ProblemSet ps:problems){
			long time = System.currentTimeMillis();
			for(int i = 0;i<1000;i++){
				ps.check(ic3);
			}
			System.out.println(String.format("Time needed: %dms",
					System.currentTimeMillis() - time));
		}
	}
	
	public static void printv(Object s, int minverbosity){
		if(VERBOSE>=minverbosity)System.out.println(s.toString());
	}
}
