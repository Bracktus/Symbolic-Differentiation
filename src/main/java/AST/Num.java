package AST;

import Token.Token;

public class Num extends ASTNode {

    private NodeType nodeType;

    public Num(Token token){
       this.token = token;
       this.nodeType = NodeType.NUM;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public NodeType getNodeType() {
        return nodeType;
    }

    @Override
    public String toString() {
        return token.getIdentifier();
    }
}
