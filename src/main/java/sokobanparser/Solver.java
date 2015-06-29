package sokobanparser;

import ic3cub3.ic3.IC3;
import ic3cub3.sat.SAT4J;
import ic3cub3.tests.ProblemSet;
import sokobanparser.converter.Problem;

import java.io.File;
import java.io.FileNotFoundException;

import static ic3cub3.runner.Runner.printv;

/**
 * Class which solves a Sokoban puzzle with IC3
 */
public class Solver {
    /**
     * Starts solving the provided screen file
     * @throws FileNotFoundException when screen does not exist
     */
    public static void main(String... args)
            throws FileNotFoundException {
        long time = System.currentTimeMillis();

        // parse the screen
        Game g = SokobanParser.parseScreen(new File("sokoban/screen.2000"));
        // Solve the screen
        sleepPrint("Generating problem BDDs");
        Problem p = new Problem(g);
        ProblemSet problemSet = p.getProblemSet();
        sleepPrint("Generated problem BDDs");
        sleepPrint("Solving puzzle...");
        problemSet.check(new IC3(new SAT4J()));
        sleepPrint(String.format("Solution found in %ds (%dms)",
                (System.currentTimeMillis() - time) / 1000, System.currentTimeMillis() - time));

        // Clean exit
        System.exit(0);
    }

    private static void sleepPrint(String s){
        printv(() -> s,1);
    }
}
