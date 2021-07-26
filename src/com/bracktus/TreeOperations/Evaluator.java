package com.bracktus.TreeOperations;

import com.bracktus.AST.*;
import com.bracktus.Token.Token;

public class Evaluator implements Operation{

    @Override
    public Double visit(ASTNode astNode) throws Exception {
        NodeType nodeType = astNode.getNodeType();
        Token token = astNode.getToken();
        switch (nodeType){
            case BINARY:
                return visitBinary((BinaryOp) astNode, token);
            case UNARY:
                return visitUnary((UnaryOP) astNode, token);
            case NUM:
                return Double.parseDouble(token.getIdentifier());
            case VAR:
                return visitVar((Var) astNode, token);
            default:
                throw new Exception("Error evaluating, unrecognised node");
        }
    }

    private Double visitBinary(BinaryOp binaryOp, Token token) throws Exception{
        ASTNode left = binaryOp.getLeft();
        ASTNode right = binaryOp.getRight();
        switch (token.getType()){
            case PLUS:
                return visit(left) + visit(right);
            case MINUS:
                return visit(left) - visit(right);
            case MUL:
                return visit(left) * visit(right);
            case DIV:
                return visit(left) / visit(right);
            case POW:
                return Math.pow(visit(left), visit(right));
            default:
                throw new Exception("Error evaluating binary operator");
        }
    }

    private Double visitUnary(UnaryOP unaryOP, Token token) throws Exception {
        ASTNode operand = unaryOP.getOperand();
        switch (token.getType()){
            case PLUS:
                return + visit(operand);
            case MINUS:
                return - visit(operand);
            case SIN:
                return Math.sin(visit(operand));
            case COS:
                return Math.cos(visit(operand));
            case TAN:
                return Math.tan(visit(operand));
            case LOG:
                return Math.log(visit(operand));
            default:
                throw new Exception("Error evaluating unary operator");
        }
    }

    private Double visitVar(Var var, Token token) throws Exception {
        Boolean hasValue = var.hasValue();
        if (hasValue){
            return var.getValue();
        }
        else{
            throw new Exception(token.getIdentifier() + " has no value. Unable to evaluate expression");
        }
    }
}
