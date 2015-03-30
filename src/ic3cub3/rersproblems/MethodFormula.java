package ic3cub3.rersproblems;

import ic3cub3.antlr.ProblemParser.ExpressionContext;
import ic3cub3.antlr.ProblemParser.StatementContext;
import ic3cub3.plf.Formula;
import ic3cub3.plf.OrFormula;
import ic3cub3.plf.cnf.TseitinCube;

import java.util.Set;

import lombok.Data;
import lombok.Getter;

@Getter
public class MethodFormula extends Formula{
	private Set<Calculation> calculations;
	
	
	public OrFormula toFormula(){
		return null;
	}
	
	@Override
	public Formula not() {
		return toFormula().not();
	}

	@Override
	public Set<Integer> getVariables() {
		return toFormula().getVariables();
	}

	@Override
	public Set<Integer> getTseitinVariables() {
		return toFormula().getTseitinVariables();
	}

	@Override
	public String getLogic2CNFString() {
		return toFormula().getLogic2CNFString();
	}

	@Override
	public Formula rename(int old, int replacement) {
		return toFormula().rename(old, replacement);
	}

	@Override
	public Formula getPrimed() {
		return toFormula().getPrimed();
	}

	@Override
	protected TseitinCube toCNF() {
		return toFormula().toCNF();
	}

	@Data
	private class Calculation{
		private ExpressionContext expressionContext;
		private StatementContext statementContext;
	}
}
