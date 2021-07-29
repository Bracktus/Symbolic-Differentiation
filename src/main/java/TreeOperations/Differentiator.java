package TreeOperations;

import AST.*;
import Token.Token;
import Token.TokenType;

public class Differentiator implements Operation{

    @Override
    public ASTNode visit(ASTNode astNode) throws Exception {
        NodeType nodeType = astNode.getNodeType();
        Token token = astNode.getToken();
        Token tempToken;
        switch (nodeType){
            case BINARY:
                return visitBinary((BinaryOp) astNode, token);
            case UNARY:
                return visitUnary((UnaryOP) astNode, token);
            case NUM:
                tempToken = new Token(TokenType.NUMBER, "0");
                return new Num(tempToken);
            case VAR:
                tempToken = new Token(TokenType.NUMBER, "1");
                return new Num(tempToken);
            default:
                throw new Exception("Error evaluating, unrecognised node");
        }
    }

    private ASTNode visitBinary(BinaryOp binaryOp, Token token) throws Exception {
        ASTNode node;

        ASTNode left = binaryOp.getLeft();
        ASTNode right = binaryOp.getRight();
        ASTNode leftDiff = visit(left);
        ASTNode rightDiff = visit(right);

        ASTNode tempLeft;
        ASTNode tempRight;
        Token plusToken = new Token(TokenType.PLUS);
        Token minusToken = new Token(TokenType.MINUS);

        Token mulToken = new Token(TokenType.MUL);
        Token divToken = new Token(TokenType.DIV);
        Token powToken = new Token(TokenType.POW);

        Token logToken = new Token(TokenType.LOG);
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
                node = new BinaryOp(mulToken, binaryOp, e);
                break;
            default:
                throw new Exception("Error at " + token.getIdentifier());
        }
        return node;
    }

    private ASTNode visitUnary(UnaryOP unaryOP, Token token) throws Exception {
//      https://en.wikipedia.org/wiki/Differentiation_rules#The_chain_rule
        ASTNode operand = unaryOP.getOperand();
        ASTNode node;
        ASTNode outer;
        ASTNode operandDiff = visit(operand);

        Token minusToken = new Token(TokenType.MINUS);
        Token mulToken = new Token(TokenType.MUL);
        Token divToken = new Token(TokenType.DIV);
        Token powToken = new Token(TokenType.POW);

        Token oneToken = new Token(TokenType.NUMBER, "1");
        ASTNode oneNode = new Num(oneToken);
        Token sinToken = new Token(TokenType.SIN);
        Token cosToken = new Token(TokenType.COS);

        switch (token.getType()){
            case PLUS:
                //+f(x) -> +f'(x)
                node = new UnaryOP(token, operandDiff);
                break;
            case MINUS:
                //-f(x) -> -f'(x)
                node = new UnaryOP(token, operandDiff);
                break;
            case SIN:
                //sin(g(x)) -> g'(x) * cos(g(x))
                outer = new UnaryOP(cosToken, operand);
                node = new BinaryOp(mulToken, outer, operandDiff);
                break;
            case COS:
                //cos(g(x)) -> g'(x) * -sin(g(x))
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
