package plf.cnf;

import java.util.ArrayList;
import java.util.Collection;

import plf.*;

public class TseitinCube extends Cube {
	private Literal tseitinliteral;
	
	public TseitinCube(Literal outputlit, Collection<Clause> clauses){
		super(clauses);
		this.tseitinliteral=outputlit;
	}
	
	public TseitinCube(Literal outputlit){
		this(outputlit,new ArrayList<Clause>(0));
	}
	
	public Literal getTseitinOutput(){
		return tseitinliteral;
	}
	
	@Override
	public Formula toFormula() {
		//States that the output of this formula should be true
		return new AndFormula(super.toFormula(),getTseitinOutput());
	}
	
	public Formula toNegatedFormula() {
		//States that the output of this formula should be false
		return new AndFormula(super.toFormula(),getTseitinOutput().not());
	}

}
