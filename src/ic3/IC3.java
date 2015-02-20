package ic3;

import java.util.*;

import plf.Literal;
import plf.cnf.Clause;
import plf.cnf.Cube;
import runner.Runner;
import sat.SATSolver;

public class IC3 {
	private final SATSolver satsolver;
	
	public IC3(SATSolver satsolver){
		this.satsolver=satsolver;
	}
	
	//TODO return cex or TS |= P
	public boolean check(Cube I, Cube T, Cube P,Cube NP){
		//check I => P
		System.out.println("Check I => P");
		if(satsolver.sat(I.and(NP)).size()>0){
			System.out.println("I => P does not hold");
			return false;
		}else{
			System.out.println("I => P");
		}
		//check I ^ T => P
		if(satsolver.sat(I.and(T).and(NP)).size()>0){
			System.out.println("I ^ T => P does not hold");
			return false;
		}else{
			System.out.println("I ^ T => P");
		}
		
		//init frontier sets
		List<Cube> F = new ArrayList<Cube>();
		F.add(I);//F0 = I
		int k = 1;
		F.add(P);//F1 = P
		
		//init ~P'
		Cube NPPrime = NP.getPrimed();
		while(true){
			ArrayDeque<Clause> addedClauses = new ArrayDeque<Clause>();
			PriorityQueue<ProofObligation> proofObligations = new PriorityQueue<ProofObligation>();
			//test Fk ^ T ^ ~p'
			List<Cube> result = satsolver.sat(F.get(k).and(T).and(NPPrime),true);
			if(result.size()>0){
				System.out.println("F_k ^ T ^ ~p' satisfiable for k="+k);
				
				//obtain counterexample S and S'
				Cube s = result.get(0);
				System.out.println("s: "+s);
				proofObligations.add(new ProofObligation(s, k-1));
			}else{
				System.out.println("P satisfied");
			}
			while(proofObligations.size()>0){
				ProofObligation probl = proofObligations.remove();
				Cube s = probl.getCTI();
				Integer inductiveFrontier = findInductiveFrontier(probl,F,T,k);
				if(inductiveFrontier==null){
					System.out.println("Found counterexample to P: "+s);
					return false;
				}else{
					strengthen(s,F,T,inductiveFrontier,addedClauses);
					
					//'aggressively' check if the CTI is resolved in the first non-inductive frontier
					int nextfrontier = inductiveFrontier+1;
					if(nextfrontier<k){
						proofObligations.add(new ProofObligation(s, nextfrontier));
					}
				}
			}
			k++;
			
			F.add(P); //Fk = P
			System.out.println("k increased to "+k);
			propagateClauses(T,F,addedClauses,k);
			if(hasFixpoint(F)){
				System.out.println(String.format("Fixpoint found at k=%d, TS |= P",k));
				return true;
			}else{
				System.out.println(F);
			}
		}
	}

	//Find highest inductive Fi
	private Integer findInductiveFrontier(ProofObligation probl,List<Cube> F, Cube T, int k) {
		Integer result = null;
		Cube s = probl.getCTI();
		Cube sPrime = s.getPrimed();
		//TODO check if this can be done more efficiently
		Clause nots = s.not();
		for(int i = k;i>=probl.getLevel();i--){
			Cube Fi = F.get(i);
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
	private void strengthen(Cube S,List<Cube> F, Cube T,Integer inductiveFrontier,ArrayDeque<Clause> addedClauses) {
		//first obtain a minimal inductive subclause
		Clause c = MIC(S, F.get(0), T, F.get(inductiveFrontier));
		System.out.println("MIC: "+c);
		
		addedClauses.add(c);
		//add c to F1..Fi+1
		int i = 1;
		do{
			System.out.println("F"+i+" was: "+F.get(i));
			F.set(i, F.get(i).and(c));
			
			System.out.println("F"+i+" becomes: "+F.get(i));
			i++;
		}while(i<inductiveFrontier+1);
	}

	//TODO fix implementation
	private void propagateClauses(Cube T,List<Cube> F,
			ArrayDeque<Clause> addedClauses,int k) {
		//Propagate clauses
		System.out.println("Propagating clauses: "+addedClauses);
		for(Clause clause:addedClauses){
			//check whether clause can be propagated (F_i ^ T  => c') satisfiable
			//<=> F_i ^ T ^ ~c' unsat
			for(int i = 0;i<k;i++){
				Cube frontier = F.get(i);
				Cube nextfrontier = F.get(i+1);
				Cube notcprime = clause.getPrimed().not();
				if(!nextfrontier.getClauses().contains(clause)){
					System.out.printf("Test if %s can be propagated from %s(%d) to %s(%d)\n", clause,frontier,i,nextfrontier,i+1);
					boolean canbepropagated = satsolver.sat(frontier.and(clause).and(T).and(notcprime)).size()==0;
					if(canbepropagated){
						System.out.println("Yes");
						nextfrontier.addClause(clause);
						//frontier.addClause(clause);
					}else{
						System.out.println("No");
					}
				}
				
			}
		}
	/*	while(addedClauses.size()>0){
			Clause c = addedClauses.pop();
			//check whether clause can be propagated (F_k ^ c ^ T => c') satisfiable
			Cube notcprime = c.getPrimed().not();
			Cube Fk = F.get(k);
			boolean canbepropagated = satsolver.sat(Fk.and(c).and(T).and(notcprime)).size()==0;
			if(canbepropagated){
				//propagate
				F.set(k, F.get(k).and(c));
			}else{
				System.out.println("Clause "+c+" can  not be propagated");
				//return false;
			}
		}*/
	}

	/**
	 * Tests whether two formulae in f are equal
	 * @param f a list of formulae
	 * @return true if there exist two formulae f1 and f2 for which f1 <=> f2 is satisfiable
	 */
	public boolean hasFixpoint(List<Cube> f) {
		for(int f1 = 0;f1<f.size()-1;f1++){
			int f2 = f1+ 1;
			//compare the two formulae
			boolean equal = f.get(f1).equals(f.get(f2));
			if(equal){
				return true;
			}
		}
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
		System.out.println(cex);
		Clause notcex = cex.not();
		System.out.println(notcex);
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
		//test initiation (are bad states reachable from I?): ~(I => ~rhat) <=> I ^ rhat satisfiable
		if(satsolver.sat(I.and(rhat)).size()>0){
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
}
