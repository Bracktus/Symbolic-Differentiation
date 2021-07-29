package Token;

public class Token {

    //The Symbol the token represents. E.g +, sin, ^
    private final TokenType tokenType;
    //The string representation of the token
    private final String identifier;

    //This class could do with some changes.
    //A non-number token doesn't need a custom identifier
    //Perhaps make this an interface and have 2 versions, num and non-num
    //Non-num tokens have their identifier automatically set
    public Token(TokenType tokenType, String identifier){
        this.tokenType = tokenType;
        this.identifier = identifier;
    }

    public Token(TokenType tokenType) throws Exception {
        this.tokenType = tokenType;
        this.identifier = autoIdentify(tokenType);
    }

    private String autoIdentify(TokenType tokenType) throws Exception {
        switch (tokenType){
            case VAR:
               return "x";
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MUL:
                return "*";
            case DIV:
                return "/";
            case POW:
                return "^";
            case LPAREN:
                return "(";
            case RPAREN:
                return ")";
            case SIN:
                return "sin";
            case COS:
                return "cos";
            case TAN:
                return "tan";
            case LOG:
                return "log";
            case EOF:
                return "EOF";
            default:
                throw new Exception("Can't autoidentify, num or unidentified token");

        }
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
