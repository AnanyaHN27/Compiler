package compiler;

import java.util.*;

public class Parser{

    private Automata automata;

    public Parser(Grammar grammar) {
        this.automata = new Automata(grammar);
    }

    public Tree parse(List<Token> tokens) {
        automata.reset();
        boolean premature_end = false;
        for (Token token : tokens) {
            if (!pass(token)) {
                premature_end = true;
                break;
            }
        }
        if (!premature_end)
            pass(null);
        return automata.getTree();
    }

    private boolean pass(Token token) {
        automata.passToken(token);
        // Handle errors, early accept etc here
        return automata.running();
    }


}