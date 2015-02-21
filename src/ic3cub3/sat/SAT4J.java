package ic3cub3.sat;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.runner.Runner;

import java.util.*;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.*;

public class SAT4J extends SATSolver{
	private ISolver solver;
	private static final ArrayList<Cube> UNSAT = new ArrayList<Cube>(); //UNSAT constant
	
	public SAT4J(ISolver solver){
		this.solver=solver;
	}
	
	public SAT4J(){
		this(SolverFactory.newDefault());
	}
	
	protected ISolver getSolver(Cube c){
		ISolver result = solver;
		solver.reset();
		solver.newVar(Literal.MAXID());
		solver.setExpectedNumberOfClauses(c.getClauses().size());
		//solver.setTimeout(3600);
		return result;
	}

	@Override
	public List<Cube> sat(Cube c, boolean skipPrimed) {
		//Set up the solver
		ISolver solver = getSolver(c);
		Set<Integer> tseitinvars = c.getTseitinVariables();
		for(Clause clause:c.getClauses()){
			VecInt conv = new VecInt(clause.toDIMACSArray());
			//System.out.println(clause+"; "+conv);
			try {	
				//System.out.println(clause);
				solver.addClause(conv);
			} catch (ContradictionException e) {
				if(Runner.VERBOSE>1)System.out.println("Trivially unsat");
				//System.out.println(c);
				return UNSAT;
			}
		}
		//attempt to find a model
		try {
			Cube result = new Cube();
			//System.exit(0);
			boolean satisfiable = solver.isSatisfiable();
			if(satisfiable){
				int[] model = solver.findModel();
				for(int var:model){
					int id = Math.abs(var);
					boolean primed = isPrimeVar(id);
					if(!skipPrimed || !primed){
						//only add non-tseitin variables
						if(!tseitinvars.contains(var)){
							Literal lit = new Literal(id-(primed?1:0),var<0,primed,false);
							result.addLiteral(lit);
						}
					}
				}
				return Arrays.asList(new Cube[]{result});
			}else{
				return UNSAT;
			}
		} catch (TimeoutException e) {
			assert(false); //timeout should not occur, as we have not requested one
			return UNSAT;
		}
	}
	
	public boolean isPrimeVar(int id){
		return id%2==0;
	}
}
