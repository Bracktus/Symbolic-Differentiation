package AST;

import Token.Token;

public class BinaryOp extends ASTNode{
    private ASTNode left;
    private ASTNode right;
    private NodeType nodeType;

    public BinaryOp(Token token, ASTNode left, ASTNode right){
        this.token = token;
        this.left = left;
        this.right = right;
        this.nodeType = NodeType.BINARY;
    }

    public ASTNode getLeft() {
        return left;
    }

    public ASTNode getRight() {
        return right;
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

        stringBuilder.append("(" + left.toString());
        stringBuilder.append(" " + token.getIdentifier() + " ");
        stringBuilder.append(right.toString() + ")");

        return stringBuilder.toString();
    }
}

