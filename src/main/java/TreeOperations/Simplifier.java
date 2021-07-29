package TreeOperations;

import AST.*;
import Token.Token;
import Token.TokenType;

public class Simplifier implements Operation{

    //We need to apply this procedure multiple times
    public ASTNode simplifyLoop(ASTNode astNode) throws Exception {
        ASTNode originalAST = astNode;
        ASTNode newAST = visit(originalAST);

        while (!originalAST.toString().equals(newAST.toString())){
            originalAST = newAST;
            newAST = visit(newAST);
        }
        return newAST;
    }

    @Override
    public ASTNode visit(ASTNode astNode) throws Exception {
        NodeType nodeType = astNode.getNodeType();
        Token token = astNode.getToken();
        switch (nodeType){
            case BINARY:
                return visitBinary((BinaryOp) astNode, token);
            case UNARY:
                return visitUnary((UnaryOP) astNode, token);
            case NUM:
                return astNode;
            case VAR:
                return astNode;
            default:
                throw new Exception("Error simplifying, unrecognised node");
        }
    }

    private ASTNode visitBinary(BinaryOp binaryOp, Token token) throws Exception {
        ASTNode left = binaryOp.getLeft();
        ASTNode right = binaryOp.getRight();

        //the simplified versions of the left and right nodes
        ASTNode leftSimp = visit(left);
        ASTNode rightSimp = visit(right);

        //apply the binary rules to the left and right nodes
        switch (token.getType()){
            case PLUS:
                return binaryAdditionRules(leftSimp, rightSimp);
            case MINUS:
                return binaryMinusRules(leftSimp, rightSimp);
            case MUL:
                return binaryMultiplyRules(leftSimp, rightSimp);
            case DIV:
                return binaryDivisionRules(leftSimp, rightSimp);
            default:
                //otherwise (when it's a power) return the operation
                return binaryOp;
        }
    }

    private ASTNode visitUnary(UnaryOP unaryOP, Token token) throws Exception {
        ASTNode operandSimp = visit(unaryOP.getOperand());
        switch (token.getType()){
            case PLUS:
                //+x -> x
                return operandSimp;
            case MINUS:
                return unaryMinusRules(operandSimp);
            default:
                return new UnaryOP(token, operandSimp);
        }
    }

    private ASTNode unaryMinusRules(ASTNode operand){
       // --x -> +x -> x
       Token operandToken = operand.getToken();
       if (operand instanceof UnaryOP && operandToken.getType() == TokenType.MINUS) {
           return ((UnaryOP) operand).getOperand();
       }
       else{
           return operand;
       }
    }

    private ASTNode binaryAdditionRules(ASTNode left, ASTNode right){
        Token leftToken = left.getToken();
        Token rightToken = right.getToken();

        TokenType leftType = leftToken.getType();
        TokenType rightType = rightToken.getType();

        TokenType numType = TokenType.NUMBER;
        TokenType plusType = TokenType.PLUS;

        Token tempToken;
        String tempString;

        //Simplify if:
        //left is num and right is num -> num
        //left is anything and right is 0 -> anything
        //left is 0 and right is anything -> anything

        //unimplemented
        //left is anything and right is unary minus -> var - right.operand

        if (patternMatcher(leftType, rightType, numType, numType)){
            //left is num and right is num
            Double leftVal = Double.parseDouble(leftToken.getIdentifier());
            Double rightVal = Double.parseDouble(rightToken.getIdentifier());
            tempString = String.valueOf(leftVal + rightVal);
            tempToken = new Token(numType, tempString);
            return new Num(tempToken);
        }
        else if (rightToken.getIdentifier().equals("0")){
            //left is anything and right is 0
            return left;
        }
        else if (leftToken.getIdentifier().equals("0")){
            //left is 0 and right is anything
            return right;
        }
        else {
            tempToken = new Token(plusType, "+");
            return new BinaryOp(tempToken, left, right);
        }
    }

    private ASTNode binaryMinusRules(ASTNode left, ASTNode right) throws Exception {
        //Simplify if:
        //left is num and right is num -> num
        //left is num and right is unary minus -> num+right.operand
        //left is var and right is var -> 0
        //left is var and right is unary minus -> var+right.operand
        //left is 0 and right is anything -> -anything
        //left is anything and right is 0 -> anything

        Token leftToken = left.getToken();
        Token rightToken = right.getToken();

        TokenType leftType = leftToken.getType();
        TokenType rightType = rightToken.getType();

        TokenType numType = TokenType.NUMBER;
        TokenType varType = TokenType.VAR;
        TokenType minusType = TokenType.MINUS;
        TokenType plusType = TokenType.PLUS;

        Token tempToken;

        if (patternMatcher(leftType, rightType, numType, numType)){
            //left is num and right is num -> num
            Double leftVal = Double.parseDouble(leftToken.getIdentifier());
            Double rightVal = Double.parseDouble(rightToken.getIdentifier());
            Double sum = leftVal + rightVal;

            //if sum is negative, store the new node as unaryMinus(abs(left+right))
            if (sum < 0){
                tempToken = new Token(minusType);
                Token tempInnerToken = new Token(numType, String.valueOf(sum * -1));
                ASTNode tempOperand = new Num(tempInnerToken);
                return new UnaryOP(tempToken, tempOperand);
            }
            else{
                String tempString = String.valueOf(sum);
                tempToken = new Token(numType, tempString);
                return new Num(tempToken);
            }
        }
        else if (patternMatcher(leftType, rightType, varType, varType)){
            //left is var and right is var -> 0
            tempToken = new Token(numType, "0");
            return new Num(tempToken);
        }
        else if (rightType == minusType && right instanceof UnaryOP){
            //left is anything and right is unary minus -> var+right.operand
            tempToken = new Token(plusType);
            return new BinaryOp(tempToken, left, ((UnaryOP) right).getOperand());
        }
        else if (rightToken.getIdentifier().equals("0")){
            //left is anything and right is 0 -> anything
            return left;
        }
        else if (leftToken.getIdentifier().equals("0")){
            //left is 0 and right is anything -> -anything
            tempToken = new Token(minusType);
            return new UnaryOP(tempToken, right);
        }
        //else
        tempToken = new Token(minusType);
        return new BinaryOp(tempToken ,left, right);
    }

    private ASTNode binaryMultiplyRules(ASTNode left, ASTNode right) throws Exception {
        //Simplify if:
        //left is anything and right is 0
        //left is anything and right is 1
        //left is 0 and right is anything
        //left is 1 and right is anything
        //left is num and right is num
        //left is unary minus and right is unary minus
        Token leftToken = left.getToken();
        Token rightToken = right.getToken();

        TokenType leftType = leftToken.getType();
        TokenType rightType = rightToken.getType();

        Token tempToken;
        TokenType numType = TokenType.NUMBER;
        TokenType minusType = TokenType.MINUS;
        TokenType mulType = TokenType.MUL;

        if (rightToken.getIdentifier().equals("0")){
            //left is anything and right is 0
            tempToken = new Token(numType, "0");
            return new Num(tempToken);
        }
        else if (rightToken.getIdentifier().equals("1")){
            //left is anything and right is 1
            return left;
        }
        else if (leftToken.getIdentifier().equals("0")){
            //left is 0 and right is anything
            tempToken = new Token(numType, "0");
            return new Num(tempToken);
        }
        else if (leftToken.getIdentifier().equals("1")){
            //left is 1 and right is anything
            return right;
        }
        else if(patternMatcher(leftType, rightType, numType, numType)){
            Double leftVal = Double.parseDouble(leftToken.getIdentifier());
            Double rightVal = Double.parseDouble(rightToken.getIdentifier());

            String prod = String.valueOf(leftVal * rightVal);
            tempToken = new Token(numType, prod);
            return new Num(tempToken);
        }
        else if (patternMatcher(leftType, rightType, minusType, minusType)){
            if (left instanceof UnaryOP && right instanceof UnaryOP){
                tempToken = new Token(mulType);
                ASTNode leftOP = ((UnaryOP) left).getOperand();
                ASTNode rightOp = ((UnaryOP) right).getOperand();
                return new BinaryOp(tempToken, leftOP, rightOp);
            }
        }
        tempToken = new Token(mulType);
        return new BinaryOp(tempToken ,left, right);
    }

    private ASTNode binaryDivisionRules(ASTNode left, ASTNode right) throws Exception {
        //Simplify if:
        //left is var and right is var
        //left is anything and right is 1
        //left is 0 and right is anything

        Token leftToken = left.getToken();
        Token rightToken = right.getToken();

        TokenType leftType = leftToken.getType();
        TokenType rightType = rightToken.getType();

        TokenType varType = TokenType.VAR;
        TokenType numType = TokenType.NUMBER;
        TokenType divType = TokenType.DIV;

        Token tempToken;
        if (patternMatcher(leftType, rightType, varType, varType)){
            tempToken = new Token(numType, "1");
            return new Num(tempToken);
        }
        else if (rightToken.getIdentifier().equals("1")){
            return left;
        }
        else if (leftToken.getIdentifier().equals("0")){
            tempToken = new Token(numType, "0");
            return new Num(tempToken);
        }
        else{
            tempToken = new Token(divType);
            return new BinaryOp(tempToken, left, right);
        }
    }

    private boolean patternMatcher(TokenType leftType, TokenType rightType, TokenType leftMatch, TokenType rightMatch){
        boolean left = (leftType == leftMatch);
        boolean right = (rightType == rightMatch);
        return left && right;
    }
}
