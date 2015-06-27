package sokobanparser;

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
     * @param screen the sokoban screen to solve
     * @throws FileNotFoundException when screen does not exist
     */
    public static void processSolve(String screen)
            throws FileNotFoundException {
        long time = System.currentTimeMillis();

        // parse the screen
        Game g = SokobanParser.parseScreen(new File(screen));

        // Solve the screen
        sleepPrint("Generating problem BDDs");
        Problem p = new Problem(g);
        sleepPrint("Generated problem BDDs");

        //Start solving the screen
        Solver s = new Solver(p);
        sleepPrint("Solving puzzle...");
        String solution = s.solve();
        sleepPrint(String.format("Solution found in %ds (%dms)",
                (System.currentTimeMillis() - time) / 1000,System.currentTimeMillis()-time));
        System.out.println(solution);

        // Clean exit
        System.exit(0);
    }

    private static void sleepPrint(String s){
        printv(() -> s,1);
    }
}
