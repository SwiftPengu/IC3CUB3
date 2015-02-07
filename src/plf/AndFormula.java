package plf;

import java.util.Set;

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
}
