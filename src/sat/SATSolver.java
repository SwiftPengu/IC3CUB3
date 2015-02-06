package sat;

import java.util.List;

import plf.Formula;

public abstract class SATSolver {
	/**
	 * Feeds the given formula to a SAT solver, and returns a satisfiable interpretation if it exists
	 * @param f the formula to be satisfied
	 * @return empty list if unsatisfiable, a list of formulae each representing a satisfiable interpretation if satisfiable
	 */
	public abstract List<Formula> solve(Formula f, boolean skip);
	public abstract List<Formula> solve(Formula f);
	
	public List<Formula> solveTimed(Formula f, boolean skip){
		long time = System.currentTimeMillis();
		List<Formula> result = solve(f,skip);
		System.out.println(String.format("Took %dms",System.currentTimeMillis()-time));
		return result;
	}
	
	/**
	 * Whether this solver needs the formula to be in CNF notation
	 * @return true when this solver needs the formula to be in CNF notation
	 */
	public abstract boolean needsCNF();
}
