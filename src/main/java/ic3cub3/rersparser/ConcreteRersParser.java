package ic3cub3.rersparser;

import ic3cub3.plf.Formula;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.rersparser.ProblemParser.*;
import ic3cub3.tests.ProblemSet;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ic3cub3.runner.Runner.printv;
import static ic3cub3.tests.ProblemSet.*;
import static java.util.Map.Entry;

public class ConcreteRersParser extends AbstractRERSParser {
    /**
     * Returns a cube representing the assignment of a value to a variable
     * @param var a variable which is assigned a value
     * @param value the value which is assigned
     * @return a cube representing the two's-complement value of value with the literals from var
     */
    public Cube parseAssignment(Variable var,int value){
        //A stream of [0..varsize)
        return new Cube(IntStream.range(0, var.getLiterals().size()).boxed().sequential().map(
                //test whether the i-th bit in value is 1 or 0
                i -> ((value >> i) & 1) == 1 ?
                        var.getLiterals().get(i).getPrimed()
                        : var.getLiterals().get(i).not().getPrimed())
                .map(Clause::new)
                .collect(Collectors.toList())).and(var.getAssignmentLiteral().toEquivalentCube());
    }

    public Formula parseInputEqual(int val){
        return getInputs().get(val);
    }

    @Override
    public Formula parseEqual(Variable var, int val) {
        return var.getCube(val).toFormula();
    }

    //0 -> variable not changed
    public Cube restrictAssignments(){
        return getVariables().values().stream().sequential().map(var ->
                var.getAssignmentLiteral().not().implies(
                        //for each literal k state that k <-> k'
                        var.getLiterals().stream().map(l -> l.iff(l.getPrimed()))
                                .reduce(Formula::and)
                                .get()
                )).reduce(Formula::and).get()
                .toEquivalentCube();
    }

    @Override
    public void build() {
        printv(() -> "Converting RERS problem to PLF",0);
        //Build call tree (assume that it is indeed a tree)
        while(parseParseableMethods()) {}
        buildI();
        //printv(() -> this.getI(),0);
        //printv(() -> this.getI().getClauses().size(),0);
        buildT();
        //printv(() -> this.getT(), 0);
        //printv(() -> this.getT().getClauses().size(),0);
        buildP();
        //printv(() -> this.getP(),0);
        //printv(() -> this.getT().getClauses().size(),0);
        printv(() -> "Done converting problem",0);
    }

    private void buildI() {
        setI(getVariables().entrySet().stream()
                        .filter(e -> getInitialValues().containsKey(e.getKey()))
                        .map(e -> e.getValue().getCube(getInitialValues().get(e.getKey())))
                        .reduce(new Cube(), Cube::and, Cube::and)
        );
    }

    private void buildT() {
        Cube resultT = restrictAssignments();
        Cube mainMethod = parseMultipleStatement(getMainMethod().statement().closedCompoundStatement());
        resultT = resultT.and(mainMethod);
        setT(resultT);
    }

    private void buildP() {
        setP(getErrorids().entrySet().stream()
                .sorted(Entry.comparingByValue())
                .map(Entry::getKey)
                .map(this::parseExpressionCondition)
                .map(f -> PropertyPair.of(f.toEquivalentCube(),f.not().toEquivalentCube()))
                .collect(Collectors.toList()));
    }

    public boolean parseParseableMethods(){
        final long[] count = {0};
        getMethodDeclarations().entrySet().stream()
                .filter(e -> !getInlinedMethods().containsKey(e.getKey()))
                .filter(e -> getAllFunctionCalls(e.getValue()).allMatch(f -> getInlinedMethods().containsKey(getMethodName(f))))
                        //actual parsing takes place here
                .forEach(e -> {getInlinedMethods().put(e.getKey(),
                        parseMultipleStatement(e.getValue().statement().closedCompoundStatement()));
                        count[0]++;});
        return count[0]>0;
    }

    public static Stream<FunctionCallContext> getAllFunctionCalls(FunctionDeclarationContext ctx){
        return flatten(ctx.statement()).filter(s -> s.functionCall()!=null)
                .filter(f -> !getMethodName(f.functionCall()).contains("print"))
                .filter(f -> !getMethodName(f.functionCall()).equals("errorCheck")).map(StatementContext::functionCall);
    }

    public static Stream<StatementContext> flatten(IfStatementContext ctx){
        return flatten(ctx.statement().closedCompoundStatement());
    }

    public static Stream<StatementContext> flatten(StatementContext ctx){
        if(ctx.ifStatement()!=null) {
            return flatten(ctx.ifStatement());
        }else if(ctx.closedCompoundStatement()!=null){
            return flatten(ctx.closedCompoundStatement());
        }else{
            return Stream.of(ctx);
        }
    }

    public static Stream<StatementContext> flatten(ClosedCompoundStatementContext ctx){
        return ctx.compoundStatement().statement().stream().flatMap(ConcreteRersParser::flatten);
    }
}
