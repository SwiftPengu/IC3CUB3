package ic3cub3.tests.actual;

import ic3cub3.plf.Literal;
import ic3cub3.plf.cnf.Clause;
import ic3cub3.plf.cnf.Cube;
import ic3cub3.tests.ProblemSet;

public class ProblemTestManual extends ProblemSet {
    private static final Literal a = new Literal();
    private static final Literal b = a.getPrimed();
    private static final Literal c = new Literal();
    private static final Literal d = c.getPrimed();

    public ProblemTestManual() {
        super(new Cube(a.not(),c),
                new Cube(new Clause(a.not(),b),
                        new Clause(a.not(),d.not()),
                        new Clause(a,b.not(),c),
                        new Clause(a,c.not(),d),
                        new Clause(b,d.not())),
                new Cube(a,c.not()).not().asCube(),
                new Cube(a,c.not()));
    }
}
