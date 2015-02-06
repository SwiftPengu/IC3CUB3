package logic2cnf;

import java.io.*;
import java.util.Scanner;

import plf.Formula;
import runner.Runner;
import sat.SATSolver;

/**
 * Class which invokes Logic2CNF
 * @author Rick Hindriks
 *
 */
public class Logic2CNF implements SATSolver {
	private String command = "../logic2cnf/logic2cnf";
	
	public Logic2CNF() throws IOException{
		//TODO look for logic2cnf
		if(!new File(command).exists())throw new RuntimeException("Logic2CNF not found!");

		//Test if Logic2CNF is working
		try {
			Process logic2cnf = Runtime.getRuntime().exec(command);
			logic2cnf.destroy();
		} catch (IOException e) {
			System.err.println("Exception while executing Logic2CNF");
			e.printStackTrace();
			throw e;
		}
		
	}

	@Override
	public Formula solve(Formula f) {
		try {
			final Process logic2cnf = Runtime.getRuntime().exec(command);

			//feed logic2cnf input
			PrintWriter pw = new PrintWriter(new BufferedOutputStream(logic2cnf.getOutputStream()));
			//define variables
			pw.print("def");
			for(Integer i:f.getVariables()){
				pw.print(" x"+i);
			}
			pw.println(";");
			//define the equation to be checked
			if(Runner.VERBOSE>0)System.out.println(f.getLogic2CNFString());
			pw.println(f.getLogic2CNFString()+";");
			pw.close();
			

			//process the error stream
			/*if(Runner.VERBOSE>0){
				new Thread(new Runnable(){
					public void run(){
						Scanner sc = new Scanner(logic2cnf.getErrorStream());
						while(sc.hasNextLine()){
							System.err.println("#: "+sc.nextLine());
						}
					}
				}).start();
			}*/
			
			//process the output
			Scanner sc = new Scanner(logic2cnf.getInputStream());
			int lines = 0;
			while(sc.hasNextLine()){
				lines++;
				if(Runner.VERBOSE>0)System.out.println("@: "+sc.nextLine());
			}
			System.out.println(lines==0?"no CTI":"CTI");
			sc.close();
			//clean up Logic2CNF
			logic2cnf.destroy();
			
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean needsCNF() {
		return false;
	}

	
}
