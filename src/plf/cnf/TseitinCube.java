package plf.cnf;

import java.util.ArrayList;
import java.util.Collection;

import plf.Literal;

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
}
