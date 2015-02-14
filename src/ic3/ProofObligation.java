package ic3;

import plf.cnf.Cube;
import runner.Runner;

/**
 * Class representing an obligation to prove that a given CTI should not hold be
 * reachable within level+1 steps
 *
 */
public class ProofObligation implements Comparable<ProofObligation> {
	private final Cube CTI;
	private final int level;

	public ProofObligation(Cube CTI, int level) {
		if (Runner.VERBOSE > 0)
			System.out.println(String.format(
					"New proof obligation: %s at level %d", CTI, level));
		this.CTI = CTI;
		this.level = level;
	}

	public Cube getCTI() {
		return CTI;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public int compareTo(ProofObligation arg0) {
		return this.level - arg0.level;
	}

}
