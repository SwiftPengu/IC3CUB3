package ic3cub3.rersparser;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ic3cub3.runner.Runner.printv;

@Data(staticConstructor = "of")
public class Variable {
    private final List<Literal> literals;
    private final Literal assignmentLiteral = new Literal();

    public static Variable of(int size){
        Variable result = Variable.of(
                IntStream.range(0, size).boxed()
                        .map(i -> new Literal())
                        .collect(Collectors.toList()));
        printv(() -> "new variable: " + result.getAssignmentLiteral().getID() + "; " + result.getLiterals(),1);
        return result;
    }

    public Cube getCube(){
        return new Cube(getLiterals().stream().map(Clause::new).collect(Collectors.toList()));
    }

    public Cube getCube(int value){
        return new Cube(IntStream.range(0, getLiterals().size()).boxed().sequential().map(
                //test whether the i-th bit in value is 1 or 0
                i -> ((value >> i) & 1) == 1 ?
                        getLiterals().get(i)
                        : getLiterals().get(i).not())
                .map(Clause::new)
                .collect(Collectors.toList()));
    }
}
