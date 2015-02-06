package plf;

import java.util.Set;

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
	
	public abstract Set<Integer> getVariables();
	
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
}
