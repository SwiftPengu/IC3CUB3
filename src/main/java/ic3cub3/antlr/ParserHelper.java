package ic3cub3.antlr;

import ic3cub3.tests.ProblemSet;
import lombok.Getter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Getter
public class ParserHelper {
	private ProblemTreeWalker generator;
	
	public ParserHelper(){
		generator = new ProblemTreeWalker();
	}
	
	public ProblemSet parse(File f) throws IOException{
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(f));
        ProblemLexer lexer = new ProblemLexer(input);
        ProblemParser parser = new ProblemParser(new CommonTokenStream(lexer));
        parser.addParseListener(new ExceptionListener());
        ParseTree tree = parser.program();
        
        //Walk over the tree
        long timer = System.currentTimeMillis();
        System.out.println("Analyzing parse tree");
        ParseTreeWalker tw = new ParseTreeWalker();
        tw.walk(getGenerator(), tree);
        System.out.println("Finished analyzing parse tree");
        System.out.printf("Took: %dms\n",System.currentTimeMillis()-timer);
		return getGenerator().getProblemSet();
	}
}
