package ic3cub3.plf;

import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.TseitinCube;

import java.util.Set;

public class OrFormula extends Formula{
	private final Formula left;
	private final Formula right;
	
	public OrFormula(Formula left,Formula right){
		assert(left!=null);
		assert(right!=null);
		this.left=left;
		this.right=right;
	}
	
	@Override
	public String toString() {
		return String.format("(%s v %s)",left.toString(),right.toString());
	}
	
	@Override
	public AndFormula not() {
		//DeMorgan's law
		return new AndFormula(left.not(),right.not());
	}

	@Override
	public Set<Integer> getVariables() {
		Set<Integer> lvars = left.getVariables();
		Set<Integer> rvars = right.getVariables();
		assert(lvars!=null);
		assert(rvars!=null);
		lvars.addAll(rvars);
		return lvars;
	}

	@Override
	public String getLogic2CNFString() {
		return String.format("(%s+%s)",left.getLogic2CNFString(),right.getLogic2CNFString());
	}

	@Override
	public OrFormula rename(int old, int replacement) {
		return new OrFormula(left.rename(old, replacement), right.rename(old, replacement));
	}
	
	@Override
	public OrFormula getPrimed() {
		return new OrFormula(left.getPrimed(),right.getPrimed());
	}
	
	@Override
	public TseitinCube toCNF() {
		//(l v r v ~out) ^ (~a v c) ^ (~b v c)
		Literal output = new Literal(true);
		TseitinCube result = new TseitinCube(output);
		TseitinCube L = left.toCNF();
		TseitinCube R = right.toCNF();
		
		//l v r v ~out
		Clause temp = new Clause();
		temp.addLiteral(L.getTseitinOutput());
		temp.addLiteral(R.getTseitinOutput());
		temp.addLiteral(output.not());
		result.addClause(temp);
		
		// ~a v c
		temp = new Clause();
		temp.addLiteral(L.getTseitinOutput().not());
		temp.addLiteral(output);
		result.addClause(temp);
		
		//~b v c
		temp = new Clause();
		temp.addLiteral(R.getTseitinOutput().not());
		temp.addLiteral(output);
		result.addClause(temp);
		
		//state the the output should be true (=satisfiable)
		//result.addLiteral(output);

		//add all clauses from L and R
		for(Clause c:L.getClauses()){
			result.addClause(c);
		}
		for(Clause c:R.getClauses()){
			result.addClause(c);
		}		
		return result;
	}
	
	@Override
	public Set<Integer> getTseitinVariables() {
		Set<Integer> lvars = left.getTseitinVariables();
		Set<Integer> rvars = right.getTseitinVariables();
		assert(lvars!=null);
		assert(rvars!=null);
		lvars.addAll(rvars);
		return lvars;
	}
}