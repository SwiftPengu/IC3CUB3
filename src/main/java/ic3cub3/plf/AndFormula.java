package ic3cub3.plf;

import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.plf.cnf.TseitinCube;
import lombok.Getter;

import java.util.Set;

/**
 * A class representing the logical AND of two other formulae
 */
public class AndFormula extends Formula{
	@Getter
	private final Formula left;
	@Getter
	private final Formula right;
	
	public AndFormula(Formula left,Formula right){
		assert(left!=null);
		assert(right!=null);
		this.left=left;
		this.right=right;
	}
	
	@Override
	public String toString() {
		return String.format("(%s ^ %s)",getLeft().toString(),getRight().toString());
	}

	@Override
	public OrFormula not() {
		//DeMorgan's law
		return new OrFormula(getLeft().not(),getRight().not());
	}

	@Override
	public Set<Integer> getVariables() {
		Set<Integer> lvars = getLeft().getVariables();
		Set<Integer> rvars = getRight().getVariables();
		assert(lvars!=null);
		assert(rvars!=null);
		lvars.addAll(rvars);
		return lvars;
	}

	@Override
	public String getLogic2CNFString() {
		return String.format("(%s.%s)",getLeft().getLogic2CNFString(),getRight().getLogic2CNFString());
	}

	@Override
	public AndFormula rename(int old, int replacement) {
		return new AndFormula(getLeft().rename(old, replacement),getRight().rename(old, replacement));
	}

	@Override
	public AndFormula getPrimed() {
		return new AndFormula(getLeft().getPrimed(),getRight().getPrimed());
	}
	
	@Override
	public TseitinCube toCNF() {
		//(~l v ~r v out) ^ (a v ~c) ^ (b v ~c)
		final Literal output = new Literal(true);
		final TseitinCube result = new TseitinCube(output);
		final TseitinCube L = getLeft().toCNF();
		final TseitinCube R = getRight().toCNF();
		
		
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
		temp.addLiteral(R.getTseitinOutput());
		temp.addLiteral(output.not());
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
		Set<Integer> lvars = getLeft().getTseitinVariables();
		Set<Integer> rvars = getRight().getTseitinVariables();
		assert(lvars!=null);
		assert(rvars!=null);
		lvars.addAll(rvars);
		return lvars;
	}
	
	@Override
	public Cube toEquivalentCube() {
        Cube R = getRight().toEquivalentCube();
        Cube L = getLeft().toEquivalentCube();
		return L.and(R);
	}
}