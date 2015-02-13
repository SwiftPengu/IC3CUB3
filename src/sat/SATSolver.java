package sat;

import java.util.List;

import plf.cnf.Cube;

public abstract class SATSolver {
	/**
	 * Feeds the given formula to a SAT solver, and returns a satisfiable interpretation if it exists
	 * @param f the formula to be satisfied
	 * @return empty list if unsatisfiable, a list of formulae each representing a satisfiable interpretation if satisfiable
	 */
	public abstract List<? extends Cube> sat(Cube f, boolean skipPrimed);

	public List<? extends Cube> sat(Cube f){
		return sat(f,false);
	}
	
	public List<? extends Cube> solveTimed(Cube f, boolean skip){
		long time = System.currentTimeMillis();
		List<? extends Cube> result = sat(f,skip);
		System.out.println(String.format("Took %dms",System.currentTimeMillis()-time));
		return result;
	}
}
