package sokobanparser;


import java.util.ArrayList;
import java.util.List;

/**
 * Models a Sokoban game
 * @author Rick Hindriks - s1086057 & Thomas Neele - s1122770
 * 
 */
public class Game {
	private final List<List<FieldItem>> field;
	private int width;// width is variable depending on the largest line
	private int[] player;
	private final List<int[]> boxes = new ArrayList<int[]>();

	/**
	 * Constructs an empty field
	 */
	public Game() {
		this.field = new ArrayList<List<FieldItem>>();
	}

	/**
	 * Adds a line to the board
	 * @param line a list of fielditems to add for each column on the line
	 */
	public void addLine(List<FieldItem> line) {
		this.width = Math.max(this.width, line.size());
		this.field.add(line);
	}

	/**
	 * The current height of the board
	 * @return the height of the board
	 */
	public int getHeight() {
		return this.field.size();
	}

	/**
	 * The current width of the board
	 * @return the width of the broadest
	 */
	public int getWidth() {
		return this.width;
	}

	@Override
	public String toString() {
		return String.format("<Game (%d,%d) p:(%d,%d) %s", getWidth(),getHeight(), getPlayerLocation()[0],getPlayerLocation()[1],this.field.toString());
	}

	public String transposedString(){
		String result = "[";
		for(int x = 0;x<getWidth();x++){
			result = result+"[";
			int y = 0;
			for(List<FieldItem> row:this.field){
				if(row.size()-1<x){
					//end of line, so put empty
					result+=FieldItem.EMPTY;
				}else{
					result+=row.get(x);
				}
				if(y!=this.field.size()-1){
					result+=",";
				}
				y++;
			}
			result+="]";
			if(x!=getWidth()-1) {
				result+=",";
			}
		}
		result+="]";
		return result;
	}

	public List<List<FieldItem>> getField() {
		return this.field;
	}

	public void setPlayer(int x,int y){
		this.player = new int[]{x,y};
	}

	public void addBox(int x, int y){
		this.boxes.add(new int[]{x,y});
	}

	public List<int[]> getBoxes(){
		return this.boxes;
	}
	
	public void moveBox(int index,int[] newlocation){
		getBoxes().set(index, newlocation);
	}

	public int[] getPlayerLocation(){
		return this.player;
	}
	
	/**
	 * Obtain the FieldItem at the specified location if it exists (this excludes boxes and the player)
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return null if the location is invalid (not on the playable field) else, the fielditem at location (x,y)
	 */
	public FieldItem getItemAt(int x, int y){
		if(y>=0 && y<getHeight()){
			List<FieldItem> row = getField().get(y);
			if(x>=0 && x<row.size()){
				return row.get(x);
			}
		}
		return null;
	}
	
	/**
	 * Checks whether this Game is in a winning state
	 * @requires the amount of boxes is equal to the amount of dots
	 * @return true when all boxes are on a dot
	 */
	public boolean isDone(){
		for(int[] box:getBoxes()){
			//check whether a dot equals the item at the location of the box
			if(!FieldItem.DOT.equals(getItemAt(box[0],box[1]))){
				return false;
			}
		}
		return true;
	}

	/**
     * An item on the board, we do not need values for the player or the crates, those are saved separately
     * @author Rick Hindriks - s1086057 & Thomas Neele - s1122770
     *
     */
    public static enum FieldItem {
        EMPTY,DOT,WALL
    }
}