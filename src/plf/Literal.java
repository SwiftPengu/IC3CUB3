package plf;

import java.util.*;

import plf.cnf.Clause;
import plf.cnf.TseitinCube;

public class Literal extends Formula{
	private static int LITCOUNT = 1;
	
	private final int id;
	private final boolean negated;
	private final boolean primed;
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
	
	public Literal(){
		this(LITCOUNT);
		LITCOUNT+=2;
		
	}
	
	public Literal(boolean tseitin){
		this(LITCOUNT,false,false,true);
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
		HashSet<Integer> result = new HashSet<Integer>();
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
	 * @return
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
	
	public boolean isTseitin() {
		return tseitin;
	}

	@Override
	public TseitinCube toCNF() {
		TseitinCube result = new TseitinCube(this, new ArrayList<Clause>(1));
		//result.addLiteral(this);
		return result;
	}
	
	@Override
	public Set<Integer> getTseitinVariables() {
		HashSet<Integer> result = new HashSet<Integer>();
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
		if(obj instanceof Literal){
			return equals((Literal)obj);
		}else{
			return false;
		}
	}
	
	public boolean equals(Literal l){
		return l.getID()==getID() && l.negated==negated;
	}
	
	@Override
	public int hashCode() {
		return getDIMACSID();
	}
}
