package compiler;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Lexer lexer = new Lexer("cos2+3!");
        List<Token> tokenList = lexer.tokenize("cos2+3!");
        System.out.println("BREAK");
        for (Token token: tokenList){
            System.out.println(token.getType());
            if(token.getValue() != null){
                System.out.println(token.getValue());
            }
        }
    }

}
