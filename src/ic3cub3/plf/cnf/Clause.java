package ic3cub3.plf.cnf;

import ic3cub3.plf.*;

import java.util.*;

import lombok.Getter;

@Getter
public class Clause {
	private final Set<Literal> literals;

	public Clause(Collection<Literal> literals) {
		// assert(literals.size()>0);
		this.literals = new HashSet<Literal>();
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
		getLiterals().forEach(l ->{
			primed.addLiteral(l.getPrimed());
		});
		return primed;
	}

	public Clause or(Clause f) {
		Clause result = new Clause(literals);
		f.getLiterals().forEach(l->{
			result.addLiteral(l);
		});
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
		getLiterals().forEach(l -> {
			result.add(l.getID());
		});
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
		getLiterals().forEach(l -> {
			if (l.isTseitin()) {
				result.add(l.getID());
			}
		});
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Clause){
			return equals((Clause)obj);
		}else{
			return false;
		}
	}

	public boolean equals(Clause c) {
		return getLiterals().equals(c.getLiterals());
	}
	
	@Override
	public int hashCode() {
		return literals.hashCode();
	}
}
