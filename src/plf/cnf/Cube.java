package plf.cnf;

import java.util.*;

import plf.*;

public class Cube {
	private final ArrayDeque<Clause> clauses;
	
	public Cube(Collection<Clause> literals){
		//assert(literals.size()>0);
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
	
	public void addLiteral(Literal l){
		addClause(new Clause(l));
	}
	
	@Override
	public String toString() {
		assert(clauses.size()>0);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Iterator<Clause> lit = clauses.iterator();
		sb.append(lit.next());
		while(lit.hasNext()){
			sb.append(" ^ "+lit.next());
		}
		sb.append(")");
		return sb.toString();
	}

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
	
	public Cube getLiterals(){
		Cube result = new Cube();
		for(Clause c:getClauses()){
			for(Literal l:c.getLiterals()){
				result.addLiteral(l);
			}
		}
		return result;
	}
	
	public Set<Long> getVariables(){
		HashSet<Long> result = new HashSet<Long>();
		for(Clause c:getClauses()){
			result.addAll(c.getVariables());
		}
		return result;
	}
	
	public Cube and(Cube f) {
		Cube result = new Cube(clauses);
		for(Clause c:f.getClauses()){
			result.addClause(c);
		}
		return result;
	}
	
	public Cube and(Clause c){
		//TODO make new cube?
		addClause(c);
		return this;
	}
	
	public Formula toFormula(){
		assert(clauses.size()>0);
		Iterator<Clause> clauseit = clauses.iterator();
		Clause first = clauseit.next();
		if(clauses.size()==1){
			return first.toFormula();
		}else{
			Formula result = first.toFormula();
			while(clauseit.hasNext()){
				result = new AndFormula(result, clauseit.next().toFormula());
			}
			return result;
		}
	}
	
	public Clause not(){
		Clause result = new Clause();
		for(Clause c:getClauses()){
			assert(c.getLiterals().size()==1);
			result.addLiteral(c.getLiterals().getFirst());
		}
		return result;
	}
}
