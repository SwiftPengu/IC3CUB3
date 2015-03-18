package ic3cub3.antlr;

import ic3cub3.antlr.ProblemParser.FunctionDeclarationContext;
import ic3cub3.antlr.ProblemParser.*;
import ic3cub3.plf.Literal;
import ic3cub3.tests.ProblemSet;

import java.util.*;

public class ProblemTreeWalker extends ProblemBaseListener {
	private ProblemSet problemset = null;
	private HashMap<String,List<Literal>> variables = new HashMap<>();
	private HashMap<String,Integer> init = new HashMap<>();
	private Set<Integer> inputs = new HashSet<>(); //ensure unique inputs
	
	public ProblemSet getProblemSet(){
		return problemset;
	}
	
	@Override
	public void exitVarDeclaration(VarDeclarationContext ctx) {
		super.exitVarDeclaration(ctx);
		
		//obtain the identifier
		String id = ctx.var().IDENTIFIER().getText();
		
		switch(id){
		case "inputs":
			processInputs(ctx);
			break;
		case "input":
			//do nothing
			break;
		default:
			processVariableDeclaration(id, ctx);
			break;
		}
	}
	
	@Override
	public void exitFunctionDeclaration(FunctionDeclarationContext ctx) {
		super.exitFunctionDeclaration(ctx);
		String methodName = ctx.var().IDENTIFIER().getText();
		switch(methodName){
		case "errorCheck":
			processProperties(ctx);
			break;
		case "main":
			//do nothing
			break;
		case "calculate_output":
			processAllTransitions(ctx);
			break;
		default:
			assert(methodName.startsWith("calculate_output"));
			processSingleTransition(ctx);
			break;
		}
	}
	
	private void processAllTransitions(FunctionDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	private void processSingleTransition(FunctionDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	private void processProperties(FunctionDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	private void processVariableDeclaration(String id,VarDeclarationContext ctx) {
		//parse the variable type
		String type = ctx.type().types().getText();
		if(!id.startsWith("error_")){
			System.out.println("Adding variable: "+id);
			switch(type){
				case "int":
					//declare the variable and allocate literals
					List<Literal> literals = new ArrayList<>(32);
					for(int i = 0;i<32;i++){
						literals.add(new Literal());
					}
					getVariables().put(id,literals);
					
					//check whether this is an initialisation
					if(init.get(id)==null && ctx.assign()!=null){
						init.put(id,Integer.parseInt(ctx.assign().expression().andExpression(0).booleanExpression(0).addExpression(0).mulExpression(0).operand(0).NUMBER().getText()));
						System.out.println("Initialised "+id+" to "+init.get(id));
					}
					break;
				default:
					throw new RuntimeException("Unsupported declaration encountered: "+type);
			}
		}
	}

	private void processInputs(VarDeclarationContext ctx) {
	//obtain the inputs (expects integer array)
		for (ExpressionContext inputvalue : ctx.assign().expression()
				.andExpression(0).booleanExpression(0).addExpression(0)
				.mulExpression(0).operand(0).staticArray().expression()) {
			// parse any integers
			inputs.add(Integer.parseInt(inputvalue.andExpression(0)
					.booleanExpression(0).addExpression(0).mulExpression(0)
					.operand(0).NUMBER().getText()));
		}
		System.out.println("new inputs: " + inputs);
	}

	public HashMap<String, List<Literal>> getVariables() {
		return variables;
	}
}
