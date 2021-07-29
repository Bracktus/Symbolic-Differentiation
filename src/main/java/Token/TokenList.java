package Token;

import java.util.Iterator;
import java.util.LinkedList;


public class TokenList implements Iterable<Token>{

    private LinkedList<Token> expression;

    public TokenList(LinkedList<Token> expression) {
        this.expression = expression;
    }

    public int getLength(){
        return expression.size();
    }

    @Override
    public Iterator<Token> iterator() {
        return expression.iterator();
    }
}
