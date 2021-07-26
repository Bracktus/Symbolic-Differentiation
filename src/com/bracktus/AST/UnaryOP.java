package com.bracktus.AST;

import com.bracktus.Token.Token;
import com.bracktus.Token.TokenType;

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

    /**
     * Counts the number of nodes in the tree
     * @return the number of nodes in the tree
     */
    @Override
    public int countNodes() {
        return 1 + operand.countNodes();
    }

    /**
     * Evaluates the tree. It only works with expressions without variables. E.g 2 * 3 + 1.
     * @return The result of the calculation
     */
    @Override
    public double evaluate() throws Exception {
        switch (token.getType()){
            case PLUS:
                return + operand.evaluate();
            case MINUS:
                return - operand.evaluate();
            case SIN:
                return Math.sin(operand.evaluate());
            case COS:
                return Math.cos(operand.evaluate());
            case TAN:
                return Math.tan(operand.evaluate());
            case LOG:
                return Math.log(operand.evaluate());
            default:
                return 0;
        }
    }

    @Override
    public ASTNode differentiate() throws Exception {
//      https://en.wikipedia.org/wiki/Differentiation_rules#The_chain_rule
        ASTNode node;

        ASTNode outer;
        Token outerToken;

        ASTNode operandDiff = operand.differentiate();

        Token minusToken = new Token(TokenType.MINUS, "-");
        Token mulToken = new Token(TokenType.MUL, "*");
        Token divToken = new Token(TokenType.DIV, "/");
        Token powToken = new Token(TokenType.POW, "^");

        Token oneToken = new Token(TokenType.NUMBER, "1");
        ASTNode oneNode = new Num(oneToken);
        Token sinToken = new Token(TokenType.SIN, "sin");
        Token cosToken = new Token(TokenType.COS, "cos");

        switch (token.getType()){
            case PLUS:
                node = new UnaryOP(token, operandDiff);
                break;
            case MINUS:
                node = new UnaryOP(token, operandDiff);
                break;
            case SIN:
                outer = new UnaryOP(cosToken, operand);
                node = new BinaryOp(mulToken, outer, operandDiff);
                break;
            case COS:
                outer = new UnaryOP(minusToken, new UnaryOP(sinToken, operand));
                node = new BinaryOp(mulToken, outer, operandDiff);
                break;
            case TAN:
                // (tan(x))' = (sec(x))^2 = 1 / (cos(x))^2
                Token twoToken = new Token(TokenType.NUMBER, "2");

                ASTNode twoNode = new Num(twoToken);
                ASTNode cosNode = new UnaryOP(cosToken, operand);

                ASTNode cosSqrd = new BinaryOp(powToken, cosNode, twoNode);
                ASTNode secSqrd = new BinaryOp(divToken, oneNode, cosSqrd);

                node = new BinaryOp(mulToken, secSqrd, operandDiff);
                break;
            case LOG:
                outer = new BinaryOp(divToken, oneNode, operand);
                node = new BinaryOp(mulToken, outer, operandDiff);
                break;
            default:
                throw new Exception("Differentiation error at UnaryOp.differentiate");
        }
        return node;
    }


}
