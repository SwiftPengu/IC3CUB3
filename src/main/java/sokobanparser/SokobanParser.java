package sokobanparser;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * The sokoban screen parser
 * @author Rick Hindriks - s1086057 & Thomas Neele - s1122770
 *
 */
public class SokobanParser {

	/**
	 * Parses a single screen
	 * @param f the filename
	 * @return a Game modelling the parsed screen
	 * @throws FileNotFoundException when the file is not found
	 */
	public static Game parseScreen(File f) throws FileNotFoundException {
		Scanner sc = new Scanner(f);
		Game result = new Game();
		for(int y=0;sc.hasNextLine();y++) {
			String linetext = sc.nextLine();
			LinkedList<Game.FieldItem> line = parseLine(result,y,linetext);
			result.addLine(line);
		}
		sc.close();
		return result;
	}

	/**
	 * Parses a single line
	 * @param game the model to which the line should be added
	 * @param y the height of the line
	 * @param line a text representation of the line
	 * @return a list of model objects which represents the contents of this line
	 */
	private static LinkedList<Game.FieldItem> parseLine(Game game,int y,String line) {
		LinkedList<Game.FieldItem> result = new LinkedList<Game.FieldItem>();
		for (int x = 0; x < line.length(); x++) {
			char c = line.charAt(x);
			switch (c) {
			case '#': //WALL
				result.add(Game.FieldItem.WALL);
				break;
			case '$': //BOX
				game.addBox(x, y);
				result.add(Game.FieldItem.EMPTY);
				break;
			case '@': //PLAYER
				game.setPlayer(x, y);
				result.add(Game.FieldItem.EMPTY);
				break;
			case '.': //GOAL
				result.add(Game.FieldItem.DOT);
				break;
			case ' ': //EMPTY
				result.add(Game.FieldItem.EMPTY);
				break;
			case '*': //BOX on GOAL
				game.addBox(x,y);
				result.add(Game.FieldItem.DOT);
				break;
			case '+': //PLAYER on GOAL
				game.setPlayer(x, y);
				result.add(Game.FieldItem.DOT);
				break;
			default:
				throw new RuntimeException("Unknown character encountered: " + c + " in line " + line);
			}
		}
		return result;
	}
}
