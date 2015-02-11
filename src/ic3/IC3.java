package ic3;

import java.util.*;

import plf.Formula;
import plf.Literal;
import plf.cnf.Cube;
import runner.Runner;
import sat.SATSolver;

public class IC3 {
	private final SATSolver satsolver;
	
	public IC3(SATSolver satsolver){
		this.satsolver=satsolver;
	}
	
	//TODO return cex or TS |= P
	public boolean check(Formula I, Formula T, Formula P,Formula NP){
		//check I => P
		if(satsolver.sat(I.and(P.not())).size()>0){
			System.out.println("I => P does not hold");
			return false;
		}else{
			System.out.println("I => P");
		}
		//check I ^ T => P
		System.out.println(satsolver.sat(I.and(T).implies(P)));
		if(satsolver.sat(I.and(T).and(P.not())).size()>0){
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
		Formula PPrime = P.getPrimed();
		Formula NPPrime = NP.getPrimed();
		while(true){
			ArrayDeque<Formula> addedClauses = new ArrayDeque<Formula>();
			PriorityQueue<ProofObligation> proofObligations = new PriorityQueue<ProofObligation>();
			//test Fk ^ T ^ ~p'
			List<? extends Formula> result = satsolver.sat(F.get(k).and(T).and(NPPrime),true);
			if(result.size()>0){
				System.out.println("F_k ^ T ^ ~p' satisfiable for k="+k);
				
				//obtain counterexample S and S'
				Formula s = result.get(0);
				System.out.println("s: "+s);
				proofObligations.add(new ProofObligation(s, k-1));
			}else{
				System.out.println("P satisfied");
			}
			while(proofObligations.size()>0){
				ProofObligation probl = proofObligations.remove();
				Formula s = probl.getCTI();
				Integer inductiveFrontier = findInductiveFrontier(probl,F,T,k);
				if(inductiveFrontier==null){
					System.out.println("Found counterexample to P: "+s);
					return false;
				}else{
					strengthen(s,F,inductiveFrontier,addedClauses);
					
					//aggressively check if the CTI is resolved in the first non-inductive frontier
					int nextfrontier = inductiveFrontier+1;
					if(nextfrontier<k){
						proofObligations.add(new ProofObligation(s, nextfrontier));
					}
				}
			}
			k++;
			
			F.add(P); //Fk = P
			System.out.println("K in creased to "+k);
			propagateClauses(T,F,addedClauses,k);
			if(hasFixpoint(F)){
				System.out.println(String.format("Fixpoint found at k=%d, TS |= P",k));
				return true;
			}
		}
	}

	//Find highest inductive Fi
	private Integer findInductiveFrontier(ProofObligation probl,List<Formula> F, Formula T, int k) {
		Integer result = null;
		Formula s = probl.getCTI();
		Formula sPrime = s.getPrimed();
		Formula nots = s.not();
		for(int i = k;i>=probl.getLevel();i--){
			Formula Fi = F.get(i);
			System.out.println("Check s "+s+" inductive at F"+i+";" +F.get(i));
			boolean inductive = satsolver.sat(Fi.and(nots).and(T).and(sPrime)).size()==0;
			if(inductive){
				System.out.println("S is inductive at i="+i);
				result = i;
				return i;
			}else{
				if(Runner.VERBOSE>1)System.out.println("S not inductive at i="+i);
			}
		}
		return result;
	}

	//Refine F1...Fi+1
	private void strengthen(Formula S,List<Formula> F, Integer inductiveFrontier,ArrayDeque<Formula> addedClauses) {
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

	//TODO fix implementation
	private void propagateClauses(Formula T,List<Formula> F,
			ArrayDeque<Formula> addedClauses,int k) {
		//Propagate clauses
		while(addedClauses.size()>0){
			Formula c = addedClauses.pop();
			//check whether clause can be propagated (F_k ^ c ^ T => c') satisfiable
			Formula notcprime = c.getPrimed().not();
			Formula Fk = F.get(k);
			boolean canbepropagated = satsolver.sat(Fk.and(c).and(T).and(notcprime)).size()==0;
			if(canbepropagated){
				//propagate
				F.set(k, F.get(k).and(c));
			}else{
				System.out.println("Clause "+c+" can  not be propagated");
				//return false;
			}
		}
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
		Cube result = new Cube();
		result.addLiteral(new Literal(3).not());
		return result.toFormula();
	}
}
