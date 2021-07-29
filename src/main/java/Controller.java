import AST.ASTNode;
import Token.TokenList;
import Token.Tokeniser;
import TreeOperations.Differentiator;
import TreeOperations.Evaluator;
import TreeOperations.MathMLConverter;
import TreeOperations.Simplifier;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Controller {
    @FXML
    private WebView expressionView;

    @FXML
    private WebView diffView;

    @FXML
    private TextField textField;

    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField evalTextField;

    @FXML
    public void processText(MouseEvent mouseEvent) throws Exception {
        try {
            String evalString = evalTextField.getText();
            String expression = textField.getText();

            //tokenising expression
            Tokeniser tokeniser = new Tokeniser(expression);
            TokenList tokenList = tokeniser.tokeniseExpression();

            //building the tree
            TreeBuilder treeBuilder = new TreeBuilder(tokenList);
            if (checkBox.isSelected()){
                if (evalString.trim().isEmpty()){
                    evalString = "1";
                    treeBuilder.setVarVal((double) 1);
                }
                else if (validNum(evalString)){
                    treeBuilder.setVarVal(Double.parseDouble(evalString));;
                }
            }
            ASTNode root = treeBuilder.buildTree();

            //differentiating
            Differentiator differentiator = new Differentiator();
            ASTNode diffRoot = differentiator.visit(root);

            //simplifying
            Simplifier simplifier = new Simplifier();
            ASTNode diffSimp = simplifier.simplifyLoop(diffRoot);

            //converting f(x) into mathml
            MathMLConverter mathMLConverter = new MathMLConverter();
            String expressionPrefix = "<mrow><mi>f</mi><mo>(</mo><mi>x</mi><mo>)</mo></mrow><mo>=</mo>";
            String funcString = mathMLConverter.toMathML(root, expressionPrefix);

            //converting f'(x) into mathml
            String diffString;
            if (checkBox.isSelected()){
                Evaluator evaluator = new Evaluator();
                String diffPrefix = "<math display=\"block\"><mrow><mi>f</mi><mo>'</mo><mo>(</mo><mn>" + evalString + "</mn><mo>)</mo></mrow><mo>=</mo>";
                diffString = diffPrefix + "<mn>" + evaluator.visit(diffSimp) + "</mn></math>";
            }
            else {
                String diffPrefix = "<mrow><mi>f</mi><mo>'</mo><mo>(</mo><mi>x</mi><mo>)</mo></mrow><mo>=</mo>";
                diffString = mathMLConverter.toMathML(diffSimp, diffPrefix);
            }

            //displaying f(x) and f'(x)
            WebEngine expressionViewWebEngine = expressionView.getEngine();
            WebEngine diffWebEngine = diffView.getEngine();
            expressionViewWebEngine.loadContent(funcString);
            diffWebEngine.loadContent(diffString);

        }catch (Exception e){
           showError(e.toString());
        }
    }

    private boolean validNum(String numString){
        try {
            Double.parseDouble(numString);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void showError(String errorMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(String.valueOf(errorMsg));
        alert.showAndWait();
    }
}
