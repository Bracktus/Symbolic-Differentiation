package com.bracktus;

import com.bracktus.AST.ASTNode;
import com.bracktus.Token.TokenList;
import com.bracktus.Token.Tokeniser;
import com.bracktus.TreeOperations.StringConvert;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        //String -> AST
        String expression;
        Tokeniser tokeniser;
        TokenList tokenList;
        TreeBuilder tree;
        ASTNode root;
        ASTNode derivativeRoot;

        //Tree Operators
        StringConvert stringConverter = new StringConvert();

        while (true){
            System.out.print("calc>");
            expression = scan.nextLine();
            if (expression.equals("")){
                break;
            }
            try {
                tokeniser = new Tokeniser(expression);
                tokenList = tokeniser.tokeniseExpression();
                tree = new TreeBuilder(tokenList);

                root = tree.buildTree();
                derivativeRoot = root.differentiate();
                System.out.println(stringConverter.visit(derivativeRoot));
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
}
