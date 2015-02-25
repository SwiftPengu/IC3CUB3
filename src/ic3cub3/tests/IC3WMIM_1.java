package ic3cub3.tests;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;

public class IC3WMIM_1 implements Problem{
	private final Literal x1,x2,x1p,x2p;
	
	public IC3WMIM_1(){
		x1 = new Literal();
		x2 = new Literal();
		x1p = x1.getPrimed();
		x2p = x2.getPrimed();

	}

	@Override
	public Cube getInitial() {
		return new Cube(x1.not(),x2.not());
	}

	@Override
	public Cube getTransition() {
		return new Cube(new Clause(x1,x2.not(),x2p),
				new Clause(x1,x2,x1p.not()),
				new Clause(x1.not(),x1p),
				new Clause(x1.not(),x2p.not()),
				new Clause(x2,x2p.not()));
	}

	@Override
	public Cube getProperty() {
		return new Clause(x1.not(),x2).asCube();
	}
	
	@Override
	public Cube getNegatedProperty() {
		return new Clause(x1.not(),x2).not();
	}

}
