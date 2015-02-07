package sat;

import java.util.List;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;

import plf.cnf.Cube;

public class SAT4J extends CNFSolver{
	private ISolver solver;
	
	public SAT4J(ISolver solver){
		this.solver=solver;
	}
	
	public SAT4J(){
		this(SolverFactory.newDefault());
	}
	
	protected ISolver getSolver(){
		return solver;
	}

	@Override
	public List<Cube> solve(Cube c, boolean skipPrimed) {
		ISolver solver = getSolver();
		return null;
	}
}
