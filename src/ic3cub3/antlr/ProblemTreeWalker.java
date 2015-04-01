package ic3cub3.antlr;

import ic3cub3.antlr.ProblemParser.ExpressionContext;
import ic3cub3.antlr.ProblemParser.FunctionDeclarationContext;
import ic3cub3.antlr.ProblemParser.IfStatementContext;
import ic3cub3.antlr.ProblemParser.ProgramContext;
import ic3cub3.antlr.ProblemParser.StatementContext;
import ic3cub3.antlr.ProblemParser.VarDeclarationContext;
import ic3cub3.plf.*;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.tests.ProblemSet;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemTreeWalker extends ProblemBaseListener {
	private ProblemSet problemset = null;
	private final HashMap<String,List<Literal>> variables = new HashMap<>();
	private final HashMap<String,Integer> init = new HashMap<>();
	private final HashMap<Integer,Literal> inputs = new HashMap<>(); //ensure unique inputs
	private final HashMap<ExpressionContext,Integer> errorids = new HashMap<>(); //lookup for the error numbers
	private FunctionDeclarationContext mainMethod = null; 
	private final HashMap<String,FunctionDeclarationContext> methods = new HashMap<>();
	private Formula I = null;
	private Formula T = null;
	private List<Formula> P = new ArrayList<>();
	
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
			assert(getMainMethod()==null);
			setMainMethod(ctx);
			break;
		default:
			if(methodName.startsWith("calculate_output")){
				methods.put(methodName, ctx);
			}else{
				System.out.println("Warning: skipping over unsupported method: "+methodName);
			}
			break;
		}
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
						//FIXME code breaks with arrays
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
			inputs.put(Integer.parseInt(inputvalue.andExpression(0)
					.booleanExpression(0).addExpression(0).mulExpression(0)
					.operand(0).NUMBER().getText()),new Literal());
		}
		System.out.println("new inputs: " + inputs);
	}
	
	/**
	 * Obtains a cube which states that only a single output should be enabled
	 * @return
	 */
	protected Cube getUniqueInputFormula(){
		Collection<Literal> values = getInputs().values();
		
		//state that at least one output should be true
		Cube result = new Cube(new Clause(values.stream().
				collect(Collectors.toSet())));
		values.forEach(a ->{
			values.stream().
			filter(b -> (a!=b)).
			forEach(b ->{
				//internal set usage ensures only unique clauses are added
				result.addClause(new Clause(a,b));
			});
		});
		System.out.println("Only one input enabled: "+result);
		return result;
	}
	
	private Formula generateSingleMethodFormula(
			FunctionDeclarationContext currentmethod) {
		System.out.println("Independent method: "+currentmethod.var().IDENTIFIER().getText());
		Collection<StatementContext> statements = new HashSet<StatementContext>();
		if(currentmethod.statement().closedCompoundStatement()!=null){
			statements.addAll(currentmethod.statement().
					closedCompoundStatement().
					compoundStatement().
					statement());
		}else{
			statements.add(currentmethod.statement());
		}
		return statements.stream().map(this::generateSingleStatementFormula).reduce(OrFormula::new).get();
	}
	
	private Formula generateSingleStatementFormula(StatementContext ctx){
		if(ctx.ifStatement()!=null){
			return generateFormulaFromIf(ctx.ifStatement());
		}else if(ctx.assignStatement()!=null){
			//TODO lookup variable
			//TODO set next() of variable
			throw new RuntimeException("Not yet implemented"); 
		}else{
			throw new IllegalArgumentException("Unsupported statement type");
		}
	}
	
	private Formula generateFormulaFromIf(IfStatementContext ctx){
		//parse condition
		return new AndFormula(generateFormulaFromCondition(ctx.expression()),
				//and statements
				ctx.statement().closedCompoundStatement().compoundStatement().statement().stream().
				map(this::generateSingleStatementFormula).
				reduce(AndFormula::new).get());
	}
	
	private Formula generateFormulaFromCondition(ExpressionContext ctx){
		throw new RuntimeException("Not yet implemented");
	}
	
	private Set<FunctionDeclarationContext> findStatementDependencies(StatementContext ctx){
		assert(ctx!=null);
		//base case - single statement
		if(ctx.closedCompoundStatement()==null){
			//if statement
			if(ctx.ifStatement()!=null){
				return findIfDependencies(ctx.ifStatement());
			//or function call
			}else if(ctx.functionCall()!=null){
				String methodName = ctx.functionCall().var().IDENTIFIER().getText();
				if(methodName.equals("errorCheck") || methodName.equals("fprintf") || methodName.equals("printf")){
					return new HashSet<>();
				}else{
					assert(methods.get(methodName)!=null): "Method not parsed: "+methodName;
					return new HashSet<>(Arrays.asList(new FunctionDeclarationContext[]{methods.get(methodName)}));
				}
			}else if(ctx.assignStatement()!=null){	
				return new HashSet<>();
			}else{
				throw new IllegalArgumentException("Unsupported structure: "+ctx.getText());
			}
		}else{
			HashSet<FunctionDeclarationContext> result = new HashSet<>();
			ctx.closedCompoundStatement().compoundStatement().statement().forEach(statement ->{
				result.addAll(findStatementDependencies(statement));
			});
			return result;
		}
	}
	
	private Set<FunctionDeclarationContext> findIfDependencies(IfStatementContext ctx){
		assert(ctx.statement()!=null);
		return findStatementDependencies(ctx.statement());
	}
	
	@Override
	public void exitProgram(ProgramContext ctx) {
		super.exitProgram(ctx);
		build();
	}
	
	protected void build(){
		System.out.println("Building problem set...");
		int visitcount = 0;
		assert(getMainMethod()!=null);
		assert(getMethods()!=null);
		//first, traverse the call structure of the program, and attempt to generate formulae bottom-up
		
		//stack of discovered methods not yet explored
		Deque<FunctionDeclarationContext> toExplore = new LinkedList<>();
		toExplore.add(getMainMethod());
		
		//set of dependent methods
		HashMap<FunctionDeclarationContext,Set<FunctionDeclarationContext>> dependencies = new HashMap<>();
		//map of generated formulae
		HashMap<FunctionDeclarationContext,Formula> formulae = new HashMap<>();
		
		while(!toExplore.isEmpty()){
			visitcount++;
			FunctionDeclarationContext currentmethod = toExplore.pop();
			System.out.println("Exploring: "+currentmethod.var().IDENTIFIER().getText());
			assert(formulae.get(currentmethod)==null);
			//generate dependency set
			Set<FunctionDeclarationContext> currentdependencies = findStatementDependencies(currentmethod.statement());

			//add this method as a dependency
			currentdependencies.forEach(dep ->{
				if(dependencies.get(dep)==null){
					dependencies.put(dep, new HashSet<>());
				}
				dependencies.get(dep).add(currentmethod);
			});
			
			//generate formula if no dependencies
			if(currentdependencies.size()==0){
				formulae.put(currentmethod, generateSingleMethodFormula(currentmethod));
			}
			
			//add dependencies to the exploration stack
			currentdependencies.stream().filter(dep -> formulae.get(dep)==null)
			.forEach(dep ->{
				//System.out.println("Also exploring "+dep.var().IDENTIFIER().getText());
				toExplore.push(dep);
			});
		}		
		
		//TODO resolve dependencies
		System.out.println("Reachable methods: "+visitcount);
		
		Cube initial = getUniqueInputFormula();
		System.out.println("Finished building problem set...");
	}
}
