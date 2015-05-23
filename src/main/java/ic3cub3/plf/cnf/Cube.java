package ic3cub3.plf.cnf;

import ic3cub3.plf.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 *  A class representing a conjunction of clauses
 */
@Getter
public class Cube {
	private final Set<Clause> clauses;

	public Cube(Collection<Clause> clauses) {
		// assert(literals.size()>0);
		this.clauses = new HashSet<>();
		this.clauses.addAll(clauses);
	}

	public Cube(Clause... clauses) {
		this(Arrays.asList(clauses));
	}

	/**
	 * Creates a cube representing a consecutive AND of all the provided
	 * literals
	 * 
	 * @param literals the literals in this cube
	 */
	public Cube(Literal... literals) {
		this();
		Arrays.stream(literals).forEach(this::addLiteral);
	}

	public Cube() {
		this(new LinkedList<>());
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
		return getClauses().stream().map(Clause::toString).collect(Collectors.joining(" ^ ","(",")"));
	}

	public Cube getPrimed() {
		Cube result = new Cube();
		getClauses().stream().map(Clause::getPrimed).forEach(result::addClause);
		return result;
	}

	public Cube getAllLiterals() {
		return new Cube(getClauses().stream().
				flatMap(c -> c.getLiterals().stream()).
				map(Clause::new).
				collect(Collectors.toSet()));
	}

	public Set<Integer> getVariables() {
		HashSet<Integer> result = new HashSet<>();
		getClauses().forEach(c -> result.addAll(c.getVariables()));
		return result;
	}

	public Cube and(Cube f) {
		Cube result = new Cube(getClauses());
		result.getClauses().addAll(f.getClauses());
		return result;
	}

	public Cube and(Clause c) {
		return and(c.asCube());
	}

	public Formula toFormula() {
		return getClauses().stream().map(Clause::toFormula).collect(Collectors.reducing(AndFormula::new)).get();
	}

	public Clause not() {
		assert(getClauses().stream().allMatch(c -> c.getLiterals().size()==1));
		return new Clause(getClauses().stream().
				flatMap(c -> c.getLiterals().stream()).
				map(Literal::not).
				collect(Collectors.toSet()));
	}

	public Set<Integer> getTseitinVariables() {
		return getClauses().stream().
				flatMap(c -> c.getTseitinVariables().stream()).
				collect(Collectors.toSet());
	}

	/**
	 * Obtains the number of literals in the largest clause
	 * 
	 * @return the maximum amount of variables in a single clause
	 */
	public int maxclauses() {
		return getClauses().stream().
				map(Clause::getLiterals).
				map(Set::size).
				max(Integer::compare).get();
	}

	public Cube clone() {
		return new Cube(getClauses());
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Cube && equals((Cube) obj);
	}

	public boolean equals(Cube c) {
		return getClauses().equals(c.getClauses());
	}
	
	@Override
	public int hashCode() {
		return clauses.hashCode();
	}
}
