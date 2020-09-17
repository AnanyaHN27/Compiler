package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws LexingException {

        Lexer lexer = new Lexer("-2+-3");
        /**
        System.out.println("BREAK");
        for (Token token: lexer.tokenList){
            System.out.println(token.getType());
            if(token.getValue() != null){
                System.out.println(token.getValue());
            }
        }
        **/
        Parser parser = new Parser(Grammar.grammar);
        System.out.println(parser.parse(lexer.tokenList));
        /**
        List<ProductionRule> productionRules = new ArrayList<>();
        productionRules.add(new ProductionRule(Variable.Calc, List.of(Variable.Sum, '+' ,Variable.Sum)));
        productionRules.add(new ProductionRule(Variable.Fact, List.of(Variable.Sub)));
        Grammar grammar = new Grammar(productionRules);
        for(ProductionRule rule: grammar.getRules()){
            if (rule.getHead() == Variable.Calc){
                System.out.println("yes2");
            }
        }
        if (grammar.getRules().contains(Variable.Calc)){
            System.out.println("yes");
        }
         **/

    }

}
