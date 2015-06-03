package ic3cub3.tests;

import ic3cub3.ic3.IC3;
import ic3cub3.plf.cnf.Cube;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

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
		getProperties().stream().forEach(pp -> ic3.check(getInitial(), getTransition(),
                pp.getProperty(), pp.getNegatedProperty()));
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
