package plf;

import java.util.Set;

import plf.cnf.TseitinCube;

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
	
	public abstract Set<Long> getTseitinVariables();
	
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
	public Formula iff(Formula f){
		assert(f!=null);
		return new AndFormula(this.implies(f),f.implies(this));
	}
	
	public abstract Formula rename(int old, int replacement);
	
	public abstract Formula getPrimed();
	
	/**
	 * Converts this Formula to CNF
	 * @return a cube which is equisatisfiable to this Formula
	 */
	protected abstract TseitinCube toCNF();
	
	public TseitinCube tseitinTransform(){
		return tseitinTransform(false);
	}
	
	public TseitinCube tseitinTransform(boolean negated){
		TseitinCube result = toCNF();
		if(negated)result.negate();
		result.stick();
		return result;
	}
}
