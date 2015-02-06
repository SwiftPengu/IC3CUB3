package sat;

import plf.Formula;

public interface SATSolver {
	/**
	 * Feeds the 
	 * @param f
	 * @return
	 */
	public Formula solve(Formula f);
	
	/**
	 * Whether this solver needs the formula to be in CNF notation
	 * @return true when this solver needs the formula to be in CNF notation
	 */
	public boolean needsCNF();
}
