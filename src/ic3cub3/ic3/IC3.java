package ic3cub3.ic3;


import static ic3cub3.runner.Runner.printv;
import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.sat.SATSolver;

import java.util.*;

public class IC3 {
	private final SATSolver satsolver;
	
	public IC3(SATSolver satsolver){
		this.satsolver=satsolver;
	}
	
	public List<ProofObligation> check(Cube I, Cube T, Cube P,Cube NP){
			printv("I:\t"+I,2);
			printv("T:\t"+T,2);
			printv("P:\t"+P,2);
			printv("NP:\t",2);
		//
		// Establish invariants
		
		//check I => P
		printv("Check I => P",1);
		if(satsolver.sat(I.and(NP)).size()>0){
			printv("I => P does not hold",1);
			return Arrays.asList(new ProofObligation[]{new ProofObligation(P, 0)});
		}else{
			printv("I => P",1);
		}
		//check I ^ T => P
		if(satsolver.sat(I.and(T).and(NP)).size()>0){
			printv("I ^ T => P does not hold",1);
			return Arrays.asList(new ProofObligation[]{new ProofObligation(P, 1)});
		}else{
			printv("I ^ T => P",1);
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
			testPInductive(F,T,k,NP,NPPrime,proofObligations);
			
			//Solve all proof obligations for given k
			while(proofObligations.size()>0){
				ProofObligation probl = proofObligations.remove();
				printv("Attempting to prove: "+probl.getCTI()+" not reachable from level "+probl.getLevel(),1);
				Cube s = probl.getCTI();
				Integer inductiveFrontier = findInductiveFrontier(probl,F,T);
				if(inductiveFrontier==null){
					printv("Found counterexample to P: "+s,1);
					printTrace(I,probl);
					return probl.getProofTrace();
				}else{
					strengthen(s,F,T,inductiveFrontier,addedClauses);
					checkCTIResolved(F,probl,inductiveFrontier,k,T,proofObligations);					
				}
			}
			
			//No more counterexamples, and no solution yet, so increase k.
			k++;
			F.add(P); //Fk = P
			printv("k increased to "+k,0);
			propagateClauses(T,F,addedClauses,k);
			if(hasFixpoint(F)){
				return null;
			}
		}
	}
	
	private void testPInductive(List<Cube> F, Cube T,int k,Cube NP, Cube NPPrime, PriorityQueue<ProofObligation> proofObligations){
		List<Cube> result = satsolver.sat(F.get(k).and(T).and(NPPrime),true);
		if(result.size()>0){
			printv("F_k ^ T ^ ~p' satisfiable for k="+k,1);
			
			//obtain counterexample S and S'
			Cube s = result.get(0);
			printv("s: "+s,1);
			proofObligations.add(new ProofObligation(s, k-1,new ProofObligation(NP,k)));
		}else{
			printv("P is inductive",0);
		}
	}

	//Find highest inductive Fi
	private Integer findInductiveFrontier(ProofObligation probl,List<Cube> F, Cube T) {
		Integer result = null;
		Cube s = probl.getCTI();
		Cube sPrime = s.getPrimed();
		Clause nots = s.not();
		for(int i = probl.getLevel();i>=probl.getLevel()-2 && i>=0;i--){
			Cube Fi = F.get(i);
			printv("Check ~s "+nots+" inductive at F"+i+";" +F.get(i)+" ^ "+nots+" ^ "+"T"+" ^ "+sPrime,1);
			List<Cube> cex = satsolver.sat(Fi.and(nots).and(T).and(sPrime));
			if(cex.size()==0){ //no counterexample
				printv("~s is inductive at i="+i,1);
				result = i;
				return result;
			}else{
				printv("~s not inductive at i="+i,1);
			}
		}
		return result;
	}

	//Refine F1...Fi+1
	private void strengthen(Cube S,List<Cube> F, Cube T,Integer inductiveFrontier,Set<Clause> addedClauses) {
		//first obtain a minimal inductive subclause
		Clause c = MIC(S, F.get(0), T, F.get(inductiveFrontier));
		printv("MIC: "+c,1);
		
		addedClauses.add(c);
		//add c to F1..Fi+1
		for(int i = 1;i<=inductiveFrontier+1 && i<F.size();i++){
			if(!F.get(i).getClauses().contains(c)){
				printv("F"+i+" was: "+F.get(i),1);
				F.set(i, F.get(i).and(c));
				printv("F"+i+" becomes: "+F.get(i),1);
			}else{
				printv("F"+i+" remains unchanged",1);
			}
		}
	}
	
	private void checkCTIResolved(List<Cube> F, ProofObligation probl, int inductiveFrontier, int k,Cube T, PriorityQueue<ProofObligation> proofObligations){
		boolean ctiStillExists = satsolver.sat(F.get(probl.getLevel()).and(T).and(probl.getCTI().getPrimed()),true).size()>0;
		if(ctiStillExists){
			printv("CTI still exists",1);
			//cti is not yet resolved, so attempt to resolve it again
			proofObligations.add(probl);
			//if the possibility of predecessors of s exists, check for such states
			if(inductiveFrontier<k-1){	
				//find a predecessor state
				//That is, a solution to Fi+1 ^ T => s
				printv("Finding a predecessor of s from level "+(inductiveFrontier+1),3);
				List<Cube> predecessors = satsolver.sat(F.get(inductiveFrontier+1).and(T).and(probl.getCTI().getPrimed()),true);
				printv("Predecessors: "+predecessors,3);
				assert(predecessors.size()>0) : "Error: no predecessors of s for inductive ~s";
				Cube t = predecessors.get(0);
				proofObligations.add(new ProofObligation(t, inductiveFrontier,probl));
			}
		}
	}

	private void propagateClauses(Cube T,List<Cube> F,
			Set<Clause> addedClauses,int k) {
		//Propagate clauses
		printv("Propagating clauses: "+addedClauses,1);
		for(Clause clause:addedClauses){
			//check whether clause can be propagated (F_i ^ T  => c') satisfiable
			//<=> F_i ^ T ^ ~c' unsat
			for(int i = 0;i<k;i++){
				Cube frontier = F.get(i);
				Cube nextfrontier = F.get(i+1);
				Cube notcprime = clause.getPrimed().not();
				if(!nextfrontier.getClauses().contains(clause)){
					printv(String.format("Test if %s can be propagated from %s(%d) to %s(%d)\n", clause,frontier,i,nextfrontier,i+1),1);
					boolean canbepropagated = satsolver.sat(frontier.and(clause).and(T).and(notcprime)).size()==0;
					if(canbepropagated){
						nextfrontier.addClause(clause);
						//TODO simplify new frontier
						printv("Yes",1);
					}else{
						printv("No",1);
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
		for(int f1 = 0;f1<F.size()-1;f1++){
			int f2 = f1+ 1;
			//compare the two formulae
			boolean equal = F.get(f1).equals(F.get(f2));
			if(equal){
				printv(String.format("Fixpoint found at F%d and F%d, TS |= P",f1,f2),0);
				printv(F.get(f1),0);
				return true;
			}
		}
		printv("No fixpoint: "+F,1);
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
		printv("MIC on: "+cex,1);
		Clause notcex = cex.not();
		printv(notcex,2);
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
	
	public Clause down(Clause rhat,Cube I,Cube T, Cube Fi){
		Cube notrhat = rhat.not();
		printv("Down on: "+rhat,2);
		//test initiation (are bad states reachable from I?): ~(I => rhat) unsat <=> I ^ ~rhat satisfiable
		if(satsolver.sat(I.and(notrhat)).size()>0){
			printv("Initiation failed",2);
			return null;
		}
		
		//rhat => ~s
		//test consecution (attempt to obtain an unsat proof for ~(Fi ^ rhat ^ T => rhat') <=> Fi ^ rhat ^ T ^ ~rhat'
		List<? extends Cube> counterexamples = satsolver.sat(Fi.and(T).and(rhat).and(notrhat.getPrimed()),true);
		if(counterexamples.size()==0){
			printv(rhat+" is inductive!",2);
			return rhat;
		}else{
			//TODO use counterexample to refine rhat (rhat = rhat without literals not in ~cex
			return null;
		}
	}
	
	public static void printTrace(Cube I,ProofObligation probl) {
		System.out.println("Trace to ~P: ");
		System.out.print("I");
		for(ProofObligation po:probl.getProofTrace()){
			System.out.println(" --> ");
			System.out.print(po.getCTI());
		}
		System.out.println();
	}
}
