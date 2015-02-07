package sat;

import java.util.*;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.*;

import plf.cnf.*;
import plf.*;

public class SAT4J extends CNFSolver{
	private ISolver solver;
	private static final ArrayList<Cube> UNSAT = new ArrayList<Cube>();
	
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
		for(Clause clause:c.getClauses()){
			try {
				VecInt convertedClause = new VecInt();
				for(Literal l:clause.getLiterals()){
					convertedClause.push((int) l.getDIMACSID());//FIXME check if long can actually be cast, and if long is nessecary
				}
				solver.addClause(convertedClause);
			} catch (ContradictionException e) {
				return UNSAT;
			}
		}
		try {
			Cube result = new Cube();
			int[] model = solver.findModel();
			for(int var:model){
				int id = Math.abs(var);
				if(!skipPrimed || !isPrimeVar(id)){
					result.addClause(new Clause(new Literal(id,var<0,isPrimeVar(id))));
				}
			}
			return Arrays.asList(new Cube[]{result});
		} catch (TimeoutException e) {
			assert(false); //timeout should not happen
			return UNSAT;
		}
	}
	
	public IProblem getProblem(Cube c){
		return null;
	}
}
