package ic3cub3.plf;

import ic3cub3.plf.cnf.*;

import java.util.*;

import lombok.Getter;

/**
 * A class representing a boolean literal
 */
public class Literal extends Formula{
	/* Used for unique literals */
	private static int LITCOUNT = 1;
	
	private final int id;
	@Getter
	private final boolean negated;
	private final boolean primed;
	@Getter
	private final boolean tseitin;
	
	public Literal(int id,boolean negated,boolean primed,boolean tseitin){
		assert(id>0);
		this.id=id;
		this.negated=negated;
		this.primed = primed;
		this.tseitin=tseitin;
	}
	
	public Literal(int id,boolean negated){
		this(id,negated,false,false);
	}
	
	public Literal(int id){
		this(id,false);
	}
	
	/**
	 * Instantiates a non-negated, non-tseitin, non-primed unique literal
	 */
	public Literal(){
		this(LITCOUNT);
		LITCOUNT+=2;

	}
	
	public Literal(boolean tseitin){
		this(LITCOUNT,false,false,tseitin);
		LITCOUNT+=2;
	}

	@Override
	public Literal not() {
		return new Literal(id,!negated,primed,tseitin);
	}
	
	@Override
	public String toString() {
		return (negated?"~":"")+getID()+(tseitin?"t":"");
	}
	
	@Override
	public Set<Integer> getVariables() {
		HashSet<Integer> result = new HashSet<>();
		result.add(getID());
		return result;
	}

	@Override
	public String getLogic2CNFString() {
		return (negated?"~":"")+"x"+getID();
	}

	@Override
	public Literal rename(int old, int replacement) {
		if(id==old){
			return new Literal(replacement,this.negated,primed,tseitin);
		}else{
			return this;
		}
	}
	
	/**
	 * Obtains the official id (not the internal id)
	 * @return the id of this literal or id+1 when getPrimed() holds
	 */
	public int getID(){
		return primed?this.id+1:this.id;
	}
	
	/**
	 * Obtains the DIMACS id
	 * @return the DIMACS integer representing this variable
	 */
	public int getDIMACSID(){
		return negated?-getID():getID();
	}
	
	protected int getInternalID(){
		return this.id;
	}
	
	public Literal getPrimed(){
		return new Literal(this.id,this.negated,true,tseitin);
	}
	
	public Literal getUnPrimed(){
		return new Literal(this.id,this.negated,false,tseitin);
	}
	
	public boolean isPrimed(){
		return primed;
	}

	@Override
	public TseitinCube toCNF() {
		return new TseitinCube(this, new ArrayList<>(1));
	}
	
	@Override
	public Set<Integer> getTseitinVariables() {
		HashSet<Integer> result = new HashSet<>();
		if(isTseitin()){
			result.add(getID());
		}
		return result;
	}
	
	
	public static int MAXID(){
		return LITCOUNT;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Literal && equals((Literal) obj);
	}
	
	public boolean equals(Literal l){
		return l.getID()==getID() && l.negated==negated;
	}
	
	@Override
	public int hashCode() {
		return getDIMACSID();
	}
	
	@Override
	public Cube toEquivalentCube() {
		return new Cube(this);
	}
}
