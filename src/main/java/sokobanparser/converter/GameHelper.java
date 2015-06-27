package sokobanparser.converter;

import ic3cub3.plf.Literal;
import lombok.Getter;
import sokobanparser.Game;

@Getter
public class GameHelper {
	private final Game game;

	// walking direction
	private final Literal[] dirvars;

	// locations
	private final Literal[] pxvars;
	private final Literal[] pyvars;

	// box locations for all boxes
	private final Literal[][] boxxvars;
	private final Literal[][] boxyvars;

	public GameHelper(Game game) {
		this.game = game;

		// Request direction variables
		dirvars = new Literal[]{new Literal(), new Literal(), new Literal(), new Literal()};

		// Request player x-coordinate variables
		pxvars = new Literal[game.getWidth()];
		for(int i = 0; i < pxvars.length; i++) {
			pxvars[i] = new Literal();
		}

		// Request player y-coordinate variables
		pyvars = new Literal[game.getHeight()];
		for (int i = 0; i < pyvars.length; i++) {
			pyvars[i] = new Literal();
		}

		// Request box x-coordinate variables
		boxxvars = new Literal[game.getBoxes().size()][game.getWidth()];
		for (int box = 0; box < game.getBoxes().size(); box++) {
			for (int i = 0; i < pxvars.length; i++) {
				boxxvars[box][i] = new Literal();
			}
		}

		// Request box y-coordinate variables
		boxyvars = new Literal[game.getBoxes().size()][game.getHeight()];
		for (int box = 0; box < game.getBoxes().size(); box++) {
			for (int i = 0; i < pyvars.length; i++) {
				boxyvars[box][i] = new Literal();
			}
		}

	}

	public int getDirRange() {
		return dirvars.length;
	}

	public int getXRange() {
		return pxvars.length;
	}

	public int getYRange() {
		return pyvars.length;
	}

	public Literal getDirectionVariable(Direction dir) {
		switch (dir) {
			case LEFT: {
				return dirvars[0];
			}
			case UP: {
				return dirvars[1];
			}
			case RIGHT: {
				return dirvars[2];
			}
			case DOWN: {
				return dirvars[3];
			}
			default: {
				assert (false) : "Unsupported direction";
				return null;
			}
		}
	}

	public Literal getDirectionVariablePrime(Direction dir) {
		return getDirectionVariable(dir).getPrimed();
	}

	public Literal getPlayerXVar(int x) {
		if (x < 0 || x >= getGame().getWidth()) {
			throw new IllegalArgumentException(String.format(
					"Undefined x-coordinate: %d", x));
		} else {
			return pxvars[x];
		}
	}

	public Literal getPlayerXVarPrime(int x) {
		return getPlayerXVar(x).getPrimed();
	}

	public Literal getPlayerYVar(int y) {
		if (y < 0 || y >= getGame().getHeight()) {
			throw new IllegalArgumentException(String.format(
					"Undefined y-coordinate: %d", y));
		} else {
			return pyvars[y];
		}
	}

	public Literal getPlayerYVarPrime(int y) {
		return getPlayerYVar(y).getPrimed();
	}

	public Literal getBoxX(int boxid, int x) {
		if (x < 0 || x >= getGame().getWidth()) {
			throw new IllegalArgumentException(String.format(
					"Undefined x-coordinate: %d", x));
		} else if (boxid < 0 || boxid >= getGame().getBoxes().size()) {
			throw new IllegalArgumentException(String.format(
					"Undefined box id: %d", boxid));
		} else {
			return boxxvars[boxid][x];
		}
	}

	public Literal getBoxXPrime(int boxid, int x) {
		return getBoxX(boxid,x).getPrimed();
	}

	public Literal getBoxY(int boxid, int y) {
		if (y < 0 || y >= getGame().getHeight()) {
			throw new IllegalArgumentException(String.format(
					"Undefined y-coordinate: %d", y));
		} else if (boxid < 0 || boxid >= getGame().getBoxes().size()) {
			throw new IllegalArgumentException(String.format(
					"Undefined box id: %d", boxid));
		} else {
			return boxyvars[boxid][y];
		}
	}

	public Literal getBoxYPrime(int boxid, int y) {
		return getBoxY(boxid,y).getPrimed();
	}
}