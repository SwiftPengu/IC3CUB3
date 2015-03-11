package ic3cub3.ic3;

import ic3cub3.plf.cnf.Cube;
import ic3cub3.runner.Runner;

import java.util.LinkedList;

/**
 * Class representing an obligation to prove that a given CTI should not hold be
 * reachable within level+1 steps
 *
 */
public class ProofObligation implements Comparable<ProofObligation> {
	private final Cube CTI;
	private final int level;
	private final ProofObligation parent;

	public ProofObligation(Cube CTI, int level) {
		this(CTI,level,null);
	}
	
	public ProofObligation(Cube CTI, int level, ProofObligation parent){
		if (Runner.VERBOSE > 0)
			System.out.println(String.format(
					"New proof obligation: %s at level %d", CTI, level));
		this.CTI = CTI;
		this.level = level;
		this.parent = parent;
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
	
	@Override
	public String toString() {
		return String.format("ProofObligation[%s,%d]",getCTI(),getLevel());
	}
	
	public ProofObligation getParent() {
		return parent;
	}
	
	public LinkedList<ProofObligation> getProofTrace(){
		LinkedList<ProofObligation> result;
		if(parent==null){
			result = new LinkedList<>();
			result.add(this);
		}else{
			result = new LinkedList<>(parent.getProofTrace());
			result.addFirst(this);
		}
		return result;
	}
}
