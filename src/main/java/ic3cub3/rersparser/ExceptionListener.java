package ic3cub3.rersparser;

import org.antlr.v4.runtime.tree.ErrorNode;

public class ExceptionListener extends ProblemBaseListener{
	@Override
	public void visitErrorNode(ErrorNode node) {
		super.visitErrorNode(node);
		throw new RuntimeException("Node: "+node.toString());
	}

}
