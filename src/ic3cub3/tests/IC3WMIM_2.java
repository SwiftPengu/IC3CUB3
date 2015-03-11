package ic3cub3.tests;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;

/**
 * Example 2 from 'IC3: Where Monolithic and Incremental Meet'
 * by Somenzi & Bradley
 *
 */
public class IC3WMIM_2 implements TestProblem{
	private final Literal x1;
	private final Literal x2;
	private final Literal x3;
	private final Literal x1p;
	private final Literal f; //Notice that x2p has been skipped as it is x1 mathematical constant
	private final Literal x3p;
	
	
	public IC3WMIM_2(boolean debug){
		x1 = new Literal();
		x2 = new Literal();
		x3 = new Literal();
		if(debug)System.out.println(String.format("x1: %s, x2: %s, x3: %s",x1,x2,x3));
		x1p = x1.getPrimed();
		f = x2.getPrimed();
		x3p = x3.getPrimed();
	}
	
	public IC3WMIM_2(){
		this(false);
	}

	@Override
	public Cube getInitial() {
		return new Cube(x1.not(),x2.not(),x3.not());
	}

	@Override
	public Cube getTransition() {
		return new Cube(
				new Clause(x2.not(),x1p),
				new Clause(x3.not(),f),
				new Clause(x1p.not(),x2),
				new Clause(f.not(),x3)
				);
	}

	@Override
	public Cube getProperty() {
		return new Clause(x1.not(),x2.not(),x3.not()).asCube();
	}
	
	@Override
	public Cube getNegatedProperty() {
		return new Clause(x1.not(),x2.not(),x3.not()).not();
	}

	@Override
	public boolean getExpectedResult() {
		return false;
	}
}
