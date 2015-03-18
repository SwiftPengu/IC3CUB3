package ic3cub3.antlr;

import ic3cub3.tests.Problem;

import java.io.*;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ParserHelper {
	private ParserHelper(){}
	
	public static Problem parse(File f) throws FileNotFoundException, IOException{
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(f));
        ProblemLexer lexer = new ProblemLexer(input);
        ProblemParser parser = new ProblemParser(new CommonTokenStream(lexer));
        parser.addParseListener(new ExceptionListener());
        ParseTree tree = parser.program();
        
        System.out.println(tree.getText());

        //TODO use listener to generate problem
		return null;
	}
}
