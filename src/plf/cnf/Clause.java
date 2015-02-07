package plf.cnf;

import java.util.*;

import plf.Formula;
import plf.Literal;

public class Clause extends Formula{
	private ArrayDeque<Literal> literals;
	
	public Clause(Collection<Literal> literals){
		assert(literals.size()>0);
		this.literals = new ArrayDeque<Literal>();
		this.literals.addAll(literals);
	}
	
	public Clause(Literal l){
		this(Arrays.asList(new Literal[]{l}));
	}
	
	public Clause(){
		this(new ArrayList<Literal>(0));
	}
	
	public void addLiteral(Literal l){
		literals.add(l);
	}
	
	@Override
	public String toString() {
		assert(literals.size()>0);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Iterator<Literal> lit = literals.iterator();
		sb.append(lit.next());
		while(lit.hasNext()){
			sb.append("v "+lit);
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public Cube not() {
		Cube negatedcube = new Cube();
		for(Literal l:literals){
			negatedcube.addClause(new Clause(l.not()));
		}
		return negatedcube;
	}

	@Override
	public Set<Long> getVariables() {
		Set<Long> result = new HashSet<Long>();
		for(Literal l:literals){
			result.add(l.getID());
		}
		return result;
	}

	@Override
	public String getLogic2CNFString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clause rename(int old, int replacement) {
		Clause renamed = new Clause();
		for(Literal l:literals){
			renamed.addLiteral(l.rename(old, replacement));
		}
		return renamed;
	}

	@Override
	public Clause getPrimed() {
		Clause primed = new Clause();
		for(Literal l:literals){
			primed.addLiteral(l.getPrimed());
		}
		return primed;
	}
}
