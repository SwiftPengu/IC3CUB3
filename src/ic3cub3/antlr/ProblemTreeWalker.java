package ic3cub3.antlr;

import ic3cub3.antlr.ProblemParser.ExpressionContext;
import ic3cub3.antlr.ProblemParser.FunctionDeclarationContext;
import ic3cub3.antlr.ProblemParser.ProgramContext;
import ic3cub3.antlr.ProblemParser.StatementContext;
import ic3cub3.antlr.ProblemParser.VarDeclarationContext;
import ic3cub3.plf.Literal;
import ic3cub3.tests.ProblemSet;

import java.util.*;

public class ProblemTreeWalker extends ProblemBaseListener {
	private ProblemSet problemset = null;
	private final HashMap<String,List<Literal>> variables = new HashMap<>();
	private final HashMap<String,Integer> init = new HashMap<>();
	private final Set<Integer> inputs = new HashSet<>(); //ensure unique inputs
	private final HashMap<ExpressionContext,Integer> errorids = new HashMap<>(); //lookup for the error numbers
	
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
			if(methodName.startsWith("calculate_output")){
				processSingleTransition(ctx);
			}else{
				System.out.println("Warning: skipping over unsupported method: "+methodName);
			}
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
		for(StatementContext statement: ctx.statement().closedCompoundStatement().compoundStatement().statement()){
			assert(statement.ifStatement()!=null);
			ExpressionContext condition = statement.ifStatement().expression();
			//assume 2 statements
			assert(statement.ifStatement().statement().closedCompoundStatement().compoundStatement().statement().size()==2);
			//assume 2nd statement is an assertion
			assert(statement.ifStatement().statement().closedCompoundStatement().compoundStatement().statement(1).label().statement().functionCall().var().getText().equals("assert"));
			String errorconst = statement.ifStatement().statement().closedCompoundStatement().compoundStatement().statement(1).label().statement().functionCall().expression(0).andExpression(0).booleanExpression(0).operand().getText();
			errorids.put(condition, Integer.parseInt(errorconst.substring(6)));
			System.out.println("Error: "+errorids.get(condition)+" has condition "+condition.getText());
		}
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
						//TODO code breaks with arrays
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
	
	@Override
	public void exitProgram(ProgramContext ctx) {
		super.exitProgram(ctx);
		build();
	}
	
	protected void build(){
		//TODO build the problem set
		System.out.println("Building problem set...");
		System.out.println("Finished building problem set...");
	}
}
