package ic3cub3.rersparser;

import ic3cub3.plf.Formula;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        return new Cube(IntStream.range(0, getVariables().size()).boxed().sequential().map(
                //test whether the i-th bit in value is 1 or 0
                i -> ((value >> i) & 1) == 1 ?
                        var.getLiterals().get(i).getPrimed()
                        : var.getLiterals().get(i).not().getPrimed())
                .map(Clause::new)
                .collect(Collectors.toList()));
    }

    @Override
    public Formula parseEqual(Variable var, int val) {
        //TODO remove the conversion to cube
        return new Cube(IntStream.range(0, getVariables().size()).boxed().sequential().map(
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
        //TODO
        //Build call tree (assume that it is indeed a tree)
        //Inline method calls and convert C to PLF
    }
}
