package ic3cub3.plf.cnf;

import ic3cub3.plf.AndFormula;
import ic3cub3.plf.Formula;
import ic3cub3.plf.Literal;

import java.util.ArrayList;
import java.util.Collection;

/**
 *	A class representing a Cube which is obtained from a Tseitin transformation
 */
public class TseitinCube extends Cube {
	private Literal tseitinliteral;
	
	public TseitinCube(Literal outputlit, Collection<Clause> clauses){
		super(clauses);
		this.tseitinliteral=outputlit;
	}
	
	public void stick(){
		addLiteral(getTseitinOutput());
	}
	
	public TseitinCube(Literal outputlit){
		this(outputlit,new ArrayList<>(0));
	}
	
	public Literal getTseitinOutput(){
		return tseitinliteral;
	}
	
	@Override
	public Formula toFormula() {
		//States that the output of this formula should be true
		return new AndFormula(super.toFormula(),getTseitinOutput());
	}
	
	public void negate(){
		tseitinliteral = getTseitinOutput().not();
	}
	
	
}
