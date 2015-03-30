package ic3cub3.tests;

import ic3cub3.plf.cnf.Cube;

import java.util.*;

import lombok.Data;

/**
 * Class which contains multiple properties and their negation
 *
 */
@Data
public class ProblemSet {
	private final Cube initial;
	private final Cube transition;
	private final Set<PropertyPair> properties;
	
	public ProblemSet(Cube initial,Cube transition, PropertyPair... properties){
		this.initial = initial;
		this.transition= transition;
		this.properties = new HashSet<>(Arrays.asList(properties));
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
