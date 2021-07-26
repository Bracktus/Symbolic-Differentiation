package com.bracktus.TreeOperations;

import com.bracktus.AST.ASTNode;

public interface Operation {
    <T> T visit(ASTNode astNode) throws Exception;
}
