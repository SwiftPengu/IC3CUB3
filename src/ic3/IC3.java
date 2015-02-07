package ic3;

import java.util.*;

import plf.Formula;
import plf.Literal;
import runner.Runner;
import sat.SATSolver;


public class IC3 {
	private final SATSolver satsolver;
	
	public IC3(SATSolver satsolver){
		this.satsolver=satsolver;
	}
	
	public boolean check(Formula I, Formula T, Formula P){	
		//check I => P
		if(satsolver.solve(I.implies(P).not()).size()>0){
			System.out.println("I => P does not hold");
			return false;
		}else{
			System.out.println("I => P");
		}
		//check I ^ T => P
		if(satsolver.solve(I.and(T).implies(P).not()).size()>0){
			System.out.println("I ^ T => P does not hold");
			return false;
		}else{
			System.out.println("I ^ T => P");
		}
		
		//init frontier sets
		List<Formula> F = new ArrayList<Formula>();
		F.add(I);//F0 = I
		int k = 1;
		F.add(P);//F1 = P
		
		//init P'
		Formula pPrime = P.getPrimed();
		
		while(true){
			boolean ctifound = true;
			ArrayDeque<Formula> addedClauses = new ArrayDeque<Formula>();
			while(ctifound){
				ctifound = false;
				//test Fk ^ T ^ ~p'
				List<? extends Formula> result = satsolver.solve(F.get(k).and(T).and(pPrime.not()),true);
				if(result.size()>0){
					System.out.println("F_k ^ T ^ ~p' satisfiable for k="+k);
					ctifound=true;
					
					//obtain counterexample S and S'
					Formula s = result.get(0);
					System.out.println("s: "+s);
					
					Integer inductiveFrontier = findInductiveFrontier(F,s,T,k);
					if(inductiveFrontier==null){
						System.out.println("Found counterexample to P: "+s);
						return false;
					}else{
						refine(s,F,inductiveFrontier,addedClauses);
					}
				}
			}
			k++;
			F.add(P); //Fk = P
			boolean propagated = propagateClauses(T,F,addedClauses,k);
			if(!propagated){
				return false;
			}
			if(hasFixpoint(F)){
				System.out.println(String.format("Fixpoint found at k=%d, TS |= P",k));
				return true;
			}
		}
	}

	//Find highest inductive Fi
	private Integer findInductiveFrontier(List<Formula> F, Formula s,Formula T, int k) {
		Integer result = null;
		Formula sPrime = s.getPrimed();
		for(int i = k;i>=0;i--){
			Formula Fi = F.get(i);
			System.out.println("Check s "+s+" inductive at F"+i+";" +F.get(i));
			boolean inductive = satsolver.solve(Fi.and(s.not()).and(T).implies(sPrime.not()).not()).size()==0;
			if(inductive){
				System.out.println("S is inductive at i="+i);
				result = i;
				return i;
			}else{
				if(Runner.VERBOSE>1)System.out.println("No");
			}
		}
		return result;
	}

	//Refine F1...Fi+1
	private void refine(Formula S,List<Formula> F, Integer inductiveFrontier,ArrayDeque<Formula> addedClauses) {
		//first obtain a minimal inductive subclause
		Formula c = MIC(S);
		System.out.println("MIC: "+c);
		
		addedClauses.add(c);
		//add c to F1..Fi+1
		int i = 1;
		do{
			F.set(i, F.get(i).and(c));
			System.out.println("F"+i+" becomes: "+F.get(i));
			i++;
		}while(i<inductiveFrontier+1);
	}

	private boolean propagateClauses(Formula T,List<Formula> F,
			ArrayDeque<Formula> addedClauses,int k) {
		//Propagate clauses
		while(addedClauses.size()>0){
			Formula c = addedClauses.pop();
			//check whether clause can be propagated (F_k ^ c ^ T => c') satisfiable
			Formula cprime = c.getPrimed();
			boolean canbepropagated = satsolver.solve(F.get(k).and(c).and(T).implies(cprime).not()).size()==0;
			if(canbepropagated){
				//propagate
				F.set(k, F.get(k).and(c));
			}else{
				System.out.println("Clause "+c+" can not be propagated");
				return false;
			}
		}
		return true;
	}

	/**
	 * Tests whether two formulae in f are equal
	 * @param f a list of formulae
	 * @return true if there exist two formulae f1 and f2 for which f1 <=> f2 is satisfiable
	 */
	public boolean hasFixpoint(List<Formula> f) {
		for(int f1 = 0;f1<f.size()-1;f1++){
			int f2 = f1+ 1;
			//compare the two formulae
			boolean equal = f.get(f1).equal(f.get(f2), satsolver);
			if(equal)return true;
		}
		return false;
	}
	
	//TODO implement MIC algorithm
	private Formula MIC(Formula S){
		return new Literal(3).not();
	}
}
