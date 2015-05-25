package ic3cub3.rersparser;

import ic3cub3.rersparser.ProblemParser.AndExpressionContext;
import ic3cub3.rersparser.ProblemParser.BooleanExpressionContext;
import ic3cub3.rersparser.ProblemParser.ExpressionContext;
import ic3cub3.rersparser.ProblemParser.FunctionDeclarationContext;
import ic3cub3.rersparser.ProblemParser.IfStatementContext;
import ic3cub3.rersparser.ProblemParser.OperandContext;
import ic3cub3.rersparser.ProblemParser.ProgramContext;
import ic3cub3.rersparser.ProblemParser.StatementContext;
import ic3cub3.rersparser.ProblemParser.VarDeclarationContext;
import ic3cub3.plf.*;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.runner.Runner;
import ic3cub3.tests.ProblemSet;
import ic3cub3.tests.ProblemSet.PropertyPair;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.*;

/**
 * Class for encoding the RERS challenge programs as transition systems and properties suitable for IC3
 */
@Setter(value=AccessLevel.PROTECTED) @Getter(value=AccessLevel.PROTECTED)
public class ProblemTreeWalker extends ProblemBaseListener {
    private final Map<String,List<Literal>> variables = new HashMap<>();

	private final Map<String,Integer> init = new HashMap<>();
	private final Map<Integer,Literal> inputs = new HashMap<>(); //ensure unique inputs
	//TODO convert this to a list:
	private final Map<ExpressionContext,Integer> errorids = new HashMap<>(); //lookup for the error numbers
	private FunctionDeclarationContext mainMethod = null;
	private final Map<String,FunctionDeclarationContext> methods = new HashMap<>();
	private Cube I = null;
	private Cube T = null;
	private List<PropertyPair> P = new ArrayList<>();
	
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
				Runner.printv(() -> "Warning: skipping over unsupported method: "+methodName,1);
			}
			break;
		}
	}
	
	@Override
	public void exitProgram(ProgramContext ctx) {
		super.exitProgram(ctx);
		build();
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
	
	public ProblemSet getProblemSet(){
		return new ProblemSet(getInitial(), getTransitionRelation(), getProperties());
	}
	
	public Cube getInitial(){
		if(getI()==null)throw new IllegalStateException("I not initialized, execute tree walk first");
		return getI();
	}
	
	public Cube getTransitionRelation(){
		if(getT()==null)throw new IllegalStateException("T not initialized, execute tree walk first");
		return getT();
	}
	
	public List<PropertyPair> getProperties(){
		if(getP()==null || getP().size()==0)throw new IllegalStateException("P[] not initialized, execute tree walk first");
		return getP();
	}

	protected void build(){
		System.out.println("Building problem set...");
		I = buildInitialState();
		T = buildTransitionRelation();
		P = buildProblems();
		System.out.println("Finished building problem set...");
	}

	protected Cube buildInitialState(){
		Cube result = getUniqueInputCube();
		//init all variables according to their values
		result = result.and(new Cube(init.entrySet().stream().
				map(e -> getIntValue(variables.get(e.getKey()),e.getValue())).
				flatMap(c -> c.getClauses().stream()).
				collect(Collectors.toSet())));
		System.out.println("Number of clauses in initial state: "+result.getClauses().size());
		return result;
	}
	
	protected Cube buildTransitionRelation(){
		int visitcount = 0;
		assert(getMainMethod()!=null);
		assert(getMethods()!=null);
		//first, traverse the call structure of the program, and attempt to generate formulae bottom-up
		
		//stack of discovered methods not yet explored
		Deque<FunctionDeclarationContext> toExplore = new LinkedList<>();
		toExplore.add(getMainMethod());
		
		//set of dependent methods (a,b), means that by to generate a formula for (a), (b) should be already generated 
		Map<FunctionDeclarationContext,Set<FunctionDeclarationContext>> dependencies = new HashMap<>();
		//map of generated formulae
		Map<FunctionDeclarationContext,Formula> formulae = new HashMap<>();
		
		while(!toExplore.isEmpty()){
			visitcount++;
			FunctionDeclarationContext currentmethod = toExplore.pop();
			System.out.println("Exploring: "+currentmethod.var().IDENTIFIER().getText());
			assert(formulae.get(currentmethod)==null);
			
			//generate dependency set
			Set<FunctionDeclarationContext> currentdependencies = findStatementDependencies(currentmethod.statement());

			//generate formula if no dependencies
			if(currentdependencies.size()==0){
				formulae.put(currentmethod, generateSingleMethodFormula(currentmethod,null));
			}else{
				//add this method as a dependency
				currentdependencies.forEach(dep ->{
					if(dependencies.get(currentmethod)==null){
						dependencies.put(currentmethod, new HashSet<>());
					}
					dependencies.get(currentmethod).add(dep);
				});
				
				//add dependencies to the exploration stack
				currentdependencies.stream().filter(dep -> (formulae.get(dep)==null))
				.forEach(toExplore::push);
			}
		}	
		System.out.println("Reachable methods: "+visitcount);
		
		//Update dependencies (until all dependencies are satisfied)
		do{
			//remove all satisfied dependencies
			dependencies.entrySet().stream().forEach(e-> e.getValue().retainAll(dependencies.keySet()));
			
			//get all resolvable dependencies
			dependencies.entrySet().stream().filter(e -> (e.getValue().size()==0))
			//and resolve them
			//NOTE: we use peek instead of foreach, as we need to store all the processed entries, so we can remove them later
			.peek(e ->{
				formulae.put(e.getKey(), generateSingleMethodFormula(e.getKey(), formulae));
				assert(formulae.get(e.getKey())!=null);
			})
			.collect(Collectors.toSet())
			.forEach(e -> dependencies.remove(e.getKey()));
		}
		//the number of unsatisfied dependencies (in the set of the map) is >0
		while(dependencies.values().stream().flatMap(Collection::stream).count()>0);
		System.out.println("All method dependencies resolved");
		
		//Convert the result to CNF
		assert(formulae.get(getMainMethod())!=null);
		System.out.println(formulae.get(getMainMethod()));
		System.out.println("Performing Equivalence Conversion...");
		//Cube result = formulae.get(getMainMethod()).toCube();
		System.out.println("Conversion complete");
		System.out.println("Performing Tseitin Conversion...");
		Cube result = formulae.get(getMainMethod()).tseitinTransform();
		
		//add input transition
		System.out.println(getUniqueInputCube());
		result.and(getUniqueInputCube().getPrimed());
		
		return result;
	}
	
	protected List<PropertyPair> buildProblems(){
		return	errorids.entrySet().stream()
				.sorted((a,b) -> Integer.compare(a.getValue(), b.getValue()))
				.map(e -> generateFormulaFromCondition(e.getKey()))
				.map(f -> PropertyPair.of(
						f.not().toEquivalentCube(),
						//~P
						f.toEquivalentCube()))
				.collect(Collectors.toList()); 
	}
	
	/**
	 * Obtains a cube which states that only a single output should be enabled
	 * @return a cube representing that only a single output is enabled at any given time
	 */
	protected Cube getUniqueInputCube(){
		Collection<Literal> values = getInputs().values();
		
		//state that at least one output should be true
		Cube result = new Cube(new Clause(values.stream().
				collect(Collectors.toSet())));
		values.forEach(a -> values.stream().
        filter(b -> (a != b)).
        forEach(b -> {
			//internal set usage ensures only unique clauses are added
			result.addClause(new Clause(a.not(), b.not()));
		}));
		Runner.printv(() -> "Only one input enabled: "+result,2);
		return result;
	}
	
	private Set<FunctionDeclarationContext> findIfDependencies(IfStatementContext ctx){
		assert(ctx.statement()!=null);
		return findStatementDependencies(ctx.statement());
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
			ctx.closedCompoundStatement().compoundStatement().statement().forEach(
					statement -> result.addAll(findStatementDependencies(statement)));
			return result;
		}
	}
	
	private Formula generateFormulaFromAndContext(AndExpressionContext ctx){
		return ctx.booleanExpression().stream().map(this::generateFormulaFromBooleanExpressionContext).reduce(AndFormula::new).get();
	}
	
	private Formula generateFormulaFromBooleanExpressionContext(BooleanExpressionContext ctx){
		if(ctx.EQUAL()!=null && ctx.addExpression().size()>1){
			assert(ctx.addExpression(0).mulExpression(0).operand(0).var()!=null);
			assert(ctx.addExpression(1).mulExpression(0).operand(0).NUMBER()!=null);
			String var = ctx.addExpression(0).mulExpression(0).operand(0).var().IDENTIFIER().getText();
			int val = Integer.parseInt(ctx.addExpression(1).mulExpression(0).operand(0).NUMBER().getText());
			if(var.equals("input")){
				return inputs.get(val);
			}else{
				assert(variables.get(var)!=null) : var+" is not declared";
				return getIntValue(variables.get(var),val).toFormula();
			}
		}else if(ctx.addExpression().size()==1){
			return generateFormulaFromCondition(ctx.addExpression(0).mulExpression(0).operand(0).expression());
		}else{
			throw new RuntimeException("Not yet implemented");
		}
	}
	
	private Formula generateFormulaFromCondition(ExpressionContext ctx){
		return ctx.andExpression().stream().map(this::generateFormulaFromAndContext).reduce(OrFormula::new).get();
	}
	
	private Formula generateFormulaFromIf(IfStatementContext ctx, Map<FunctionDeclarationContext,Formula> methodFormulae){
		//parse condition
		if(ctx.statement().closedCompoundStatement()==null){
			return new AndFormula(generateFormulaFromCondition(ctx.expression()),
					generateSingleStatementFormula(ctx.statement(), methodFormulae));	
		}else{
			return new AndFormula(generateFormulaFromCondition(ctx.expression()),
				ctx
				.statement()
				.closedCompoundStatement()
				.compoundStatement()
				.statement()
				.stream().
				map(s -> generateSingleStatementFormula(s,methodFormulae)).
				reduce(AndFormula::new).get());
		}
	}
	
	private Integer generateOperand(ExpressionContext ctx){
		OperandContext opc = ctx.
				andExpression(0).
				booleanExpression(0).
				addExpression(0).
				mulExpression(0).
				operand(0);
		if(opc.NUMBER()!=null){
			return Integer.parseInt(opc.NUMBER().getText());
		}else if(opc.expression()!=null){
			return generateOperand(opc.expression());
		}else{
			throw new RuntimeException("Unsupported operand");
		}
	}
	
	private Formula generateSingleMethodFormula(
			FunctionDeclarationContext method, Map<FunctionDeclarationContext,Formula> methodFormulae) {
		if(methodFormulae==null)System.out.println("Independent method: "+method.var().IDENTIFIER().getText());
		Collection<StatementContext> statements = new HashSet<>();
		if(method.statement().closedCompoundStatement()!=null){
			statements.addAll(method.statement().
					closedCompoundStatement().
					compoundStatement().
					statement());
		}else{
			statements.add(method.statement());
		}
		return statements.stream().map(s -> generateSingleStatementFormula(s,methodFormulae)).reduce(OrFormula::new).get();
	}
	
	private Formula generateSingleStatementFormula(StatementContext ctx, Map<FunctionDeclarationContext,Formula> methodFormulae){
		if(ctx.ifStatement()!=null){
			return generateFormulaFromIf(ctx.ifStatement(),methodFormulae);
		}else if(ctx.assignStatement()!=null){
			String varname = ctx.assignStatement().var().IDENTIFIER().getText();
			int value = generateOperand(ctx.assignStatement().expression());
			return getIntValue(getVariables().get(varname).stream().
			map(Literal::getPrimed).collect(Collectors.toList()),value).toFormula();
		}else if(ctx.functionCall()!=null){
			String fname = ctx.functionCall().var().IDENTIFIER().getText();
			switch(fname){
			case "printf":
			case "fprintf":
			case "errorCheck":
				//do nothing (returns true)
				Literal l = new Literal();
				return new OrFormula(l,l.not());
			default:
				if(methodFormulae!=null){
					Map<String,Formula> namedMethods = methodFormulae.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().var().getText(), Entry::getValue));
					if(namedMethods.containsKey(fname)){
						return namedMethods.get(fname);
					}
					//else: not returning throws exception
				}
				throw new IllegalArgumentException("Unsupported function call: "+fname);
			}
		}else{
			throw new IllegalArgumentException("Unsupported statement type: "+ctx.getText());
		}
	}
	
	private void processInputs(VarDeclarationContext ctx) {
	//obtain the inputs (expects integer array)
		for (ExpressionContext inputvalue : ctx.assign().expression()
				.andExpression(0).booleanExpression(0).addExpression(0)
				.mulExpression(0).operand(0).staticArray().expression()) {
			// parse any integers
			inputs.put(generateOperand(inputvalue),new Literal());
		}
		System.out.println("new inputs: " + inputs);
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
					List<Literal> literals = new ArrayList<>(Integer.SIZE);
					for(int i = 0;i<Integer.SIZE;i++){
						literals.add(new Literal());
					}
					getVariables().put(id,literals);
					
					//check whether this is an initialisation
					if(init.get(id)==null && ctx.assign()!=null){
						//TODO code breaks with arrays
						int val = generateOperand(ctx.assign().expression());
						init.put(id,val);
						System.out.println("Initialised "+id+" to "+init.get(id));
					}
					break;
				default:
					throw new RuntimeException("Unsupported declaration encountered: "+type);
			}
		}
	}
	
	//TODO get trace
	public String convertState(Cube c){
		final Map<String,Integer> varvalues = new HashMap<>();
		getVariables().entrySet().stream().forEach(e -> varvalues.put(e.getKey(), 0));
		
		System.out.println(varvalues);
		
		c.getClauses().stream().forEach(clause ->{
			assert(clause.getLiterals().size()==1);
			Literal l = clause.getLiterals().iterator().next();
			String var = null;
			for(Entry<String,List<Literal>> e:getVariables().entrySet()){
				int index = -1;
				for(int i = 0;i<e.getValue().size();i++){
					if(e.getValue().get(i).getID()==l.getID()){
						index=i;
					}
				}
				if(index!=-1){
					var=e.getKey();
				}
			}
				
			
			if(var!=null){
				int index = -1;
				for(int i = 0;i<getVariables().get(var).size();i++){
					if(getVariables().get(var).get(i).getID()==l.getID()){
						index = i;
						break;
					}
				}
				assert(index!=-1);
				BigInteger val = BigInteger.valueOf(varvalues.get(var));
				if(l.isNegated()){
					val.clearBit(index);
				}else{
					val.setBit(index);
				}
				varvalues.put(var, val.intValue());
			}else{
				System.out.println("warning, skipped over "+l);
			}
		});
		
		return varvalues.toString();
	}
	
	/**
	 * Returns a cube, representing that the given bits are according to the given 2's complement value
	 * @param bits a list of bits, index 0 meaning the LSB, must contain enough bits to store the value
	 * @param value the integer value to be represented
	 * @return a cube representing the bits of value
	 */
	public static Cube getIntValue(@NonNull List<Literal> bits, int value){
		return new Cube(IntStream.range(0, bits.size()).sequential().
				mapToObj(i -> ((((value>>i)&0x1)==1)?bits.get(i):bits.get(i).not())).
				map(Clause::new).
				collect(Collectors.toSet()));
	}
}