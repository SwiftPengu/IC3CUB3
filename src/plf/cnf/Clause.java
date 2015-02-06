package plf.cnf;

import java.util.*;

import plf.Literal;

public class Clause {
	private ArrayDeque<Literal> literals;
	
	public Clause(Collection<Literal> literals){
		assert(literals.size()>0);
		this.literals = new ArrayDeque<Literal>();
		this.literals.addAll(literals);
	}
	
	public Clause(Literal l){
		this(Arrays.asList(new Literal[]{l}));
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
}
