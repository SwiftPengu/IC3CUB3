package ic3cub3.plf.cnf;

import ic3cub3.plf.*;

import java.util.*;

import lombok.Getter;

@Getter
public class Cube {
	private final Set<Clause> clauses;

	public Cube(Collection<Clause> clauses) {
		// assert(literals.size()>0);
		this.clauses = new HashSet<Clause>();
		this.clauses.addAll(clauses);
	}

	public Cube(Clause... clauses) {
		this(Arrays.asList(clauses));
	}

	/**
	 * Creates a cube representing a consecutive AND of all the provided
	 * literals
	 * 
	 * @param literals
	 */
	public Cube(Literal... literals) {
		this();
		Arrays.stream(literals).forEach(l ->{
			addLiteral(l);
		});
	}

	public Cube() {
		this(new LinkedList<Clause>());
	}

	public void addClause(Clause... l) {
		clauses.addAll(Arrays.asList(l));
	}

	public void addLiteral(Literal l) {
		addClause(new Clause(l));
	}

	@Override
	public String toString() {
		assert (clauses.size() > 0);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Iterator<Clause> lit = clauses.iterator();
		sb.append(lit.next());
		while (lit.hasNext()) {
			sb.append(" ^ " + lit.next());
		}
		sb.append(")");
		return sb.toString();
	}

	public Cube getPrimed() {
		Cube result = new Cube();
		getClauses().forEach(c->{
			result.addClause(c.getPrimed());
		});
		return result;
	}

	public Cube getAllLiterals() {
		Cube result = new Cube();
		getClauses().forEach(c ->{
			c.getLiterals().forEach(l->{
				result.addLiteral(l);
			});
		});
		return result;
	}

	public Set<Integer> getVariables() {
		HashSet<Integer> result = new HashSet<Integer>();
		getClauses().forEach(c ->{
			result.addAll(c.getVariables());
		});
		return result;
	}

	public Cube and(Cube f) {
		Cube result = new Cube(clauses);
		f.getClauses().forEach(c ->{
			result.addClause(c);
		});
		return result;
	}

	public Cube and(Clause c) {
		return and(c.asCube());
	}

	public Formula toFormula() {
		assert (clauses.size() > 0);
		Iterator<Clause> clauseit = clauses.iterator();
		Clause first = clauseit.next();
		if (clauses.size() == 1) {
			return first.toFormula();
		} else {
			Formula result = first.toFormula();
			while (clauseit.hasNext()) {
				result = new AndFormula(result, clauseit.next().toFormula());
			}
			return result;
		}
	}

	public Clause not() {
		Clause result = new Clause();
		getClauses().forEach(c ->{
			assert (c.getLiterals().size() == 1);
			result.addLiteral(c.getLiterals().stream().findAny().get().not());
		});
		return result;
	}

	public Set<Integer> getTseitinVariables() {
		HashSet<Integer> result = new HashSet<Integer>();
		getClauses().forEach(c->{
			result.addAll(c.getTseitinVariables());
		});
		return result;
	}

	/**
	 * Obtains the number of literals in the largest clause
	 * 
	 * @return the maximum amount of variables in a single clause
	 */
	public int maxclauses() {
		assert (getClauses().size() > 0);
		int max = getClauses().iterator().next().getLiterals().size();
		for (Clause c : getClauses()) {
			int size = c.getLiterals().size();
			if (size > max)
				max = size;
		}
		return max;
	}

	public Cube clone() {
		return new Cube(getClauses());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Cube){
			return equals((Cube)obj);
		}else{
			return false;
		}
	}

	public boolean equals(Cube c) {
		return getClauses().equals(c.getClauses());
	}
	
	@Override
	public int hashCode() {
		return clauses.hashCode();
	}
}
