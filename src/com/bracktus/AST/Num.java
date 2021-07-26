package com.bracktus.AST;

import com.bracktus.Token.Token;
import com.bracktus.Token.TokenType;

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

    /**
     * Counts the number of nodes in the tree
     * @return the number of nodes in the tree
     */
    @Override
    public int countNodes() {
        return 1;
    }
    /**
     * Evaluates the tree. It only works with expressions without variables. E.g 2 * 3 + 1.
     * @return The result of the calculation
     */
    @Override
    public double evaluate() {
        return Double.parseDouble(token.getIdentifier());
    }

    @Override
    public ASTNode differentiate(){
        Token tempToken = new Token(TokenType.NUMBER, "0");
        return new Num(tempToken);
    }
}
