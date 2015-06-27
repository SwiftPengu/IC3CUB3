package ic3cub3.runner;

import ic3cub3.ic3.IC3;
import ic3cub3.rersparser.ConcreteRersParser;
import ic3cub3.rersparser.ParserHelper;
import ic3cub3.sat.SAT4J;
import ic3cub3.tests.actual.IC3WMIM_1;

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
		IC3 ic3 = new IC3(new SAT4J());
		ParserHelper ph = new ParserHelper(new ConcreteRersParser());
		ph.parse(new File("src/main/java/ic3cub3/rersproblems/problemtest.c"));
		//ph.parse(new File("src/main/java/ic3cub3/rersproblems/Problem1/Problem1.c"));
        ph.check(ic3);

		new IC3WMIM_1().check(ic3);
	}
	
	public static void printv(Supplier<Object> s, int minverbosity){
		if(VERBOSE>=minverbosity)System.out.println(Optional.ofNullable(s.get()).map(Object::toString).orElse("null"));
	}
}
