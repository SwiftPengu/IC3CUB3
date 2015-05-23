package ic3cub3.tests.actual;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.tests.ProblemSet;

/**
 * Example 2 from 'IC3: Where Monolithic and Incremental Meet'
 * by Somenzi & Bradley
 * 
 * Fixed, so it adheres to the provided picture
 *
 */
public class IC3WMIM_2 extends ProblemSet{
	private static final Literal x1 = new Literal();
	private static final Literal x2 = new Literal();
	private static final Literal x3 = new Literal();
	private static final Literal x1p = x1.getPrimed();
	private static final Literal x2p = x2.getPrimed();
	//private final Literal x3p; x3' is not used
	
	
	public IC3WMIM_2(){
		//I
		super(new Cube(x1.not(),x2.not(),x3.not()),
				//T
				new Cube(
						new Clause(x2.not(),x1p),
						new Clause(x3.not(),x2p),
						new Clause(x1p.not(),x2),
						new Clause(x2p.not(),x3)),
				//P
				PropertyPair.of(new Clause(x1.not(),x2.not(),x3.not()).asCube(),
						new Clause(x1.not(),x2.not(),x3.not()).not()));
	}
}
