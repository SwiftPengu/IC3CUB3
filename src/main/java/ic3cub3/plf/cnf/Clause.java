package ic3cub3.plf.cnf;

import ic3cub3.plf.Formula;
import ic3cub3.plf.Literal;
import ic3cub3.plf.OrFormula;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *	A class representing a disjunction of literals
 */
@Getter
public class Clause {
	private final Set<Literal> literals;

	public Clause(Collection<Literal> literals) {
		// assert(literals.size()>0);
		this.literals = new HashSet<>();
		this.literals.addAll(literals);
	}

	public Clause(Literal... literals) {
		this(Arrays.asList(literals));
	}

	public Clause() {
		this(new LinkedList<>());
	}

	public void addLiteral(Literal l) {
		literals.add(l);
	}

	@Override
	public String toString() {
		assert (literals.size() > 0);
		return getLiterals().stream().map(Literal::toString).collect(Collectors.joining(" v ","(",")"));
	}

	public Cube not() {
		return new Cube(getLiterals().stream().
				map(Literal::not).
				map(Clause::new).
				collect(Collectors.toSet()));
	}

	public Clause getPrimed() {
		return new Clause(getLiterals().stream().
				map(Literal::getPrimed).
				collect(Collectors.toSet()));
	}

	public Clause or(Clause f) {
		return new Clause(Stream.concat(
				f.getLiterals().stream(), 
				this.getLiterals().stream()).
					collect(Collectors.toSet()));
	}

	public Formula toFormula() {
		return getLiterals().stream().collect(Collectors.reducing(OrFormula::new)).get();
	}

	public Set<Integer> getVariables() {
		return getLiterals().stream().
				map(Literal::getID).
				collect(Collectors.toSet());
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
		return getLiterals().stream().
				filter(Literal::isTseitin).
				map(Literal::getID).
				collect(Collectors.toSet());
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
		return obj instanceof Clause && equals((Clause) obj);
	}

	public boolean equals(Clause c) {
		return getLiterals().equals(c.getLiterals());
	}
	
	@Override
	public int hashCode() {
		return getLiterals().hashCode();
	}
}
