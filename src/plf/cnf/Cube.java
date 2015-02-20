package plf.cnf;

import java.util.*;

import plf.*;

public class Cube {
	private final LinkedList<Clause> clauses;

	public Cube(Collection<Clause> clauses) {
		// assert(literals.size()>0);
		this.clauses = new LinkedList<Clause>();
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
		for (Literal l : literals) {
			addLiteral(l);
		}
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
		for (Clause c : clauses) {
			result.addClause(c.getPrimed());
		}
		return result;
	}

	public List<Clause> getClauses() {
		return clauses;
	}

	public Cube getLiterals() {
		Cube result = new Cube();
		for (Clause c : getClauses()) {
			for (Literal l : c.getLiterals()) {
				result.addLiteral(l);
			}
		}
		return result;
	}

	public Set<Integer> getVariables() {
		HashSet<Integer> result = new HashSet<Integer>();
		for (Clause c : getClauses()) {
			result.addAll(c.getVariables());
		}
		return result;
	}

	public Cube and(Cube f) {
		Cube result = new Cube(clauses);
		for (Clause c : f.getClauses()) {
			result.addClause(c);
		}
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
		for (Clause c : getClauses()) {
			assert (c.getLiterals().size() == 1);
			result.addLiteral(c.getLiterals().getFirst().not());
		}
		return result;
	}

	public Set<Integer> getTseitinVariables() {
		HashSet<Integer> result = new HashSet<Integer>();
		for (Clause c : getClauses()) {
			result.addAll(c.getTseitinVariables());
		}
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

	public boolean equals(Cube c) {
		for (Clause cl : getClauses()) {
			if (!c.getClauses().contains(cl)) {
				return false;
			}
		}
		return true;
	}
}
