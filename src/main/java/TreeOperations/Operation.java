package TreeOperations;

import AST.ASTNode;

public interface Operation {
    <T> T visit(ASTNode astNode) throws Exception;
}
