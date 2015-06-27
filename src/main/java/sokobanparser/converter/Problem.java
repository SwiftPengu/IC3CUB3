package sokobanparser.converter;

import ic3cub3.plf.AndFormula;
import ic3cub3.plf.Formula;
import ic3cub3.plf.cnf.Cube;
import lombok.Getter;
import sokobanparser.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Class representing the BDDs of the problem to be solved
 *
 */
@Getter
public class Problem {
	private final Game game;
	private final GameHelper gh;

	public Problem(Game game){
		this.game=game;
		gh = new GameHelper(game);
	}

	/**
	 * Constructs a BDD representing the initial state
	 * @return referenced BDD representing the initial state
	 */
	public Cube getInit(){
		Cube dir = makeUniqueDirection();
		Cube p_x = makePlayerAtX(game.getPlayerLocation()[0]);
		Cube p_y = makePlayerAtY(game.getPlayerLocation()[1]);
		Cube boxes = makeBoxes();

		return dir.and(p_x).and(p_y).and(boxes);
	}

	/**
	 * Construct the BDD representing the goal state
	 * After all, this is the error state we want to
	 * search for.
	 * @return referenced BDD
	 */
	public Cube getError(){
		//Obtain a list of all goals
		List<int[]> goals = new ArrayList<int[]>();
		for(int x = 0; x < game.getWidth(); x++) {
			for(int y = 0; y < game.getHeight(); y++) {
				if(game.getItemAt(x, y) == Game.FieldItem.DOT) {
					goals.add(new int[]{x,y});
				}
			}
		}

		//For every box we will check whether it is on one of the goals
		List<int[]> boxes = game.getBoxes();

		return IntStream.range(0,boxes.size()).boxed()
				//for every box (disjunction)
				.flatMap(boxid -> goals.stream()
						//x and y match goal x,y
						.map(goal -> new AndFormula(gh.getBoxX(boxid, goal[0]), gh.getBoxY(boxid, goal[1]))))
				.map(f -> (Formula)f)
				.reduce(Formula::or).get().toEquivalentCube();
	}

	/**
	 * BDD representing the transition relation
	 * @return referenced BDD
	 */
	public Cube getTrans(){
		return makeWallsInvariant()
				.and(makeDirectionTrans())
				.and(makePlayerMove())
				.and(makeNoBoxCollisionInvariant())
				.and(makeBoxMove());
	}

	/**
	 * Builds a BDD that specifies that the player can only move
	 * in a single direction for every step
	 * @return The reference to the BDD
	 */
	private Cube makeUniqueDirection() {
		Direction[] dirs = Direction.values();
		disableGC();
		long result = getFalse();
		for(int i = 0; i < dirs.length; i++) {
			long dirBdd = getTrue();
			for(int j = 0; j < dirs.length; j++) {
				dirBdd = makeAnd(dirBdd, i == j ? gh.getDirectionVariable(dirs[j]) : makeNot(gh.getDirectionVariable(dirs[j])));
			}
			result = makeOr(result, dirBdd);
		}
		ref(result);
		enableGC();
		return result;
	}

	/**
	 * Obtain a BDD representing that the player is at location x
	 * @param x the location to represent
	 * @return a reference to a BDD representing that the player is at location x
	 */
	private Cube makePlayerAtX(int x){
		disableGC();
		long result = getTrue();
		for(int i = 0;i<game.getWidth();i++){
			result = makeAnd(result,
					(i==x)?gh.getPlayerXVar(i)
							:makeNot(gh.getPlayerXVar(i)));
		}
		ref(result);
		enableGC();
		return result;
	}

	//returns a referenced BDD representing that the player is at location y
	private Cube makePlayerAtY(int y){
		disableGC();
		long result = getTrue();
		for(int i = 0;i<game.getHeight();i++){
			result = makeAnd(result,
					(i==y)?gh.getPlayerYVar(i)
							:makeNot(gh.getPlayerYVar(i)));
		}
		ref(result);
		enableGC();
		return result;
	}

	//returns a reference to a BDD representing all the boxes at the right locations
	private Cube makeBoxes() {
		disableGC();
		long result = getTrue();
		List<int[]> boxes = game.getBoxes();
		for(int boxid = 0; boxid < boxes.size(); boxid++) {
			int[] box = boxes.get(boxid);
			int x = box[0];
			int y = box[1];

			for(int varX = 0; varX < game.getWidth(); varX++) {
				result = makeAnd(result, x == varX ? gh.getBoxX(boxid, varX) : makeNot(gh.getBoxX(boxid, varX)));
			}
			for(int varY = 0; varY < game.getHeight(); varY++) {
				result = makeAnd(result, y == varY ? gh.getBoxY(boxid, varY) : makeNot(gh.getBoxY(boxid, varY)));
			}
		}
		ref(result);
		enableGC();
		return result;
	}

	/**
	 * Construct the BDD representing that the player and all boxes
	 * are not allowed to be in a wall in the next state.
	 * This is an invariant.
	 * @return The referenced BDD.
	 */
	protected Cube makeWallsInvariant() {
		//Store all the walls
		List<int[]> walls = new ArrayList<int[]>();
		for(int x = 0; x < game.getWidth(); x++) {
			for(int y = 0; y < game.getHeight(); y++) {
				if(game.getItemAt(x, y) == FieldItem.WALL) {
					walls.add(new int[]{x,y});
				}
			}
		}

		disableGC();
		//state that the next player location is on a wall or that the next box location is on a wall
		long result = getFalse();
		List<int[]> boxes = game.getBoxes();
		for(int[] wall:walls) {
			int x = wall[0];
			int y = wall[1];
			result = makeOr(result, makeAnd(gh.getPlayerXVarPrime(x), gh.getPlayerYVarPrime(y)));
			for(int boxid = 0; boxid < boxes.size(); boxid++) {
				result = makeOr(result, makeAnd(gh.getBoxXPrime(boxid, x), gh.getBoxYPrime(boxid, y)));
			}
		}
		result = ref(makeNot(result));
		enableGC();
		return result;
	}

	/**
	 * Construct the BDD representing that no pair of boxes
	 * is allowed to be in the same place.
	 * This is an invariant.
	 * @return The referenced BDD.
	 */
	protected Cube makeNoBoxCollisionInvariant() {
		int numberOfBoxes = game.getBoxes().size();
		long result = getTrue();
		//Invariant is only needed when more than one box exists
		if(numberOfBoxes>1){
			// We will make pairwise expressions for all the boxes
			for(int i = 0; i < numberOfBoxes; i++) {
				for(int j = i + 1; j < numberOfBoxes; j++) {
					long paircollision = makeBoxCollisionPair(i,j);
					//TODO test dit
					deref(result);
					result = ref(makeAnd(result, makeNot(paircollision)));
					deref(paircollision);
				}
			}
		}
		result = ref(result);
		return result;
	}

	protected Cube makeBoxCollisionPair(int box1, int box2) {
		disableGC();
		long result = getTrue();
		//TODO optimize: only consider valid positions (without a wall)
		for(int x = 0; x < game.getWidth(); x++) {
			result = makeAnd(result, makeEquals(gh.getBoxXPrime(box1, x), gh.getBoxXPrime(box2, x)));
		}
		for(int y = 0; y < game.getHeight(); y++) {
			result = makeAnd(result, makeEquals(gh.getBoxYPrime(box1, y), gh.getBoxYPrime(box2, y)));
		}
		result = ref(result);
		enableGC();
		return result;
	}

	/**
	 * Build a BDD that represents that the next state
	 * has a unique direction for the player
	 * @return The referenced BDD
	 */
	protected Cube makeDirectionTrans() {
		Direction[] dirs = Direction.values();
		disableGC();
		long result = getFalse();
		for(int i = 0; i < dirs.length; i++) {
			long dirBdd = getTrue();
			for(int j = 0; j < dirs.length; j++) {
				dirBdd = makeAnd(dirBdd, i == j ? gh.getDirectionVariablePrime(dirs[j]) : makeNot(gh.getDirectionVariablePrime(dirs[j])));
			}
			result = makeOr(result, dirBdd);
		}
		ref(result);
		enableGC();
		return result;
	}

	/**
	 * Build a BDD that represents the allowed moves of the player
	 * @return The references BDD
	 */
	protected Cube makePlayerMove() {
		disableGC();
		// direction=LEFT & x>0 -> next(x) = x+(-1) & next(y) = y
		long moveLeft = makeImplies(
							makeAnd(gh.getDirectionVariable(Direction.LEFT), makeNot(gh.getPlayerXVar(0))),
							makePlayerMove(-1,0));

		// direction=RIGHT & x<xDimen+(-1) -> next(x) = x+1 & next(y) = y
		long moveRight = makeImplies(
							makeAnd(gh.getDirectionVariable(Direction.RIGHT), makeNot(gh.getPlayerXVar(game.getWidth() - 1))),
							makePlayerMove(1,0));

		// direction=UP & y>0 -> next(y) = y+(-1) & next(x) = x
		long moveUp = makeImplies(
							makeAnd(gh.getDirectionVariable(Direction.UP), makeNot(gh.getPlayerYVar(0))),
							makePlayerMove(0,-1));

		// direction=DOWN & y<yDimen+(-1) -> next(y) = y+1 & next(x) = x
		long moveDown = makeImplies(
							makeAnd(gh.getDirectionVariable(Direction.DOWN), makeNot(gh.getPlayerYVar(game.getHeight() - 1))),
							makePlayerMove(0,1));
		long result = ref(makeAnd(makeAnd(makeAnd(moveLeft, moveRight), moveUp), moveDown));
		enableGC();
		return result;
	}

	protected Cube makePlayerMove(int dX, int dY){
		long xResult = getFalse();
		for(int x = Math.max(0, -dX); x < game.getWidth() - Math.max(0, dX); x++) {
			long moveForThisX = getTrue();
			for(int varX = 0; varX < game.getWidth(); varX++) {
				moveForThisX = makeAnd(moveForThisX,
						varX == x ? gh.getPlayerXVar(varX) : makeNot(gh.getPlayerXVar(varX)));
				moveForThisX = makeAnd(moveForThisX,
						varX + dX == x ? gh.getPlayerXVarPrime(varX) : makeNot(gh.getPlayerXVarPrime(varX)));
			}
			xResult = makeOr(xResult, moveForThisX);
		}
		long yResult = getFalse();
		for(int y = Math.max(0, -dY); y < game.getHeight() - Math.max(0, dY); y++) {
			long moveForThisY = getTrue();
			for(int varY = 0; varY < game.getHeight(); varY++) {
				moveForThisY = makeAnd(moveForThisY,
						varY == y ? gh.getPlayerYVar(varY) : makeNot(gh.getPlayerYVar(varY)));
				moveForThisY = makeAnd(moveForThisY,
						varY + dY == y ? gh.getPlayerYVarPrime(varY) : makeNot(gh.getPlayerYVarPrime(varY)));
			}
			yResult = makeOr(yResult, moveForThisY);
		}
		return makeAnd(xResult, yResult);
	}

//	(x=next(player.x) & y=next(player.y) ->
//		(player.direction=LEFT & x>0 -> next(x) = x+(-1) & next(y) = y) &
//		(player.direction=RIGHT & x<xDimen+(-1) -> next(x) = x+1 & next(y) = y) &
//		(player.direction=UP & y>0 -> next(y) = y+(-1) & next(x) = x) &
//		(player.direction=DOWN & y<yDimen+(-1) -> next(y) = y+1 & next(x) = x)
//	) &
//	(!(x = next(player.x) & y = next(player.y)) -> x = next(x) & y = next(y))
	protected Cube makeBoxMove() {
		List<int[]> boxes = game.getBoxes();
		long result = getTrue();
		for(int i = 0; i < boxes.size(); i++) {
			disableGC();
			Runner.sleepPrint(String.format("d: box %d/%d",i,boxes.size()));
			long playerBoxCollision = makePlayerBoxCollision(i);
			long boxMove = getTrue();

			Runner.sleepPrint(String.format("d: box %d/%d LEFT",i,boxes.size()));
			long moveLeft = makeImplies(
					makeAnd(gh.getDirectionVariable(Direction.LEFT), makeNot(gh.getBoxX(i,0))),
					makeBoxMove(i,-1,0));
			boxMove=ref(makeAnd(moveLeft,boxMove));

			Runner.sleepPrint(String.format("d: box %d/%d RIGHT",i,boxes.size()));
			long moveRight = makeImplies(
					makeAnd(gh.getDirectionVariable(Direction.RIGHT), makeNot(gh.getBoxX(i,game.getWidth() - 1))),
					makeBoxMove(i,1,0));
			deref(boxMove);
			boxMove=ref(makeAnd(moveRight,boxMove));

			Runner.sleepPrint(String.format("d: box %d/%d UP",i,boxes.size()));
			long moveUp = makeImplies(
					makeAnd(gh.getDirectionVariable(Direction.UP), makeNot(gh.getBoxY(i,0))),
					makeBoxMove(i,0,-1));
			deref(boxMove);
			boxMove=ref(makeAnd(moveUp,boxMove));

			Runner.sleepPrint(String.format("d: box %d/%d DOWN",i,boxes.size()));
			long moveDown = makeImplies(
					makeAnd(gh.getDirectionVariable(Direction.DOWN), makeNot(gh.getBoxY(i,game.getHeight() - 1))),
					makeBoxMove(i,0,1));
			deref(boxMove);
			boxMove=ref(makeAnd(moveDown,boxMove));

			//long boxMove = makeAnd(makeAnd(makeAnd(moveLeft,moveRight), moveUp), moveDown);
			Runner.sleepPrint(String.format("d: box %d/%d Result",i,boxes.size()));

			long moveBoxAside = makeImplies(playerBoxCollision, boxMove);
			Runner.sleepPrint(String.format("d: box %d/%d Box d0",i,boxes.size()));
			long noBoxMovement = makeBoxMove(i,0,0);
			Runner.sleepPrint(String.format("d: box %d/%d moveatcollision",i,boxes.size()));
			long onlyMoveAtCollision = makeImplies(makeNot(playerBoxCollision), noBoxMovement);
			Runner.sleepPrint(String.format("d: box %d/%d total",i,boxes.size()));
			long total = makeAnd(moveBoxAside, onlyMoveAtCollision);
			Runner.sleepPrint(String.format("d: box %d/%d append",i,boxes.size()));
			deref(result);
			enableGC();
			result = ref(makeAnd(result, total));

		}
		ref(result);
		//enableGC();
		return result;
	}

	protected Cube makeBoxMove(int boxid, int dX, int dY){
		long xResult = getFalse();
		for(int x = Math.max(0, -dX); x < game.getWidth() - Math.max(0, dX); x++) {
			long moveForThisX = getTrue();
			for(int varX = 0; varX < game.getWidth(); varX++) {
				moveForThisX = makeAnd(moveForThisX,
						varX == x ? gh.getBoxX(boxid,varX) : makeNot(gh.getBoxX(boxid,varX)));
				moveForThisX = makeAnd(moveForThisX,
						varX + dX == x ? gh.getBoxXPrime(boxid,varX) : makeNot(gh.getBoxXPrime(boxid,varX)));
			}
			xResult = makeOr(xResult, moveForThisX);
		}
		long yResult = getFalse();
		for(int y = Math.max(0, -dY); y < game.getHeight() - Math.max(0, dY); y++) {
			long moveForThisY = getTrue();
			for(int varY = 0; varY < game.getHeight(); varY++) {
				moveForThisY = makeAnd(moveForThisY,
						varY == y ? gh.getBoxY(boxid,varY) : makeNot(gh.getBoxY(boxid,varY)));
				moveForThisY = makeAnd(moveForThisY,
						varY + dY == y ? gh.getBoxYPrime(boxid,varY) : makeNot(gh.getBoxYPrime(boxid,varY)));
			}
			yResult = makeOr(yResult, moveForThisY);
		}
		return makeAnd(xResult, yResult);
	}

	// box.x=next(player.x) & box.y=next(player.y)
	protected Cube makePlayerBoxCollision(int box) {
		long result = getTrue();
		for(int x = 0; x < game.getWidth(); x++) {
			result = makeAnd(result, makeEquals(gh.getBoxX(box, x), gh.getPlayerXVarPrime(x)));
		}
		for(int y = 0; y < game.getHeight(); y++) {
			result = makeAnd(result, makeEquals(gh.getBoxY(box, y), gh.getPlayerYVarPrime(y)));
		}
		return result;
	}

	protected Game getGame(){
		return game;
	}

	protected VarManager getvarManager(){
		return varman;
	}

}
