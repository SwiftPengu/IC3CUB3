package ic3cub3.ic3;


import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.runner.Runner;
import ic3cub3.sat.SATSolver;

import java.util.*;

public class IC3 {
	private final SATSolver satsolver;
	
	public IC3(SATSolver satsolver){
		this.satsolver=satsolver;
	}
	
	public boolean check(Cube I, Cube T, Cube P,Cube NP){
		//
		// Establish invariants
		
		//check I => P
		if(Runner.VERBOSE>0)System.out.println("Check I => P");
		if(satsolver.sat(I.and(NP)).size()>0){
			if(Runner.VERBOSE>0)System.out.println("I => P does not hold");
			return false;
		}else{
			if(Runner.VERBOSE>0)System.out.println("I => P");
		}
		//check I ^ T => P
		if(satsolver.sat(I.and(T).and(NP)).size()>0){
			if(Runner.VERBOSE>0)System.out.println("I ^ T => P does not hold");
			return false;
		}else{
			if(Runner.VERBOSE>0)System.out.println("I ^ T => P");
		}
		
		//init frontier sets
		List<Cube> F = new ArrayList<Cube>();
		F.add(I);//F0 = I
		int k = 1;
		F.add(P);//F1 = P
		
		//init ~P'
		Cube NPPrime = NP.getPrimed();
		
		//Start checking
		PriorityQueue<ProofObligation> proofObligations = new PriorityQueue<ProofObligation>();
		
		//We either give a counterexample, or prove that P holds
		while(true){
			Set<Clause> addedClauses = new HashSet<Clause>();
			
			//test Fk ^ T ^ ~p'
			List<Cube> result = satsolver.sat(F.get(k).and(T).and(NPPrime),true);
			if(result.size()>0){
				if(Runner.VERBOSE>0)System.out.println("F_k ^ T ^ ~p' satisfiable for k="+k);
				
				//obtain counterexample S and S'
				Cube s = result.get(0);
				if(Runner.VERBOSE>0)System.out.println("s: "+s);
				proofObligations.add(new ProofObligation(s, k-1));
			}else{
				if(Runner.VERBOSE>0)System.out.println("P satisfied");
			}
			
			//Solve all proof obligations for given k
			while(proofObligations.size()>0){
				ProofObligation probl = proofObligations.remove();
				System.out.println("Attempting to prove: "+probl);
				Cube s = probl.getCTI();
				InductiveFrontier inductiveFrontier = findInductiveFrontier(probl,F,T,k);
				if(inductiveFrontier.level==null){
					System.out.println("Found counterexample to P: "+s);
					System.out.println("Trace: "+proofObligations);
					return false;
				}else{
					strengthen(s,F,T,inductiveFrontier.level,addedClauses);
					
					//'aggressively' check if the CTI is resolved in the first non-inductive frontier
					int nextFrontier = inductiveFrontier.level+1;
					//TODO check whether current badState is resolved!
					boolean ctiStillExists = satsolver.sat(F.get(k).and(T).and(NPPrime),true).size()>0;
					if(ctiStillExists){
						System.out.println("CTI still exists");
						//cti is not yet resolved, so attempt to resolve it again
						//if the possibility of predecessors of s exists, check for such states
						proofObligations.add(new ProofObligation(s, nextFrontier));
						if(inductiveFrontier.level<k-1){
							System.out.println(nextFrontier+"; "+F.size());
							proofObligations.add(new ProofObligation(inductiveFrontier.counterexample, inductiveFrontier.level));
						}
					}
					
				}
			}
			
			//No more counterexamples, and no solution yet, so increase k.
			k++;
			F.add(P); //Fk = P
			System.out.println("k increased to "+k);
			propagateClauses(T,F,addedClauses,k);
			if(hasFixpoint(F)){
				return true;
			}
		}
	}

	//Find highest inductive Fi
	//TODO only search up to k-2
	private InductiveFrontier findInductiveFrontier(ProofObligation probl,List<Cube> F, Cube T, int k) {
		InductiveFrontier result = new InductiveFrontier();
		Cube s = probl.getCTI();
		Cube sPrime = s.getPrimed();
		Clause nots = s.not();
		for(int i = k;i>=0;i--){
			Cube Fi = F.get(i);
			if(Runner.VERBOSE>0)System.out.println("Check s "+s+" inductive at F"+i+";" +F.get(i));
			List<Cube> cex = satsolver.sat(Fi.and(nots).and(T).and(sPrime),true);
			if(cex.size()==0){ //no counterexample
				if(Runner.VERBOSE>0)System.out.println("S is inductive at i="+i);
				result.level = i;
				return result;
			}else{
				result.counterexample=cex.get(0);
				if(Runner.VERBOSE>0)System.out.println("S not inductive at i="+i);
			}
		}
		return result;
	}

	//Refine F1...Fi+1
	private void strengthen(Cube S,List<Cube> F, Cube T,Integer inductiveFrontier,Set<Clause> addedClauses) {
		//first obtain a minimal inductive subclause
		Clause c = MIC(S, F.get(0), T, F.get(inductiveFrontier));
		if(Runner.VERBOSE>0)System.out.println("MIC: "+c);
		
		addedClauses.add(c);
		//add c to F1..Fi+1
		for(int i = 1;i<=inductiveFrontier+1 && i<F.size();i++){
			if(!F.get(i).getClauses().contains(c)){
				if(Runner.VERBOSE>0)System.out.println("F"+i+" was: "+F.get(i));
				F.set(i, F.get(i).and(c));
				if(Runner.VERBOSE>0)System.out.println("F"+i+" becomes: "+F.get(i));
			}else{
				System.out.println("F"+i+" remains unchanged");
			}
		}
	}

	private void propagateClauses(Cube T,List<Cube> F,
			Set<Clause> addedClauses,int k) {
		//Propagate clauses
		if(Runner.VERBOSE>0)System.out.println("Propagating clauses: "+addedClauses);
		for(Clause clause:addedClauses){
			//check whether clause can be propagated (F_i ^ T  => c') satisfiable
			//<=> F_i ^ T ^ ~c' unsat
			for(int i = 0;i<k;i++){
				Cube frontier = F.get(i);
				Cube nextfrontier = F.get(i+1);
				Cube notcprime = clause.getPrimed().not();
				if(!nextfrontier.getClauses().contains(clause)){
					if(Runner.VERBOSE>0)System.out.printf("Test if %s can be propagated from %s(%d) to %s(%d)\n", clause,frontier,i,nextfrontier,i+1);
					boolean canbepropagated = satsolver.sat(frontier.and(clause).and(T).and(notcprime)).size()==0;
					if(canbepropagated){
						nextfrontier.addClause(clause);
						if(Runner.VERBOSE>0)System.out.println("Yes");
					}else{
						if(Runner.VERBOSE>0)System.out.println("No");
					}
				}
				
			}
		}
	}

	/**
	 * Tests whether two formulae in f are equal
	 * @param F a list of formulae
	 * @return true if there exist two formulae f1 and f2 for which f1 <=> f2 is satisfiable
	 */
	public boolean hasFixpoint(List<Cube> F) {
		//TODO simplify f2 first
		for(int f1 = 0;f1<F.size()-1;f1++){
			int f2 = f1+ 1;
			//compare the two formulae
			boolean equal = F.get(f1).equals(F.get(f2));
			if(equal){
				System.out.println(String.format("Fixpoint found at F%d and F%d, TS |= P",f1,f2));
				System.out.println(F.get(f1));
				return true;
			}
		}
		if(Runner.VERBOSE>0)System.out.println("No fixpoint: "+F);
		return false;
		
		//implementation which asks the SAT solver:
		//boolean equal = satsolver.sat(f.get(f1).toFormula().iff(f.get(f2).toFormula()).tseitinTransform()).size()>0;
	}
		
	/**
	 * Finds a minimally inductive clause relative to F (F ^ T ^ ~result => ~result') given a negated counterexample
	 * @param notcex the negated counterexample
	 * @param F the frontier set which the result should be relatively inductive to
	 * @return a minimally inductive clause which is inductive relative to F
	 */
	public Clause MIC(final Cube cex,final Cube I,final Cube T,final Cube F){
		//assert notcex is inductive (F ^ ~s ^ T => ~s') satisfiable
		// <=> ~(F ^ ~s ^ T => ~s') <=> (F ^ ~s ^ T ^ s') unsatisfiable
		if(Runner.VERBOSE>0)System.out.println("MIC on: "+cex);
		Clause notcex = cex.not();
		if(Runner.VERBOSE>1)System.out.println(notcex);
		assert(satsolver.sat(F.and(notcex).and(T).and(cex.getPrimed())).size()==0) : "MIC: ~cex is not inductive on F";
		
		Clause result = notcex.clone(); //~s is the maximum inductive clause, return this if all else fails
		ArrayList<Literal> literals = new ArrayList<Literal>(result.getLiterals());
		//visit literals in random order
		Collections.shuffle(literals);
		
		//drop a literal
		for (Literal l:literals){
			boolean copiedold = true; //result might have been refined in the meantime
			Clause rhat = new Clause(); //result - l
			for(Literal newlit:result.getLiterals()){
				if(newlit!=l){
					rhat.addLiteral(newlit);
				}else{
					copiedold=false; //we skipped a literal, so we didnt produce a copy
				}
			}
			if(!copiedold){
				//apply DOWN to rhat if it is not empty
				if(rhat.getLiterals().size()>0){
					Clause down = down(rhat,I,T,F);
					if(down!=null){
						result = down;
					}
				}
			}
		}
		return result;
	}
	
	private Clause down(Clause rhat,Cube I,Cube T, Cube Fi){
		Cube notrhat = rhat.not();
		if(Runner.VERBOSE>1)System.out.println("Down on: "+rhat);
		//test initiation (are bad states reachable from I?): ~(I => rhat) unsat <=> I ^ ~rhat satisfiable
		if(satsolver.sat(I.and(notrhat)).size()>0){
			if(Runner.VERBOSE>1) System.out.println("Initiation failed");
			return null;
		}
		
		//rhat => ~s
		//test consecution (attempt to obtain an unsat proof for ~(Fi ^ rhat ^ T => rhat') <=> Fi ^ rhat ^ T ^ ~rhat'
		List<? extends Cube> counterexamples = satsolver.sat(Fi.and(T).and(rhat).and(notrhat.getPrimed()),true);
		if(counterexamples.size()==0){
			if(Runner.VERBOSE>1)System.out.println(rhat+" is inductive!");
			return rhat;
		}else{
			//TODO use counterexample to refine rhat (rhat = rhat without literals not in ~cex
			return null;
		}
	}
	
	private class InductiveFrontier{
		//the level at which ~s becomes inductive
		public Integer level = null;
		
		//counterexample of the next frontier if it is not s
		public Cube counterexample = null;
	}
}
