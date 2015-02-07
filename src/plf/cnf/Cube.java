package plf.cnf;

import java.util.*;

import plf.Formula;

public class Cube extends Formula{
	private ArrayDeque<Clause> clauses;
	
	public Cube(Collection<Clause> literals){
		assert(literals.size()>0);
		this.clauses = new ArrayDeque<Clause>();
		this.clauses.addAll(literals);
	}
	
	public Cube(Clause c){
		this(Arrays.asList(new Clause[]{c}));
	}
	
	public Cube(){
		this(new ArrayList<Clause>(0));
	}
	
	public void addClause(Clause l){
		clauses.add(l);
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

	@Override
	public Clause not() {
		// TODO
		return null;
	}

	@Override
	public Set<Long> getVariables() {
		Set<Long> vars = new HashSet<Long>();
		for(Clause c:clauses){
			vars.addAll(c.getVariables());
		}
		return vars;
	}

	@Override
	public String getLogic2CNFString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cube rename(int old, int replacement) {
		Cube result = new Cube();
		for(Clause c:clauses){
			result.addClause(c.rename(old, replacement));
		}
		return result;
	}

	@Override
	public Cube getPrimed() {
		Cube result = new Cube();
		for(Clause c:clauses){
			result.addClause(c.getPrimed());
		}
		return result;
	}
	
	public ArrayDeque<Clause> getClauses(){
		return clauses;
	}
}
