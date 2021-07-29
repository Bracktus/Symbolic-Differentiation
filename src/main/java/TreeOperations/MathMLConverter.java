package TreeOperations;

import AST.ASTNode;
import AST.BinaryOp;
import AST.NodeType;
import AST.UnaryOP;
import Token.Token;
import Token.TokenType;

public class MathMLConverter implements Operation{

    public String toMathML(ASTNode astNode, String prefix) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<math display=\"block\">" );
        stringBuilder.append(prefix);
        stringBuilder.append(visit(astNode));
        stringBuilder.append("</math>");
        return stringBuilder.toString();
    }

    @Override
    public String visit(ASTNode astNode) throws Exception {
        NodeType nodeType = astNode.getNodeType();
        StringBuilder stringBuilder = new StringBuilder();
        switch (nodeType){
            case BINARY:
                return visitBinary((BinaryOp) astNode);
            case UNARY:
                return visitUnary((UnaryOP) astNode);
            case NUM:
                stringBuilder.append("<mn>");
                stringBuilder.append(astNode.getToken().getIdentifier());
                stringBuilder.append("</mn>");
                break;
            case VAR:
                stringBuilder.append("<mi>");
                stringBuilder.append(astNode.getToken().getIdentifier());
                stringBuilder.append("</mi>");
        }
        return stringBuilder.toString();
    }

    private String visitBinary(BinaryOp binaryOp) throws Exception {
        TokenType tokenType = binaryOp.getToken().getType();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<mrow>");
        switch (tokenType){
            case DIV:
                stringBuilder.append("<mfrac>");
                stringBuilder.append(visit(binaryOp.getLeft()));
                stringBuilder.append(visit(binaryOp.getRight()));
                stringBuilder.append("</mfrac>");
                break;
            case POW:
                stringBuilder.append("<msup>");
                stringBuilder.append(visit(binaryOp.getLeft()));
                stringBuilder.append(visit(binaryOp.getRight()));
                stringBuilder.append("</msup>");
                break;
            default:
                String identifier = binaryOp.getToken().getIdentifier();
                stringBuilder.append(visit(binaryOp.getLeft()));
                stringBuilder.append("<mo>");
                stringBuilder.append(identifier);
                stringBuilder.append("</mo>");
                stringBuilder.append(visit(binaryOp.getRight()));
        }
        stringBuilder.append("</mrow>");
        return stringBuilder.toString();
    }

    private String visitUnary(UnaryOP unaryOP) throws Exception{
        Token token = unaryOP.getToken();
        TokenType tokenType = token.getType();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<mrow>");
        if (tokenType == TokenType.PLUS || tokenType == TokenType.MINUS){
            stringBuilder.append("<mo>" + token.getIdentifier() + "</mo>");
            stringBuilder.append(visit(unaryOP.getOperand()));
        }
        else{
            stringBuilder.append("<mi>" + token.getIdentifier() + "</mi>");
            stringBuilder.append("<mo> ( </mo>");
            stringBuilder.append(visit(unaryOP.getOperand()));
            stringBuilder.append("<mo> ) </mo>");
        }
        stringBuilder.append("</mrow>");
        return stringBuilder.toString();
    }
}
