package ic3cub3.tests;

import ic3cub3.ic3.IC3;
import ic3cub3.ic3.ProofObligation;
import ic3cub3.plf.cnf.Cube;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

import static ic3cub3.runner.Runner.printv;

/**
 * Class representing a transition system, along with multiple properties and their negation
 */
@Data
public class ProblemSet {
	private final Cube initial;
	private final Cube transition;
	private final List<PropertyPair> properties;

	public ProblemSet(Cube initial,Cube transition, Cube P, Cube NP){
		this(initial,transition,Arrays.asList(new PropertyPair[]{PropertyPair.of(P,NP)}));
	}
	
	public ProblemSet(Cube initial,Cube transition, PropertyPair property){
		this(initial,transition,Arrays.asList(new PropertyPair[]{property}));
	}
	
	public ProblemSet(Cube initial,Cube transition, List<PropertyPair> properties){
		this.initial = initial;
		this.transition= transition;
		this.properties = properties;
	}
	
	public void check(IC3 ic3){
        printv(() -> "Checking: "+getClass().getName(),0);
		getProperties().stream()
                .map(pp -> ic3.check(getInitial(), getTransition(),
                        pp.getProperty(), pp.getNegatedProperty()))
                .map(po -> po != null ? po.getProofTrace() : null).forEach(System.out::println);
	}

	/**
	 * A class representing a property and its negation
	 */
	@Data(staticConstructor = "of")
	public static class PropertyPair{
		private final Cube property;
		private final Cube negatedProperty;
	}
}
