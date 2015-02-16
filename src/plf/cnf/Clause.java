package plf.cnf;

import java.util.*;

import plf.*;

public class Clause {
	private LinkedList<Literal> literals;

	public Clause(Collection<Literal> literals) {
		// assert(literals.size()>0);
		this.literals = new LinkedList<Literal>();
		this.literals.addAll(literals);
	}

	public Clause(Literal... literals) {
		this(Arrays.asList(literals));
	}

	public Clause() {
		this(new LinkedList<Literal>());
	}

	public void addLiteral(Literal l) {
		literals.add(l);
	}

	@Override
	public String toString() {
		assert (literals.size() > 0);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Iterator<Literal> lit = literals.iterator();
		sb.append(lit.next());
		while (lit.hasNext()) {
			sb.append(" v " + lit.next());
		}
		sb.append(")");
		return sb.toString();
	}

	public Cube not() {
		Cube negatedcube = new Cube();
		for (Literal l : literals) {
			negatedcube.addClause(new Clause(l.not()));
		}
		return negatedcube;
	}

	public Clause getPrimed() {
		Clause primed = new Clause();
		for (Literal l : literals) {
			primed.addLiteral(l.getPrimed());
		}
		return primed;
	}

	public LinkedList<Literal> getLiterals() {
		return literals;
	}

	public Clause or(Clause f) {
		Clause result = new Clause(literals);
		for (Literal l : f.getLiterals()) {
			result.addLiteral(l);
		}
		return result;
	}

	public Formula toFormula() {
		assert (literals.size() > 0);
		Iterator<Literal> litit = literals.iterator();
		Literal first = litit.next();
		if (literals.size() == 1) {
			return first;
		} else {
			Formula result = first;
			while (litit.hasNext()) {
				result = new OrFormula(result, litit.next());
			}
			return result;
		}
	}

	public Set<Integer> getVariables() {
		HashSet<Integer> result = new HashSet<Integer>();
		for (Literal l : getLiterals()) {
			// if(!l.isTseitin()){
			result.add(l.getID());
			// }
		}
		return result;
	}

	/**
	 * Returns a cube stating only this clause
	 * 
	 * @return a cube which containing just this clause
	 */
	public Cube asCube() {
		return new Cube(this);
	}

	public Set<Integer> getTseitinVariables() {
		HashSet<Integer> result = new HashSet<Integer>();
		for (Literal l : getLiterals()) {
			if (l.isTseitin()) {
				result.add(l.getID());
			}
		}
		return result;
	}

	public int[] toDIMACSArray() {
		assert (literals.size() > 0);
		int[] result = new int[literals.size()];
		int index = 0;
		for (Literal l : getLiterals()) {
			result[index] = l.getDIMACSID();
			index++;
		}
		return result;
	}

	public Clause clone() {
		return new Clause(getLiterals());
	}

	public boolean equals(Clause c) {
		for (Literal l : getLiterals()) {
			if (c.getLiterals().contains(l))
				return false;
		}
		return true;
	}
}
