package ic3cub3.runner;

import ic3cub3.ic3.IC3;
import ic3cub3.sat.SAT4J;
import ic3cub3.sat.SATSolver;
import ic3cub3.tests.ProblemSet;
import ic3cub3.tests.actual.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Bootstrapping class
 *
 */
public class Runner {
	public static int VERBOSE = 0;

	public static void main(String[] args) throws IOException {
		final int j = Integer.MAX_VALUE;
		IntStream.range(0,32).boxed().map(i -> ((j>>i)&1) == 1).forEach(System.out::println);
		System.exit(0);

		ProblemSet[] problems = new ProblemSet[]{new IC3WMIM_1(), new IC3WMIM_2(), new ReachableBadState()};
		SATSolver solver = new SAT4J();
		IC3 ic3 = new IC3(solver);
		for(ProblemSet ps:problems){
			long time = System.currentTimeMillis();
			ps.check(ic3);
			System.out.println(String.format("Time needed: %dms",
					System.currentTimeMillis() - time));
		}
	}
	
	public static void printv(Supplier<Object> s, int minverbosity){
		if(VERBOSE>=minverbosity)System.out.println(s.get().toString());
	}
}
