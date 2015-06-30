package ic3cub3.rersparser;

import ic3cub3.plf.AndFormula;
import ic3cub3.plf.Formula;
import ic3cub3.plf.Literal;
import ic3cub3.plf.OrFormula;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.rersparser.ProblemParser.*;
import ic3cub3.tests.ProblemSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ic3cub3.runner.Runner.printv;

/**
 * Class for encoding the RERS challenge programs as transition systems and properties suitable for IC3
 */
@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractRERSParser extends ProblemBaseListener{
    private final Map<String,Variable> variables = new HashMap<>();
    private final Map<String,FunctionDeclarationContext> methodDeclarations = new HashMap<>();
    private final Map<Integer,Literal> inputs = new HashMap<>();
    private final Map<ExpressionContext,Integer> errorids = new HashMap<>(); //lookup for the error numbers

    private final Map<String,Integer> initialValues = new HashMap<>();
    private final Map<String,Cube> inlinedMethods = new HashMap<>();

    private FunctionDeclarationContext mainMethod;
    private Cube I;
    private Cube T;
    private List<ProblemSet.PropertyPair> P;

    //var is assigned value
    public abstract Cube parseAssignment(Variable var,int value);

    //var is equal to value
    public abstract Formula parseEqual(Variable var,int val);
    public abstract Formula parseInputEqual(int val);

    public Stream<Cube> parseSingleStatement(StatementContext ctx){
        if(ctx.assignStatement()!=null)return prepareAssignment(ctx.assignStatement());
        if(ctx.functionCall()!=null)return prepareFunctionCall(ctx.functionCall());
        if(ctx.ifStatement()!=null)return prepareIf(ctx.ifStatement());
        if(ctx.closedCompoundStatement()!=null)return parseMultipleStatement(ctx.closedCompoundStatement());
        throw new UnsupportedOperationException("Unable to parse statement: "+ctx);
    }

    public Stream<Cube> parseMultipleStatement(ClosedCompoundStatementContext closedCompoundStatementContext){
        return Stream.of(new Cube(closedCompoundStatementContext.compoundStatement().statement().stream()
                .flatMap(this::parseSingleStatement)
                .map(Cube::getClauses)
                .flatMap(Set::stream)
                .collect(Collectors.toList())));
    }

    private Stream<Cube> prepareAssignment(AssignStatementContext assignStatementContext) {
        assert(assignStatementContext!=null);
        assert(getVariables().containsKey(assignStatementContext.var().IDENTIFIER().getText()));
        return Stream.of(parseAssignment(getVariables().get(assignStatementContext.var().IDENTIFIER().getText()),
                getIntegerFromExpression(assignStatementContext.expression())));
    }

    private Stream<Cube> prepareFunctionCall(FunctionCallContext functionCallContext) {
        switch(getMethodName(functionCallContext).toLowerCase()){
            case "fprintf":
            case "printf":
            case "errorcheck":
                return Stream.empty();
            default:
                return Stream.of(getInlinedMethods().get(functionCallContext.var().IDENTIFIER().getText()));
        }
    }

    protected Formula parseExpressionCondition(ExpressionContext ctx){
        return ctx.andExpression().stream()
                .map(this::parseAndCondition)
                .reduce(OrFormula::new).get();
    }

    private Formula parseAndCondition(AndExpressionContext andExpressionContext) {
        return andExpressionContext.booleanExpression().stream()
                .map(this::parseBooleanCondition)
                .reduce(AndFormula::new).get();
    }

    private Formula parseBooleanCondition(BooleanExpressionContext ctx){
        if(ctx.EQUAL()!=null && ctx.EQUAL().toString().contains("==")) {
            //TODO assumes id==number, and assumes no negative integers are compared
            String id = ctx.addExpression(0).mulExpression(0).operand(0).getText().trim();
            int value = Integer.
                    parseInt(ctx.addExpression(1).
                            mulExpression(0).
                            operand(0).
                            NUMBER().
                            getText());
            if(id.equals("input")){
                return parseInputEqual(value);
            }else {
                assert(getVariables().containsKey(id));
                return parseEqual(getVariables().get(id), value);
            }
        }else{
            return parseExpressionCondition(ctx.addExpression(0).mulExpression(0).operand(0).expression());
        }
    }

    private Stream<Cube> prepareIf(IfStatementContext ifStatementContext) {
        Optional<Cube> statement = parseSingleStatement(
                ifStatementContext.statement()
        ).findAny();
        if(statement.isPresent()){
            return Stream.of(parseExpressionCondition(ifStatementContext.expression())
                    .implies(statement.get().toFormula())
                    .toEquivalentCube());
        }else{
            return Stream.empty();
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
            printv(() -> "Error number " + errorids.get(condition) + " has condition " + condition.getText(),1);
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
                assert(getMainMethod()==null) : "Main method encountered more than once";
                setMainMethod(ctx);
                break;
            default:
                if(methodName.startsWith("calculate_output")){
                    getMethodDeclarations().put(methodName, ctx);
                }else{
                    printv(() -> "Warning: skipping over unsupported method: " + methodName, 1);
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

    private void processVariableDeclaration(String id,VarDeclarationContext ctx) {
        //parse the variable type
        String type = ctx.type().types().getText();
        if(!id.startsWith("error_")){
            printv(() -> "Adding variable: "+id,1);
            switch(type){
                case "int":
                    //declare the variable and allocate literals
                    Variable var = Variable.of(2);
                    getVariables().put(id,var);
                    printv(() -> ("new variable: "+id+" ("+var.getLiterals()+")"),1);

                    //check whether this is an initialisation
                    if(ctx.assign()!=null){
                        //TODO code breaks with arrays
                        int val = getIntegerFromExpression(ctx.assign().expression());
                        initialValues.put(id, val);
                        printv(() -> "Initialised " + id + " to " + initialValues.get(id),1);
                    }
                    break;
                default:
                    throw new RuntimeException("Unsupported declaration encountered: "+type);
            }
        }
    }

    private void processInputs(VarDeclarationContext ctx) {
        //obtain the inputs (expects integer array)
        ctx.assign().expression()
                .andExpression(0)
                .booleanExpression(0)
                .addExpression(0)
                .mulExpression(0)
                .operand(0).staticArray()
                .expression().forEach(inputvalue ->
                        //parse any integers
                        inputs.put(getIntegerFromExpression(inputvalue),new Literal())
        );
        System.out.println("new inputs: " + inputs);
    }

    public abstract void build();

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

    public List<ProblemSet.PropertyPair> getProperties(){
        if(getP()==null || getP().size()==0)throw new IllegalStateException("P[] not initialized, execute tree walk first");
        return getP();
    }

    private static Integer getIntegerFromExpression(ExpressionContext ctx){
        //TODO assertions for every node
        Optional<String> literal = Optional.of(ctx)
                .map(e -> e.andExpression(0))
                .map(a -> a.booleanExpression(0))
                .map(b -> b.addExpression(0))
                .map(a -> a.mulExpression(0))
                .map(m -> m.operand(0))
                .map(OperandContext::NUMBER)
                .map(ParseTree::getText);
        Optional<String> nestedLiteral = Optional.of(ctx)
                .map(e -> e.andExpression(0))
                .map(a -> a.booleanExpression(0))
                .map(b -> b.addExpression(0))
                .map(a -> a.mulExpression(0))
                .map(m -> m.operand(0))
                .map(OperandContext::expression)
                .map(e -> e.andExpression(0))
                .map(a -> a.booleanExpression(0))
                .map(b -> b.addExpression(0))
                .map(a -> a.mulExpression(0))
                .map(m -> m.operand(0))
                .map(OperandContext::NUMBER)
                .map(ParseTree::getText);
        return Integer.parseInt(literal.orElseGet(nestedLiteral::get));
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
        printv(() -> "Only one input enabled: " + result, 2);
        return result;
    }

    public static String getMethodName(FunctionCallContext ctx){
        return ctx.var().IDENTIFIER().getText();
    }

    public static String getMethodName(FunctionDeclarationContext ctx){
        return ctx.var().IDENTIFIER().getText();
    }
}