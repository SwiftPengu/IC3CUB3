package ic3cub3.antlr;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data(staticConstructor = "of")
public class Variable {
    private final List<Literal> literals;
    private final Literal assignmentLiteral = new Literal();

    public static Variable of(int size){
        return Variable.of(
                IntStream.range(0, size).boxed()
                        .map(i -> new Literal())
                        .collect(Collectors.toList()));
    }

    public Cube getCube(){
        return new Cube(getLiterals().stream().map(Clause::new).collect(Collectors.toList()));
    }
}
