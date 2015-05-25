package ic3cub3.rersparser;

import ic3cub3.plf.AndFormula;
import ic3cub3.plf.Formula;
import ic3cub3.plf.Literal;
import ic3cub3.plf.OrFormula;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.rersparser.ProblemParser.StatementContext;
import ic3cub3.runner.Runner;
import ic3cub3.tests.ProblemSet;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static ic3cub3.rersparser.ProblemParser.*;
import static ic3cub3.runner.Runner.printv;

/**
 * Class for encoding the RERS challenge programs as transition systems and properties suitable for IC3
 */
@Setter
@Getter
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

    public Cube parseSingleStatement(StatementContext ctx){
        if(ctx.assignStatement()!=null)return prepareAssignment(ctx.assignStatement());
        if(ctx.functionCall()!=null)return prepareFunctionCall(ctx.functionCall());
        if(ctx.ifStatement()!=null)return prepareIf(ctx.ifStatement());
        if(ctx.closedCompoundStatement()!=null)return parseMultipleStatement(ctx.closedCompoundStatement());
        throw new UnsupportedOperationException("Unable to parse statement: "+ctx);
    }

    public Cube parseMultipleStatement(ClosedCompoundStatementContext closedCompoundStatementContext){
        return new Cube(closedCompoundStatementContext.compoundStatement().statement().stream()
                .map(this::parseSingleStatement)
                .map(Cube::getClauses)
                .flatMap(Set::stream)
                .collect(Collectors.toList()));
    }

    private Cube prepareAssignment(AssignStatementContext assignStatementContext) {
        assert(assignStatementContext!=null);
        assert(getVariables().containsKey(assignStatementContext.var().IDENTIFIER().getText()));
        assert(assignStatementContext.expression().andExpression(0).booleanExpression(0).addExpression(0).mulExpression(0).operand(0).NUMBER()!=null);
        return parseAssignment(getVariables().get(assignStatementContext.var().IDENTIFIER().getText()),
               getIntegerFromExpression(assignStatementContext.expression()));
    }

    private Cube prepareFunctionCall(FunctionCallContext functionCallContext) {
        assert(getInlinedMethods().containsKey(functionCallContext.var().IDENTIFIER().getText()));
        return getInlinedMethods().get(functionCallContext.var().IDENTIFIER().getText());
    }

    private Formula parseExpressionCondition(ExpressionContext ctx){
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
        if(ctx.EQUAL()!=null) {
            //TODO assumes id==number, and assumes no negative integers are compared
            String id = ctx.addExpression(0).mulExpression(0).operand(0).getText();
            int value = Integer.parseInt(ctx.addExpression(1).mulExpression(0).operand(0).NUMBER().getText());
            assert(getVariables().containsKey(id));
            return parseEqual(getVariables().get(id),value);
        }else{
            return parseExpressionCondition(ctx.addExpression(0).mulExpression(0).operand(0).expression());
        }
    }

    private Cube prepareIf(IfStatementContext ifStatementContext) {
        return parseExpressionCondition(ifStatementContext.expression())
                .implies(
                        parseSingleStatement(ifStatementContext.statement()).toFormula()
                ).toEquivalentCube();
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
            System.out.println("Error number "+errorids.get(condition)+" has condition "+condition.getText());
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
                    Runner.printv(() -> "Warning: skipping over unsupported method: " + methodName, 1);
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
            System.out.println("Adding variable: "+id);
            switch(type){
                case "int":
                    //declare the variable and allocate literals
                    getVariables().put(id,Variable.of(32));

                    //check whether this is an initialisation
                    if(ctx.assign()!=null){
                        //TODO code breaks with arrays
                        int val = getIntegerFromExpression(ctx.assign().expression());
                        initialValues.put(id, val);
                        System.out.println("Initialised " + id + " to " + initialValues.get(id));
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
        return Integer.parseInt(ctx.andExpression(0).booleanExpression(0).addExpression(0).mulExpression(0).operand(0).NUMBER().getText());
    }
}