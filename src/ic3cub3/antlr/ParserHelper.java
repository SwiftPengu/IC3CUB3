package ic3cub3.antlr;

import ic3cub3.tests.ProblemSet;

import java.io.*;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ParserHelper {
	private ParserHelper(){}
	
	public static ProblemSet parse(File f) throws FileNotFoundException, IOException{
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(f));
        ProblemLexer lexer = new ProblemLexer(input);
        ProblemParser parser = new ProblemParser(new CommonTokenStream(lexer));
        parser.addParseListener(new ExceptionListener());
        ParseTree tree = parser.program();
        
        //Walk over the tree
        System.out.println("Analyzing parse tree");
        ProblemTreeWalker problemgenerator = new ProblemTreeWalker();
        ParseTreeWalker tw = new ParseTreeWalker();
        tw.walk(problemgenerator, tree);
        System.out.println("Finished analyzing parse tree");
		return problemgenerator.getProblemSet();
	}
}
