package com.bracktus;

import com.bracktus.AST.*;
import com.bracktus.Token.Token;
import com.bracktus.Token.TokenList;
import com.bracktus.Token.TokenType;

import java.util.Iterator;

public class TreeBuilder {

    private Token currentToken;
    private TokenList tokenList;
    private Iterator<Token> tokenIterator;

    public TreeBuilder(TokenList tokenList){
        this.tokenList = tokenList;
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

    public ASTNode buildTree() throws Exception {
        ASTNode node = p1();
        if (currentToken.getType() == TokenType.EOF){
            return node;
        }
        else{
            throw new Exception("Syntax Error, too many tokens. See buildTree");
        }
    }

    private ASTNode p1() throws Exception {
        //P1 -> P2 ((PLUS|MINUS) P2)*
        //P2 -> P3 ((MUL|DIV) P3)*
        //P3 -> BASE (POW BASE)*
        //BASE -> (PLUS|MINUS|SIN|COS|TAN|LOG)P3 | NUMBER | LPAREN P1 RPAREN

        ASTNode res = p2();

        while(currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS){
            Token token = currentToken;
            switch (currentToken.getType()){
                case PLUS:
                    eat(TokenType.PLUS);
                    break;
                case MINUS:
                    eat(TokenType.MINUS);
                    break;
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
            switch (currentToken.getType()){
                case MUL:
                    eat(TokenType.MUL);
                    break;
                case DIV:
                    eat(TokenType.DIV);
                    break;
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
        switch (currentToken.getType()){
            case NUMBER:
                eat(TokenType.NUMBER);
                return new Num(token);
            case VAR:
                eat(TokenType.VAR);
                return new Var(token);
            case LPAREN:
                eat(TokenType.LPAREN);
                result = p1();
                eat(TokenType.RPAREN);
                return result;
            case MINUS:
                eat(TokenType.MINUS);
                result = new UnaryOP(token, base());
                return result;
            case PLUS:
                eat(TokenType.PLUS);
                result = new UnaryOP(token, base());
                return result;
            case SIN:
                eat(TokenType.SIN);
                result = new UnaryOP(token, base());
                return result;
            case COS:
                eat(TokenType.COS);
                result = new UnaryOP(token, base());
                return result;
            case TAN:
                eat(TokenType.TAN);
                result = new UnaryOP(token, base());
                return result;
            case LOG:
                eat(TokenType.LOG);
                result = new UnaryOP(token, base());
                return result;
            default:
                throw new Exception("Syntax Error, unary function not recognised.");
        }
    }
}
