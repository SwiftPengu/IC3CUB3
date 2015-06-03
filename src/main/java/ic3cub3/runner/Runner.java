package ic3cub3.runner;

import ic3cub3.ic3.IC3;
import ic3cub3.rersparser.ConcreteRersParser;
import ic3cub3.rersparser.ParserHelper;
import ic3cub3.sat.SAT4J;
import ic3cub3.sat.SATSolver;
import ic3cub3.tests.ProblemSet;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Bootstrapping class
 *
 */
public class Runner {
	public static int VERBOSE = 1;

	public static void main(String[] args) throws IOException {
		ParserHelper ph = new ParserHelper(new ConcreteRersParser());
		ph.parse(new File("src/main/java/ic3cub3/rersproblems/problemtest.c"));
        ph.check(new IC3(new SAT4J()));

		//System.exit(0);
		//ProblemSet[] problems = new ProblemSet[]{new IC3WMIM_1(), new IC3WMIM_2(), new ReachableBadState()};
        ProblemSet[] problems = new ProblemSet[]{ph.getGenerator().getProblemSet()};
		SATSolver solver = new SAT4J();
		IC3 ic3 = new IC3(solver);
		for(ProblemSet ps:problems){
            printv(() -> "Processing problem set: "+ps,0);
			long time = System.currentTimeMillis();
			ps.check(ic3);
			System.out.println(String.format("Time needed: %dms",
					System.currentTimeMillis() - time));
		}
	}
	
	public static void printv(Supplier<Object> s, int minverbosity){
		if(VERBOSE>=minverbosity)System.out.println(Optional.ofNullable(s.get()).map(Object::toString).orElse("null"));
	}
}
