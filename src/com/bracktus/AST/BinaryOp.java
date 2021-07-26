package com.bracktus.AST;

import com.bracktus.Token.Token;
import com.bracktus.Token.TokenType;

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

    /**
     * Counts the number of nodes in the tree
     * @return the number of nodes in the tree
     */
    @Override
    public int countNodes() {
        return 1 + left.countNodes() + right.countNodes();
    }

    @Override
    public double evaluate() throws Exception {
        switch (token.getType()){
            case PLUS:
                return left.evaluate() + right.evaluate();
            case MINUS:
                return left.evaluate() - right.evaluate();
            case MUL:
                return left.evaluate() * right.evaluate();
            case DIV:
                return left.evaluate() / right.evaluate();
            case POW:
                return Math.pow(left.evaluate(), right.evaluate());
            default:
                throw new Exception("Error evaluating binary operator");
        }
    }

    @Override
    public ASTNode differentiate() throws Exception {
        ASTNode node;

        ASTNode leftDiff = left.differentiate();
        ASTNode rightDiff = right.differentiate();

        ASTNode tempLeft;
        ASTNode tempRight;
        Token plusToken = new Token(TokenType.PLUS, "+");
        Token minusToken = new Token(TokenType.MINUS, "-");

        Token mulToken = new Token(TokenType.MUL, "*");
        Token divToken = new Token(TokenType.DIV, "/");
        Token powToken = new Token(TokenType.POW, "^");

        Token logToken = new Token(TokenType.LOG, "log");
        switch (token.getType()){
            case PLUS:
                 node = new BinaryOp(token, leftDiff, rightDiff);
                 break;
            case MINUS:
                 node = new BinaryOp(token, leftDiff, rightDiff);
                 break;
            case MUL:
//               f(x)  = g(x) * h(x)
//               f'(x) = g(x) * h'(x) + g'(x) * h(x)

                 tempLeft = new BinaryOp(token, left, rightDiff);
                 tempRight = new BinaryOp(token, right, leftDiff);

                 node = new BinaryOp(plusToken, tempLeft, tempRight);
                 break;
            case DIV:
//              f(x)  = g(x) / h(x)
//              f'(x) = h(x) * g'(x) - g(x) * h'(x) / (h(x))^2

                ASTNode two = new Num(new Token(TokenType.NUMBER, "2"));
                ASTNode numeratorLeft = new BinaryOp(mulToken, leftDiff, right);
                ASTNode numeratorRight = new BinaryOp(mulToken, left, rightDiff);

                ASTNode numerator = new BinaryOp(minusToken, numeratorLeft, numeratorRight);
                ASTNode denominator = new BinaryOp(powToken, right, two);

                node = new BinaryOp(token, numerator, denominator);
                break;
            case POW:
//              https://en.wikipedia.org/wiki/Differentiation_rules#Generalized_power_rule
                // (f^g)' = f^g * (f' * g/f + g' * log(f))
                // let g/f = a
                // let log(f) = b
                // let f' * g/f = f' * a = c
                // let g' * log(f) = g' * b = d
                // let f' * g/f + g' * log(f) = c + d = e
                // let (f^g)' = f^g * e

                ASTNode a = new BinaryOp(divToken, right, left);
                ASTNode b = new UnaryOP(logToken, left);
                ASTNode c = new BinaryOp(mulToken, leftDiff, a);
                ASTNode d = new BinaryOp(mulToken, rightDiff, b);
                ASTNode e = new BinaryOp(plusToken, c, d);
                node = new BinaryOp(mulToken, this, e);
                break;
            default:
                throw new Exception("Error at " + this.token.getIdentifier());
        }
        return node;
    }

}

