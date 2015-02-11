package plf;

import java.util.Set;

import plf.cnf.Clause;
import plf.cnf.TseitinCube;

public class AndFormula extends Formula{
	private final Formula left;
	private final Formula right;
	
	public AndFormula(Formula left,Formula right){
		assert(left!=null);
		assert(right!=null);
		this.left=left;
		this.right=right;
	}
	
	@Override
	public String toString() {
		return String.format("(%s ^ %s)",left.toString(),right.toString());
	}

	@Override
	public OrFormula not() {
		//DeMorgan's law
		return new OrFormula(left.not(),right.not());
	}

	@Override
	public Set<Long> getVariables() {
		Set<Long> lvars = left.getVariables();
		Set<Long> rvars = right.getVariables();
		assert(lvars!=null);
		assert(rvars!=null);
		lvars.addAll(rvars);
		return lvars;
	}

	@Override
	public String getLogic2CNFString() {
		return String.format("(%s.%s)",left.getLogic2CNFString(),right.getLogic2CNFString());
	}

	@Override
	public AndFormula rename(int old, int replacement) {
		return new AndFormula(left.rename(old, replacement),right.rename(old, replacement));
	}

	@Override
	public AndFormula getPrimed() {
		return new AndFormula(left.getPrimed(),right.getPrimed());
	}
	
	@Override
	public TseitinCube toCNF() {
		//(~l v ~r v out) ^ (a v ~c) ^ (b v ~c)
		Literal output = new Literal(true);
		TseitinCube result = new TseitinCube(output);
		TseitinCube L = left.toCNF();
		TseitinCube R = right.toCNF();
		
		//~l v r ~v out
		Clause temp = new Clause();
		temp.addLiteral(L.getTseitinOutput().not());
		temp.addLiteral(R.getTseitinOutput().not());
		temp.addLiteral(output);
		result.addClause(temp);
		
		// a v ~c
		temp = new Clause();
		temp.addLiteral(L.getTseitinOutput());
		temp.addLiteral(output.not());
		result.addClause(temp);
		
		//b v ~c
		temp = new Clause();
		temp.addLiteral(R.getTseitinOutput().not());
		temp.addLiteral(output);
		result.addClause(temp);
		
		//state the the output should be true (=satisfiable)
		result.addLiteral(output);

		//add all clauses from L and R
		for(Clause c:L.getClauses()){
			result.addClause(c);
		}
		for(Clause c:R.getClauses()){
			result.addClause(c);
		}		
		return result;
	}
}
