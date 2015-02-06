package ic3;

import java.util.*;

import plf.Formula;
import sat.SATSolver;

public class IC3 {
	private final SATSolver satsolver;
	
	public IC3(SATSolver satsolver){
		this.satsolver=satsolver;
	}
	
	public boolean check(Formula I, Formula T, Formula P){
		HashSet<Integer> primedvars = new HashSet<Integer>();
		primedvars.add(3);
		primedvars.add(4);
		
		//check I => P
		if(satsolver.solve(I.implies(P).not()).size()>0){
			System.out.println("I => P does not always hold");
			return false;
		}else{
			System.out.println("I => P");
		}
		//check I ^ T => P
		if(satsolver.solve(I.and(T).implies(P).not()).size()>0){
			System.out.println("I ^ T => P does not always hold");
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
		Formula Pprime = prime(P);
		
		while(true){
			boolean ctifound = true;
			while(ctifound){ //TODO ctis in Fk
				ctifound = false;
				//test Fk ^ T ^ ~p'
				List<Formula> result = satsolver.solve(F.get(k).and(T).and(Pprime.not()),primedvars);
				if(result.size()>0){
					System.out.println("F_k ^ T ^ ~p' satisfiable for k="+k);
					ctifound=true;
					
					//obtain counterexample S and S'
					Formula S = result.get(0);
					Formula Sprime = prime(S);
					
					//Find highest inductive Fi
					Integer inductiveFrontier = null;
					for(int i = k;i>=0;i--){
						Formula Fi = F.get(i);
						System.out.println("Check S inductive at F_"+i);
						boolean inductive = satsolver.solve(Fi.and(S.not()).and(T).implies(Sprime.not()).not()).size()==0;
						if(inductive){
							System.out.println("S is inductive at i="+i);
							inductiveFrontier = i;							
						}
					}
					if(inductiveFrontier==null){
						System.out.println("Found counterexample to P: "+S);
						return false;
					}else{
						//TODO refine F1...Fi+1
					}
				}
			}
			k++;
			F.add(P); //Fk = P
			//TODO propagate clauses
			
			if(hasFixpoint(F)){
				System.out.println(String.format("Fixpoint found with k=%d, TS |= P",k));
			}
		}
	}

	public boolean hasFixpoint(List<Formula> f) {
		for(int f1 = 0;f1<f.size()-1;f1++){
			for(int f2=f1+1;f2<f.size();f2++){
				//compare the two formulae
				boolean equal = f.get(f1).equal(f.get(f2), satsolver);
				if(equal)return true;
			}
		}
		return false;
	}

	//TODO use list of primed and unprimed variables
	private Formula prime(Formula f){
		return f.rename(1, 3).rename(2, 4);
	}
	
	private Formula unprime(Formula f){
		return f.rename(3, 1).rename(4, 2);
	}
}
