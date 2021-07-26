package com.bracktus.AST;

import com.bracktus.Token.Token;
import com.bracktus.Token.TokenType;

public class Var extends ASTNode{

    private double value;
    private boolean hasValue;
    private NodeType nodeType;

    public Var(Token token){
        this.token = token;
        this.hasValue = false;
        this.nodeType = NodeType.VAR;
    }

    public Var(Token token, int value){
        this.token = token;
        this.hasValue = true;
        this.value = value;
        this.nodeType = NodeType.VAR;
    }

    public void setValue(double value) {
        this.hasValue = true;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public boolean hasValue() {
        return hasValue;
    }

    @Override
    public Token getToken() {
        return this.token;
    }

    @Override
    public NodeType getNodeType() {
        return nodeType;
    }

    @Override
    public int countNodes() {
        return 1;
    }

    @Override
    public double evaluate() throws Exception {
        if (hasValue){
            return value;
        }
        else{
            throw new Exception(token.getIdentifier() + " has no value. Unable to evaluate expression");
        }
    }

    @Override
    public ASTNode differentiate() {
        return new Num(new Token(TokenType.NUMBER, "1"));
    }

}
