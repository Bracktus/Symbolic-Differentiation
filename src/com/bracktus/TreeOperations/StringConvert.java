package com.bracktus.TreeOperations;

import com.bracktus.AST.*;
import com.bracktus.Token.Token;
import com.bracktus.Token.TokenType;

public class StringConvert implements Operation{

    @Override
    public String visit(ASTNode astNode) throws Exception {
        NodeType nodeType = astNode.getNodeType();
        switch (nodeType){
            case BINARY:
                return visitBinary((BinaryOp) astNode);
            case UNARY:
                return visitUnary((UnaryOP) astNode);
            case VAR:
                return visitVar((Var) astNode);
            case NUM:
                return visitNum((Num) astNode);
            default:
                throw new Exception("Unrecognised operation. See visit.");
        }
    }

   private String visitBinary(BinaryOp binaryOp) throws Exception {
       StringBuilder stringBuilder = new StringBuilder();
       Token token = binaryOp.getToken();
       ASTNode left = binaryOp.getLeft();
       ASTNode right = binaryOp.getRight();

       stringBuilder.append(visit(left));
       stringBuilder.append(" " + token.getIdentifier() + " ");
       stringBuilder.append(visit(right));

       return stringBuilder.toString();
   }

   private String visitUnary(UnaryOP unaryOP) throws Exception {
       Token token = unaryOP.getToken();
       ASTNode operand = unaryOP.getOperand();

       StringBuilder stringBuilder = new StringBuilder();
       if (token.getType() == TokenType.PLUS || token.getType() == TokenType.MINUS){
           stringBuilder.append(token.getIdentifier());
           stringBuilder.append(visit(operand));
       }
       else{
           stringBuilder.append(token.getIdentifier());
           stringBuilder.append("(");
           stringBuilder.append(visit(operand));
           stringBuilder.append(")");
       }
       return stringBuilder.toString();
   }

   private String visitVar(Var var){
        Token token = var.getToken();
        return token.getIdentifier();
   }

   private String visitNum(Num num){
       Token token = num.getToken();
       return token.getIdentifier();
   }

}
