package com.bracktus.AST;

import com.bracktus.Token.Token;

public abstract class ASTNode {

    protected Token token;

    public abstract Token getToken();
    public abstract NodeType getNodeType();
    public abstract int countNodes();

    public abstract double evaluate() throws Exception;
    public abstract ASTNode differentiate() throws Exception;
}
