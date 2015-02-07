package plf;

import java.util.HashSet;
import java.util.Set;

public class Literal extends Formula{
	private static long counter = 1;
	
	private final long id;
	private final boolean negated;
	private final boolean primed;
	
	public Literal(long id,boolean negated,boolean primed){
		assert(id!=0);
		this.id=id;
		this.negated=negated;
		this.primed = primed;
	}
	
	public Literal(long id,boolean negated){
		this(id,negated,false);
	}
	
	public Literal(long id){
		this(id,false);
	}
	
	public Literal(){
		this(counter);
		counter+=2;
	}

	@Override
	public Literal not() {
		return new Literal(id,!negated,primed);
	}
	
	@Override
	public String toString() {
		return (negated?"~":"")+getID();
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
			return new Literal(replacement,this.negated,primed);
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
		return new Literal(this.id,this.negated,true);
	}
	
	public Literal getUnPrimed(){
		return new Literal(this.id,this.negated,false);
	}
	
	public boolean isPrimed(){
		return primed;
	}
}
