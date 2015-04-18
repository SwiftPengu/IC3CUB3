package ic3cub3.tests.actual;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.tests.ProblemSet;

// 00 -> 01 -> 10 -> 11 (bad)
public class ReachableBadState extends ProblemSet{
	private static final Literal A = new Literal();
	private static final Literal B = new Literal();
	private static final Literal Ap = A.getPrimed();
	private static final Literal Bp = B.getPrimed();
	
	public ReachableBadState() {
		//I
		super(new Cube(A.not(),B.not()),
				//T
				new Cube(
						new Clause(A.not(),Ap),
						new Clause(A.not(),Bp),
						new Clause(B.not(),Ap),
						new Clause(B,Bp),
						new Clause(Ap,Bp),
						new Clause(B.not(),Bp.not(),A),
						new Clause(Ap.not(),Bp.not(),A),
						new Clause(Ap.not(),A,B)
						),
				//P
				PropertyPair.of(new Clause(A.not(),B.not()).asCube(),
						new Cube(A,B)));
	}
}
