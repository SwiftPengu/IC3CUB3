package ic3cub3.antlr;

import ic3cub3.plf.Formula;
import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class for encoding the RERS challenge programs as transition systems and properties suitable for IC3
 */
@Setter
@Getter
public abstract class AbstractRERSParser extends ProblemBaseListener{
    public abstract Map<String,Variable> getVariables();
    public abstract Map<String,ProblemParser.FunctionDeclarationContext> getMethodDeclarations();
    public abstract Map<Integer,Literal> getInputs();

    public Cube parseAssignment(String name,int value){
        Variable var = getVariables().get(name);
        return new Cube(IntStream.range(0, getVariables().size()).boxed().sequential().map(
                //test whether the i-th bit in value is 1 or 0
                i -> ((value >> i) & 1) == 1 ?
                        var.getLiterals().get(i)
                        : var.getLiterals().get(i).not())
                .map(Clause::new)
                .collect(Collectors.toList()));
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
}
