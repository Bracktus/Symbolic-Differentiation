package AST;

import Token.Token;

public class Var extends ASTNode{

    private Double value;
    private boolean hasValue;
    private NodeType nodeType;

    public Var(Token token){
        this.token = token;
        this.hasValue = false;
        this.nodeType = NodeType.VAR;
    }

    public Var(Token token, Double value){
        this.token = token;
        this.hasValue = true;
        this.value = value;
        this.nodeType = NodeType.VAR;
    }

    public void setValue(Double value) {
        this.hasValue = true;
        this.value = value;
    }

    public Double getValue() {
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
    public String toString() {
        return token.getIdentifier();
    }


}
