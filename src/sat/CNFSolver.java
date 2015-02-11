package sat;

import plf.cnf.Cube;

public abstract class CNFSolver extends SATSolver{
	/**
	 * Feeds the given formula to a SAT solver, and returns a satisfiable interpretation if it exists
	 * @param c the formula to be satisfied
	 * @return empty list if unsatisfiable, a list of cubes each representing a satisfiable interpretation if satisfiable
	 */
	public abstract Cube sat(Cube c, boolean skip);
	
	public Cube sat(Cube c){
		return sat(c,false);
	}
}
