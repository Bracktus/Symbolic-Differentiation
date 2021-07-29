package AST;

import Token.Token;
import Token.TokenType;

public class UnaryOP extends ASTNode{

    private ASTNode operand;
    private NodeType nodeType;

    public UnaryOP(Token token, ASTNode operand){
        this.token = token;
        this.operand = operand;
        this.nodeType = NodeType.UNARY;
    }

    public ASTNode getOperand() {
        return operand;
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
        StringBuilder stringBuilder = new StringBuilder();
        if (token.getType() == TokenType.PLUS || token.getType() == TokenType.MINUS){
            stringBuilder.append(token.getIdentifier());
            stringBuilder.append(operand.toString());
        }
        else{
            stringBuilder.append(token.getIdentifier());
            stringBuilder.append("(");
            stringBuilder.append(operand.toString());
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }
}
