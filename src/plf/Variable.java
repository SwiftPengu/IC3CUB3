package plf;

import java.util.HashSet;
import java.util.Set;

public class Variable extends Formula{
	private final int id;
	private final boolean negated;
	
	public Variable(int id,boolean negated){
		assert(id!=0);
		this.id=id;
		this.negated=negated;
	}
	
	public Variable(int id){
		this(id,false);
	}

	@Override
	public Formula not() {
		return new Variable(id,!negated);
	}
	
	@Override
	public String toString() {
		return (negated?"~":"")+id;
	}
	
	@Override
	public Set<Integer> getVariables() {
		HashSet<Integer> result = new HashSet<Integer>();
		result.add(this.id);
		return result;
	}

	@Override
	public String getLogic2CNFString() {
		return (negated?"~":"")+"x"+id;
	}

	@Override
	public Formula rename(int old, int replacement) {
		if(id==old){
			return new Variable(replacement,this.negated);
		}else{
			return this;
		}
	}
}
