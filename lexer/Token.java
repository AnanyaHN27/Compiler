package compiler;

import java.util.HashMap;

public class Token {
    public enum Tokens {
        SDNToken,
        AddToken,
        SubToken,
        MultToken,
        CosineToken,
        FactToken,
        LineEndToken,
    }

    private Tokens type;
    private Double value;

    public Token(Tokens type, Double value) {
        this.type = type;
        this.value = value;
        if(value != null) generateSDNNode();
    }

    public Token(Tokens type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public Tokens getType() {
        return type;
    }

    public HashMap<Tokens, Variable> tokenToSymbol = new HashMap<>(){{
        put(Tokens.SDNToken, Variable.sdn);
        put(Tokens.AddToken, Variable.plus);
        put(Tokens.SubToken, Variable.minus);
        put(Tokens.MultToken, Variable.times);
        put(Tokens.CosineToken, Variable.cos);
        put(Tokens.FactToken, Variable.factorial);
        put(Tokens.LineEndToken, Variable.lineEnd);
    }};

    public HashMap<Tokens, String> tokenToChar = new HashMap<>(){{
        //put(Tokens.SDNToken, value);
        put(Tokens.AddToken, "+");
        put(Tokens.SubToken, "-");
        put(Tokens.MultToken, "*");
        put(Tokens.CosineToken, "cos");
        put(Tokens.FactToken, "!");
        //put(Tokens.LineEndToken, Symbol.lineEnd);
    }};

    public Tree generateSDNNode() {
        return new SDNNode(type, value);
    }
}
