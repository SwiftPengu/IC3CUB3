package ic3cub3.sat;

import ic3cub3.plf.Formula;
import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.runner.Runner;
import ic3cub3.runner.Settings;

import java.io.*;
import java.util.*;

/**
 * Class which invokes Logic2CNF
 * @author Rick Hindriks
 *
 */
public class Logic2CNF extends SATSolver {
	
	public Logic2CNF() throws IOException{
		File l2c = new File(Settings.COMMAND);
		if(!l2c.exists())throw new RuntimeException("Logic2CNF not found at "+l2c.getAbsolutePath());

		//Test if Logic2CNF is working
		try {
			Process logic2cnf = Runtime.getRuntime().exec(Settings.COMMAND);
			logic2cnf.destroy();
		} catch (IOException e) {
			System.err.println("Exception while executing Logic2CNF");
			e.printStackTrace();
			throw e;
		}
		
	}

	@Override
	public List<Cube> sat(Cube c,boolean skip) {
		Formula f = c.toFormula();
		try {
			//Run Logic2CNF
			final Process logic2cnf = Runtime.getRuntime().exec(Settings.COMMAND);

			//Feed input
			processInput(logic2cnf,f);			
			processErrorStream(logic2cnf); //Gobble std.err
			List<Cube> result = processOutput(logic2cnf,skip,f.getTseitinVariables()); //obtain processed output
			if(Runner.VERBOSE>1)System.out.println("D: "+result);
			logic2cnf.destroy(); //Clean up Logic2CNF
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void processInput(Process logic2cnf,Formula f) {
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(logic2cnf.getOutputStream()));
		//define variables
		pw.print("def");
		for(Integer i:f.getVariables()){
			pw.print(" x"+i);
		}
		pw.println(";");
		//define the equation to be checked
		if(Runner.VERBOSE>1)System.out.println("D: "+f.getLogic2CNFString());
		pw.println(f.getLogic2CNFString()+";");
		pw.close();	
	}

	private void processErrorStream(final Process logic2cnf){
		//process the error stream
		if(Runner.VERBOSE>1){
			new Thread(new Runnable(){
				public void run(){
					Scanner sc = new Scanner(logic2cnf.getErrorStream());
					while(sc.hasNextLine()){
						System.err.println("#: "+sc.nextLine());
					}
					sc.close();
				}
			}).start();
		}

	}
	
	private List<Cube> processOutput(Process logic2cnf, boolean skip, Set<Integer> tseitinvars) {
		List<Cube> result = new ArrayList<Cube>();
		
		//process results line by line
		Scanner sc = new Scanner(logic2cnf.getInputStream());
		while(sc.hasNextLine()){
			//process a line
			String line = sc.nextLine();
			if(Runner.VERBOSE>1)System.out.println(line);
			Scanner linescan = new Scanner(line);
			linescan.next(); //skip line number
			
			//construct formula
			Cube singleformula = null;
			
			//process all variables
			while(linescan.hasNext()){
				String varstring = linescan.next();
				boolean negated = varstring.charAt(0)=='~'; //test for negation
				int varid = Integer.parseInt(varstring.substring(negated?2:1));
				boolean primedvar = isPrimed(varid);
				if(!skip || !primedvar){
					if(!tseitinvars.contains(varid)){//discard extra introduced variables
						Literal var = new Literal(varid-(primedvar?1:0),negated,primedvar,false);
						
						//add the formula
						if(singleformula==null){
							singleformula = new Clause(var).asCube();
						}else{
							singleformula = singleformula.and(new Clause(var).asCube());
						}
					}
				}
			}
			result.add(singleformula);
			linescan.close();
		}
		sc.close();
		return result;
	}
	
	private boolean isPrimed(int varid){
		return varid%2==0;
	}
}
