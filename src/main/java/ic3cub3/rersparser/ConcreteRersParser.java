package ic3cub3.rersparser;

import ic3cub3.plf.Formula;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.rersparser.ProblemParser.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ic3cub3.runner.Runner.printv;

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
                .collect(Collectors.toList()));
    }

    public Formula parseInputEqual(int val){
        return getInputs().get(val);
    }

    @Override
    public Formula parseEqual(Variable var, int val) {
        //TODO remove the conversion to cube
        return new Cube(IntStream.range(0, var.getLiterals().size()).boxed().sequential().map(
                //test whether the i-th bit in value is 1 or 0
                i -> ((val >> i) & 1) == 1 ?
                        var.getLiterals().get(i)
                        : var.getLiterals().get(i).not())
                .map(Clause::new)
                .collect(Collectors.toList())).toFormula();
    }

    //0 -> variable not changed
    public Cube restrictAssignments(){
        return getVariables().values().stream().map(var ->
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
        while(parseParseableMethods()) {
            System.out.println(getInlinedMethods());
        }
        //TODO
    }

    public boolean parseParseableMethods(){
        System.out.println(getInlinedMethods());
        long count = getMethodDeclarations().entrySet().stream()
                .filter(e -> !getInlinedMethods().containsKey(e.getKey()))
                .filter(e -> getAllFunctionCalls(e.getValue()).allMatch(f -> getInlinedMethods().containsKey(getMethodName(f))))
                .count();
        getMethodDeclarations().entrySet().stream()
                .filter(e -> !getInlinedMethods().containsKey(e.getKey()))
                .filter(e -> getAllFunctionCalls(e.getValue()).allMatch(f -> getInlinedMethods().containsKey(getMethodName(f))))
                        //actual parsing takes place here
                .forEach(e -> getInlinedMethods().put(e.getKey(),
                        parseMultipleStatement(e.getValue().statement().closedCompoundStatement())));
        return count>0;
    }

    public static String getMethodName(FunctionCallContext ctx){
        return ctx.var().IDENTIFIER().getText();
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
