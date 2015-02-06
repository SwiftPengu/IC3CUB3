package plf.cnf;

import java.util.*;

public class Cube {
	private ArrayDeque<Clause> clauses;
	
	public Cube(Collection<Clause> literals){
		assert(literals.size()>0);
		this.clauses = new ArrayDeque<Clause>();
		this.clauses.addAll(literals);
	}
	
	public Cube(Clause c){
		this(Arrays.asList(new Clause[]{c}));
	}
	
	@Override
	public String toString() {
		assert(clauses.size()>0);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Iterator<Clause> lit = clauses.iterator();
		sb.append(lit.next());
		while(lit.hasNext()){
			sb.append("^ "+lit);
		}
		sb.append(")");
		return sb.toString();
	}
}
