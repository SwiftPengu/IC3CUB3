package ic3cub3.tests.actual;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.tests.ProblemSet;

/**
 * Example 1 from 'IC3: Where Monolithic and Incremental Meet'
 * by Somenzi & Bradley
 *
 */
public class IC3WMIM_1 extends ProblemSet{
	private static final Literal x1 = new Literal();
	private static final Literal x2 = new Literal();
	private static final Literal x1p = x1.getPrimed();
	private static final Literal x2p = x2.getPrimed();
	
	public IC3WMIM_1(){
		//I
		super(new Cube(x1.not(),x2.not()),
				//T
				new Cube(new Clause(x1,x2.not(),x2p),
						new Clause(x1,x2,x1p.not()),
						new Clause(x1.not(),x1p),
						new Clause(x1.not(),x2p.not()),
						new Clause(x2,x2p.not())),
				//P
				PropertyPair.of(new Clause(x1.not(),x2).asCube(),
						new Clause(x1.not(),x2).not()));
	}
}
