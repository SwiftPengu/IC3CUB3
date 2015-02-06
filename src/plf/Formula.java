package plf;

import java.util.Set;

import sat.SATSolver;

public abstract class Formula {
	public Formula and(Formula f){
		assert(f!=null);
		return new AndFormula(this,f);
	}
	
	public Formula or(Formula f){
		assert(f!=null);
		return new OrFormula(this,f);
	}
	
	public abstract Formula not();
	
	public abstract Set<Long> getVariables();
	
	public Set<Long> getPrimedVariables(){
		return getPrimed().getVariables();
	}
	
	public abstract String getLogic2CNFString();
	
	/**
	 * Returns this => f
	 * @param f a formula to be implied by this formula
	 * @return a formula representing this => f
	 */
	public Formula implies(Formula f){
		assert(f!=null);
		return new OrFormula(this.not(),f);
	}
	
	/**
	 * Returns this <=> f
	 * @param f a formula to be equal to this formula
	 * @return a formula representing this <=> f
	 */
	public Formula equals(Formula f){
		assert(f!=null);
		return new AndFormula(this.implies(f),f.implies(this));
	}
	
	public abstract Formula rename(int old, int replacement);
	
	/**
	 * Query a SAT solver whether this formula is equal to another formula
	 * @param f the other formula to check
	 * @param satsolver the SAT solver to query 
	 * @return true when this.equals(f) is satisfiable
	 */
	public boolean equal(Formula f,SATSolver satsolver){
		return satsolver.solve(this.equals(f),false).size()>0;
	}
	
	public abstract Formula getPrimed();
}
