package ic3cub3.plf;

import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.TseitinCube;

import java.util.Set;

import lombok.Getter;

public class AndFormula extends Formula{
	@Getter
	private final Formula left;
	@Getter
	private final Formula right;
	
	private TseitinCube tsl,tsr;
	
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
		Literal output = new Literal(true);
		TseitinCube result = new TseitinCube(output);
		Thread t1 = new Thread(new Runnable(){
			public void run(){
				tsl = getLeft().toCNF();
			}
		});
		Thread t2 = new Thread(new Runnable(){
			public void run(){
				tsr = getRight().toCNF();
			}
		});
		t1.start();
		t2.start();
		try{
		t1.join();
		t2.join();
		}catch(InterruptedException e){}
		
		
		//~l v r ~v out
		Clause temp = new Clause();
		temp.addLiteral(tsl.getTseitinOutput().not());
		temp.addLiteral(tsr.getTseitinOutput().not());
		temp.addLiteral(output);
		result.addClause(temp);
		
		// a v ~c
		temp = new Clause();
		temp.addLiteral(tsl.getTseitinOutput());
		temp.addLiteral(output.not());
		result.addClause(temp);
		
		//b v ~c
		temp = new Clause();
		temp.addLiteral(tsr.getTseitinOutput());
		temp.addLiteral(output.not());
		result.addClause(temp);
		
		//state the the output should be true (=satisfiable)
		//result.addLiteral(output);

		//add all clauses from L and R
		for(Clause c:tsl.getClauses()){
			result.addClause(c);
		}
		for(Clause c:tsr.getClauses()){
			result.addClause(c);
		}
		
		tsl=null;
		tsr=null;
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
}
