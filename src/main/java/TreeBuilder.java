import AST.*;
import Token.Token;
import Token.TokenList;
import Token.TokenType;

import java.util.Iterator;

public class TreeBuilder {

    private Token currentToken;
    private final Iterator<Token> tokenIterator;

    private double varVal;
    private boolean varValSet = false;

    public TreeBuilder(TokenList tokenList){
        this.tokenIterator = tokenList.iterator();
        this.currentToken = tokenIterator.next();
    }

    private void eat(TokenType tokenType){
        if (currentToken.getType() == tokenType){
            currentToken = tokenIterator.next();
        }
        else{
            System.out.println("Invalid Syntax");
        }
    }

    public void setVarVal(Double varVal){
        this.varVal = varVal;
        this.varValSet = true;
    }

    public ASTNode buildTree() throws Exception {
        ASTNode node = p1();
        if (currentToken.getType() == TokenType.EOF){
            return node;
        }
        else{
            throw new Exception("Syntax Error, too many tokens.");
        }
    }

    private ASTNode p1() throws Exception {
        //P1 -> P2 ((PLUS|MINUS) P2)*
        //P2 -> P3 ((MUL|DIV) P3)*
        //P3 -> BASE (POW BASE)*
        //BASE -> (PLUS|MINUS|SIN|COS|TAN|LOG)P3 | NUMBER | LPAREN P1 RPAREN | VAR

        ASTNode res = p2();

        while(currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS){
            Token token = currentToken;
            switch (currentToken.getType()) {
                case PLUS -> eat(TokenType.PLUS);
                case MINUS -> eat(TokenType.MINUS);
            }
            res = new BinaryOp(token, res, p2());
        }
        return res;
    }

    private ASTNode p2() throws Exception {
        //P2 -> P3 ((MUL|DIV) P3)*

        ASTNode res = p3();

        while (currentToken.getType() == TokenType.MUL || currentToken.getType() == TokenType.DIV){
            Token token = currentToken;
            switch (currentToken.getType()) {
                case MUL -> eat(TokenType.MUL);
                case DIV -> eat(TokenType.DIV);
            }
            res = new BinaryOp(token, res, p3());
        }
       return res;
    }

    private ASTNode p3() throws Exception{
        //P3 -> BASE (POW BASE)*
        ASTNode res = base();

        while (currentToken.getType() == TokenType.POW){
            Token token = currentToken;
            eat(TokenType.POW);
            res = new BinaryOp(token, res, base());
        }
        return res;
    }

    private ASTNode base() throws Exception {
        //BASE -> (PLUS|MINUS|SIN|COS|TAN|LOG)P3 | NUMBER | LPAREN P1 RPAREN

        Token token = currentToken;
        ASTNode result;
        switch (currentToken.getType()) {
            case NUMBER -> {
                eat(TokenType.NUMBER);
                return new Num(token);
            }
            case VAR -> {
                eat(TokenType.VAR);
                if(varValSet){
                    return new Var(token, varVal);
                }
                else{
                    return new Var(token);
                }
            }
            case LPAREN -> {
                eat(TokenType.LPAREN);
                result = p1();
                eat(TokenType.RPAREN);
                return result;
            }
            case MINUS -> {
                eat(TokenType.MINUS);
                result = new UnaryOP(token, base());
                return result;
            }
            case PLUS -> {
                eat(TokenType.PLUS);
                result = new UnaryOP(token, base());
                return result;
            }
            case SIN -> {
                eat(TokenType.SIN);
                result = new UnaryOP(token, base());
                return result;
            }
            case COS -> {
                eat(TokenType.COS);
                result = new UnaryOP(token, base());
                return result;
            }
            case TAN -> {
                eat(TokenType.TAN);
                result = new UnaryOP(token, base());
                return result;
            }
            case LOG -> {
                eat(TokenType.LOG);
                result = new UnaryOP(token, base());
                return result;
            }
            default -> throw new Exception("Syntax Error, unary function not recognised.");
        }
    }
}
