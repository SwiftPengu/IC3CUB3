package ic3cub3.tests;

import ic3cub3.plf.cnf.Cube;

public interface Problem {
	public Cube getInitial();
	public Cube getTransition();
	public Cube getProperty();
	public Cube getNegatedProperty();
}
