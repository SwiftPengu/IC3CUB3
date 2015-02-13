package plf;

import java.util.*;

import plf.cnf.Clause;
import plf.cnf.TseitinCube;

public class Literal extends Formula{
	private static long counter = 1;
	
	private final long id;
	private final boolean negated;
	private final boolean primed;
	private final boolean tseitin;
	
	public Literal(long id,boolean negated,boolean primed,boolean tseitin){
		assert(id!=0);
		this.id=id;
		this.negated=negated;
		this.primed = primed;
		this.tseitin=tseitin;
	}
	
	public Literal(long id,boolean negated){
		this(id,negated,false,false);
	}
	
	public Literal(long id){
		this(id,false);
	}
	
	public Literal(){
		this(counter);
		counter+=2;
	}
	
	public Literal(boolean tseitin){
		this(counter,false,false,true);
		counter+=2;
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
	public Set<Long> getVariables() {
		HashSet<Long> result = new HashSet<Long>();
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
	public long getID(){
		return primed?this.id+1:this.id;
	}
	
	/**
	 * Obtains the DIMACS id
	 * @return the DIMACS integer representing this variable
	 */
	public long getDIMACSID(){
		return negated?-getID():getID();
	}
	
	protected long getInternalID(){
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
	public Set<Long> getTseitinVariables() {
		HashSet<Long> result = new HashSet<Long>();
		if(isTseitin()){
			result.add(getID());
		}
		return result;
	}
}
