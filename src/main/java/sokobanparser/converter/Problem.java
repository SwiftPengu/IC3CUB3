package sokobanparser.converter;

import ic3cub3.plf.AndFormula;
import ic3cub3.plf.Formula;
import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.tests.ProblemSet;
import lombok.Getter;
import sokobanparser.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static ic3cub3.runner.Runner.printv;

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

    public ProblemSet getProblemSet(){
        return new ProblemSet(getInit(),getTrans(),getError().toFormula().not().toEquivalentCube(),getError());
    }

	/**
	 * Constructs a BDD representing the initial state
	 * @return referenced BDD representing the initial state
	 */
	protected Cube getInit(){
		Cube dir = makeUniqueDirection();
		Cube p_x = makePlayerAtX(game.getPlayerLocation()[0]);
		Cube p_y = makePlayerAtY(game.getPlayerLocation()[1]);
		Cube boxes = makeBoxes();
        printv(() -> "Init",1);
		return dir.and(p_x).and(p_y).and(boxes);
	}

	/**
	 * Construct the BDD representing the goal state
	 * After all, this is the error state we want to
	 * search for.
	 * @return referenced BDD
	 */
    protected Cube getError(){
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

        printv(() -> "Problem",1);
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
    protected Cube getTrans(){
        printv(() -> "Trans start",1);
		Cube result = makeWallsInvariant()
				.and(makeDirectionTrans())
				.and(makePlayerMove())
				.and(makeBoxMove());
		Optional<Cube> nbcinv = makeNoBoxCollisionInvariant();
		if(nbcinv.isPresent()){
			result = result.and(nbcinv.get());
		}
        printv(() -> "Trans end",1);
		return result;
	}

	/**
	 * Builds a BDD that specifies that the player can only move
	 * in a single direction for every step
	 * @return The reference to the BDD
	 */
	private Cube makeUniqueDirection() {
		return Arrays.stream(Direction.values()).map(dir -> Arrays.stream(Direction.values())
                	.map(dir2 -> dir2 == dir ? gh.getDirectionVariable(dir2) : gh.getDirectionVariable(dir2).not())
                	.map(l -> (Formula) l)
                	.reduce(Formula::and).get()
		).reduce(Formula::or)
				.get()
				.toEquivalentCube();
	}

	/**
	 * Obtain a BDD representing that the player is at location x
	 * @param x the location to represent
	 * @return a reference to a BDD representing that the player is at location x
	 */
	private Cube makePlayerAtX(int x){
		return IntStream.range(0,game.getWidth()).boxed()
				.map(i -> (i==x?gh.getPlayerXVar(i):gh.getPlayerXVar(i).not()))
				.map(Cube::new)
				.reduce(Cube::and).get();
	}

	//returns a referenced BDD representing that the player is at location y
	private Cube makePlayerAtY(int y){
		return IntStream.range(0,game.getHeight()).boxed()
				.map(i -> (i==y?gh.getPlayerYVar(i):gh.getPlayerYVar(i).not()))
				.map(Cube::new)
				.reduce(Cube::and).get();	}

	//returns a reference to a BDD representing all the boxes at the right locations
	private Cube makeBoxes() {
		List<int[]> boxes = game.getBoxes();

		Cube result = new Cube();
		for(int boxid = 0; boxid < boxes.size(); boxid++) {
			int[] box = boxes.get(boxid);
			int x = box[0];
			int y = box[1];

			for(int varX = 0; varX < game.getWidth(); varX++) {
				result.addLiteral(x == varX ? gh.getBoxX(boxid, varX) : gh.getBoxX(boxid, varX).not());
			}
			for(int varY = 0; varY < game.getHeight(); varY++) {
				result.addLiteral(y == varY ? gh.getBoxY(boxid, varY) : gh.getBoxY(boxid, varY).not());
			}
		}
		return result;
	}

	/**
	 * Construct the BDD representing that the player and all boxes
	 * are not allowed to be in a wall in the next state.
	 * This is an invariant.
	 * @return The referenced BDD.
	 */
	protected Cube makeWallsInvariant() {
		Formula result = null;

		//Store all the walls
		List<int[]> walls = new ArrayList<int[]>();
		for(int x = 0; x < game.getWidth(); x++) {
			for(int y = 0; y < game.getHeight(); y++) {
				if(game.getItemAt(x, y) == Game.FieldItem.WALL) {
					walls.add(new int[]{x,y});
				}
			}
		}

		//state that the next player location is on a wall or that the next box location is on a wall

		List<int[]> boxes = game.getBoxes();
		for(int[] wall:walls) {
			int x = wall[0];
			int y = wall[1];
			AndFormula nextplayerloc = new AndFormula(gh.getPlayerXVarPrime(x), gh.getPlayerYVarPrime(y));
			if(result==null){
				result = nextplayerloc;
			}else{
				result = result.or(nextplayerloc);
			}
			for(int boxid = 0; boxid < boxes.size(); boxid++) {
				result.or(new AndFormula(gh.getBoxXPrime(boxid, x), gh.getBoxYPrime(boxid, y)));
			}
		}
		assert(result!=null);
		return result.toEquivalentCube();
	}

	/**
	 * Construct the BDD representing that no pair of boxes
	 * is allowed to be in the same place.
	 * This is an invariant.
	 * @return The referenced BDD.
	 */
	protected Optional<Cube> makeNoBoxCollisionInvariant() {
		int numberOfBoxes = game.getBoxes().size();

		//Invariant is only needed when more than one box exists
		if(numberOfBoxes>1){
			return Optional.of(IntStream.range(0,numberOfBoxes).boxed().flatMap(i ->
					IntStream.range(i+1,numberOfBoxes).boxed().map(j -> makeBoxCollisionPair(i,j).not())
			).reduce(Formula::and).get().toEquivalentCube());
		}else{
			return Optional.empty();
		}
	}

	protected Formula makeBoxCollisionPair(int box1, int box2) {
		Formula result = null;
		for(int x = 0; x < game.getWidth(); x++) {
			Formula boxxequal = gh.getBoxXPrime(box1,x).iff(gh.getBoxXPrime(box2,x));
			if(result==null){
				result = boxxequal;
			}else{
				result = result.and(boxxequal);
			}
		}
		assert(result!=null);
		for(int y = 0; y < game.getHeight(); y++) {
			result = result.and(gh.getBoxYPrime(box1, y).iff(gh.getBoxYPrime(box2, y)));
		}
		return result;
	}

	/**
	 * Build a BDD that represents that the next state
	 * has a unique direction for the player
	 * @return The referenced BDD
	 */
	protected Cube makeDirectionTrans() {
		return makeUniqueDirection().getPrimed();
	}

	/**
	 * Build a BDD that represents the allowed moves of the player
	 * @return The references BDD
	 */
	protected Cube makePlayerMove() {
		// direction=LEFT & x>0 -> next(x) = x+(-1) & next(y) = y
		Cube result = new AndFormula(gh.getDirectionVariable(Direction.LEFT),gh.getPlayerXVar(0).not())
				.implies(makePlayerMove(-1, 0)).toEquivalentCube();

		// direction=RIGHT & x<xDimen+(-1) -> next(x) = x+1 & next(y) = y
		result = result.and(new AndFormula(gh.getDirectionVariable(Direction.RIGHT),gh.getPlayerXVar(game.getWidth()-1).not())
				.implies(makePlayerMove(1, 0)).toEquivalentCube());

		// direction=UP & y>0 -> next(y) = y+(-1) & next(x) = x
		result = result.and(new AndFormula(gh.getDirectionVariable(Direction.UP),gh.getPlayerYVar(0).not())
				.implies(makePlayerMove(0, -1)).toEquivalentCube());

		// direction=DOWN & y<yDimen+(-1) -> next(y) = y+1 & next(x) = x
		result = result.and(new AndFormula(gh.getDirectionVariable(Direction.UP), gh.getPlayerYVar(game.getHeight() - 1).not())
				.implies(makePlayerMove(0, 1)).toEquivalentCube());
		return result;
	}

	protected Formula makePlayerMove(int dX, int dY){
		Formula xResult = null;
		for(int x = Math.max(0, -dX); x < game.getWidth() - Math.max(0, dX); x++) {
			Formula moveForThisX = null;
			for(int varX = 0; varX < game.getWidth(); varX++) {
				Literal xequal = varX == x ? gh.getPlayerXVar(varX) : gh.getPlayerXVar(varX).not();
				if(moveForThisX==null){
					moveForThisX=xequal;
				}else{
					moveForThisX = moveForThisX.and(xequal);
				}
				moveForThisX = moveForThisX.and(varX + dX == x ? gh.getPlayerXVarPrime(varX) : gh.getPlayerXVarPrime(varX).not());
			}
			if(xResult==null){
				xResult=moveForThisX;
			}else{
				xResult=xResult.or(moveForThisX);
			}
		}

		Formula yResult = null;
		for(int y = Math.max(0, -dY); y < game.getHeight() - Math.max(0, dY); y++) {
			Formula moveForThisY = null;
			for(int varY = 0; varY < game.getHeight(); varY++) {
				Literal yequal = varY == y ? gh.getPlayerYVar(varY) : gh.getPlayerYVar(varY).not();
				if(moveForThisY==null){
					moveForThisY=yequal;
				}else{
					moveForThisY = moveForThisY.and(yequal);
				}
				moveForThisY = moveForThisY.and(varY + dY == y ? gh.getPlayerYVarPrime(varY) : gh.getPlayerYVarPrime(varY).not());
			}
			if(yResult==null){
				yResult=moveForThisY;
			}else {
				yResult = yResult.or(moveForThisY);
			}
		}
		assert(xResult!=null && yResult!=null);
		return xResult.and(yResult);
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
		Cube result = new Cube();
		for(int i = 0; i < boxes.size(); i++) {
			Formula playerBoxCollision = makePlayerBoxCollision(i);
			Cube moveLeft = new AndFormula(gh.getDirectionVariable(Direction.LEFT), gh.getBoxX(i,0).not()).implies(makeBoxMove(i,-1,0)).toEquivalentCube();
			Cube moveRight = new AndFormula(gh.getDirectionVariable(Direction.RIGHT), gh.getBoxX(i,game.getWidth() - 1).not()).implies(makeBoxMove(i,1,0)).toEquivalentCube();
			Cube moveUp = new AndFormula(gh.getDirectionVariable(Direction.UP), gh.getBoxY(i,0).not()).implies(makeBoxMove(i,0,-1)).toEquivalentCube();
			Cube moveDown = new AndFormula(gh.getDirectionVariable(Direction.DOWN), gh.getBoxY(i,game.getHeight() - 1).not()).implies(makeBoxMove(i,0,1)).toEquivalentCube();

			Cube boxMove = moveLeft.and(moveRight).and(moveUp).and(moveDown);
			Formula moveBoxAside = playerBoxCollision.implies(boxMove.toFormula());
			Formula noBoxMovement = makeBoxMove(i,0,0);
			Formula onlyMoveAtCollision = playerBoxCollision.not().implies(noBoxMovement);
			Cube total = moveBoxAside.and(onlyMoveAtCollision).toEquivalentCube();
			result = result.and(total);
		}
		return result;
	}

	protected Formula makeBoxMove(int boxid, int dX, int dY){
		Formula xResult = null;
		for(int x = Math.max(0, -dX); x < game.getWidth() - Math.max(0, dX); x++) {
			Formula moveForThisX = null;
			for(int varX = 0; varX < game.getWidth(); varX++) {
				Formula xmatch = varX == x ? gh.getBoxX(boxid,varX) : gh.getBoxX(boxid,varX).not();
				if(moveForThisX==null){
					moveForThisX = xmatch;
				} else {
					moveForThisX = moveForThisX.and(xmatch);
				}
				moveForThisX = moveForThisX.and(varX + dX == x ? gh.getBoxXPrime(boxid,varX) : gh.getBoxXPrime(boxid,varX).not());
			}
			if(xResult==null){
				xResult=moveForThisX;
			}else{
				xResult=xResult.or(moveForThisX);
			}
		}

        Formula yResult = null;
		for(int y = Math.max(0, -dY); y < game.getHeight() - Math.max(0, dY); y++) {
			Formula moveForThisY = null;
			for(int varY = 0; varY < game.getHeight(); varY++) {
                Formula ymatch = varY == y ? gh.getBoxY(boxid,varY) : gh.getBoxY(boxid,varY).not();
                if(moveForThisY==null){
                    moveForThisY=ymatch;
                }else{
                    moveForThisY=moveForThisY.and(ymatch);
                }
				moveForThisY = moveForThisY.and(varY + dY == y ? gh.getBoxYPrime(boxid,varY) : gh.getBoxYPrime(boxid,varY).not());
			}
            if(yResult==null){
                yResult=moveForThisY;
            }else{
                yResult=yResult.or(moveForThisY);
            }
		}
        assert(xResult!=null && yResult!=null);
		return xResult.and(yResult);
	}

	// box.x=next(player.x) & box.y=next(player.y)
	protected Formula makePlayerBoxCollision(int box) {
		Formula result = null;
		for(int x = 0; x < game.getWidth(); x++) {
			Formula playerboxcol = gh.getBoxX(box,x).iff(gh.getPlayerXVarPrime(x));
			if(result==null){
				result=playerboxcol;
			}else {
				result = result.and(playerboxcol);
			}
		}
		assert (result != null);
		for (int y = 0; y < game.getHeight(); y++) {
			result = result.and(gh.getBoxY(box, y).iff(gh.getPlayerYVarPrime(y)));
		}
		return result;
	}

}
