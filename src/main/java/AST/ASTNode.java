package AST;

import Token.Token;

public abstract class ASTNode {

    protected Token token;

    public abstract Token getToken();
    public abstract NodeType getNodeType();
    public abstract String toString();
}
