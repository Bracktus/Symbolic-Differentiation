package Token;

import java.util.LinkedList;

public class Tokeniser {

    private final String expression;
    private int position;

    public Tokeniser(String expression){
        this.expression = expression;
        this.position = 0;
    }

    private Token parseNum(Character character, int exprLen) throws Exception {

        boolean seenDecimal = false;
        StringBuilder stringBuilder = new StringBuilder(exprLen);

        //run until we don't see a digit character
        while ((character == '.' || Character.isDigit(character)) && position < exprLen){

            //Allow 1 decimal point
            if (character == '.' && seenDecimal){
                throw new Exception("Syntax Error, too many decimal points");
            }
            else if (character == '.'){
                seenDecimal = true;
            }

            stringBuilder.append(character);
            position++;

            if (position < exprLen){
                character = expression.charAt(position);
            }
            else {
                break;
            }
        }
       return new Token(TokenType.NUMBER, stringBuilder.toString());
    }

    private Token parseFunction(int exprLen) throws Exception {
       StringBuilder func = new StringBuilder();

       //copy the next 3 characters into func
       for (int i = 0; i < 3; i++){
           if (position > exprLen){
               throw new Exception("Syntax Error, error parsing function. See parseFunction");
           }
           func.append(expression.charAt(position));
           position++;
       }

       switch (func.toString()){
           case "sin":
               return new Token(TokenType.SIN);
           case "cos":
               return new Token(TokenType.COS);
           case "tan":
               return new Token(TokenType.TAN);
           case "log":
               return new Token(TokenType.LOG);
           default:
               throw new Exception("Syntax Error, unary function not recognised.");
       }
    }

    public TokenList tokeniseExpression() throws Exception {
        //loop through expression
        //if it's a binary operator or a bracket, add and move to next token
        //if it's a digit or a decimal point, add and continue on same token

        char character;
        Token token;
        int exprLen = expression.length();

        LinkedList<Token> tokenised = new LinkedList<>();
        while (position < exprLen){
            character = expression.charAt(position);

            //if the token is a number
            if (Character.isDigit(character)){
                token = parseNum(character, exprLen);
                tokenised.add(token);
            }
            //if the token is a function
            else if (Character.isAlphabetic(character) && character != 'x'){
                //pretty hacky :/
                token = parseFunction(exprLen);
                tokenised.add(token);
            }
            //if the token is a symbol
            else {
                switch (character) {
                    case '+' -> tokenised.add(new Token(TokenType.PLUS));
                    case '-' -> tokenised.add(new Token(TokenType.MINUS));
                    case '*' -> tokenised.add(new Token(TokenType.MUL));
                    case '/' -> tokenised.add(new Token(TokenType.DIV));
                    case '^' -> tokenised.add(new Token(TokenType.POW));
                    case '(' -> tokenised.add(new Token(TokenType.LPAREN));
                    case ')' -> tokenised.add(new Token(TokenType.RPAREN));
                    case 'x' -> tokenised.add(new Token(TokenType.VAR));
                }
                position++;
            }
        }
        //add it to the token list
        tokenised.add(new Token(TokenType.EOF));
        return new TokenList(tokenised);
    }
}
