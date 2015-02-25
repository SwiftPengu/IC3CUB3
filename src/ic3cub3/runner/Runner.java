package ic3cub3.runner;

import ic3cub3.ic3.IC3;
import ic3cub3.sat.SAT4J;
import ic3cub3.sat.SATSolver;
import ic3cub3.tests.IC3WMIM_2;
import ic3cub3.tests.Problem;

public class Runner {
	public static int VERBOSE = 1;

	public static void main(String[] args) {
		Problem pr = new IC3WMIM_2();
		long time = System.currentTimeMillis();
		SATSolver solver = new SAT4J();
		IC3 ic3 = new IC3(solver);
		ic3.check(pr.getInitial(), pr.getTransition(), pr.getProperty(),
				pr.getNegatedProperty());
		System.out.println(String.format("Time needed: %dms",
				System.currentTimeMillis() - time));

	}
}
