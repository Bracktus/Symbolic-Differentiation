package com.bracktus.Token;

public class Token {
    /*
    Types:
        Numbers, e.g. 7.4, 5, 908
        Binary operators, e.g. +, -, *
        Unary operators, e.g sin, cos, log...

        Also the unary operators, - and +, e.g. -1 or +7
        but we'll deal with them later

    Binary and unary operators fall under symbols
    Need to store precedence for operators
    */

    private final TokenType tokenType;
    private final String identifier;

    public Token(TokenType tokenType, String identifier){
        this.tokenType = tokenType;
        this.identifier = identifier;
    }

    public TokenType getType(){
        return this.tokenType;
    }

    public String getIdentifier(){
        return this.identifier;
    }

    public String toString(){
        return "Token( " + tokenType + ", " + identifier + ")";
    }
}
