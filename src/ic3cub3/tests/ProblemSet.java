package ic3cub3.tests;

import ic3cub3.plf.cnf.Cube;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

/**
 * Class which contains multiple properties and their negation
 *
 */
@Data
public class ProblemSet {
	private final Cube initial;
	private final Cube transition;
	private final List<PropertyPair> properties;
	
	public ProblemSet(Cube initial,Cube transition, PropertyPair property){
		this(initial,transition,Arrays.asList(new PropertyPair[]{property}));
	}
	
	public ProblemSet(Cube initial,Cube transition, List<PropertyPair> properties){
		this.initial = initial;
		this.transition= transition;
		this.properties = properties;
	}

	@Data
	public static class PropertyPair{
		private final Cube property;
		private final Cube negatedProperty;
		
		public PropertyPair(Cube p, Cube np){
			this.property = p;
			this.negatedProperty=np;
		}
	}
}
