package ic3cub3.tests;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;

// 00 -> 01 -> 10 -> 11 (bad)
public class ReachableBadState implements TestProblem{
	private final Literal A,B,C,D;
	
	public ReachableBadState(boolean debug) {
		A = new Literal();
		B = new Literal();
		if(debug)System.out.println(String.format("x1: %s, x2: %s",A,B));
		C = A.getPrimed();
		D = B.getPrimed();
	}
	
	public ReachableBadState() {
		this(false);
	}
	
	@Override
	public Cube getInitial() {
		return new Cube(A.not(),B.not());
	}

	@Override
	public Cube getTransition() {
		return new Cube(
				new Clause(A.not(),C),
				new Clause(A.not(),D),
				new Clause(B.not(),C),
				new Clause(B,D),
				new Clause(C,D),
				new Clause(B.not(),D.not(),A),
				new Clause(C.not(),D.not(),A),
				new Clause(C.not(),A,B)
				);
	}

	@Override
	public Cube getProperty() {
		return new Clause(A.not(),B.not()).asCube();
	}

	@Override
	public Cube getNegatedProperty() {
		return new Cube(A,B);
	}

	@Override
	public boolean getExpectedResult() {
		return false;
	}

}
