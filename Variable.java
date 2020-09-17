package compiler;

import java.util.HashMap;

public class Variable {

    public enum NonTerminal {
        CalcExp,
        SubExp,
        MultExp,
        CosineExp,
        FactExp,
    }

    public enum Terminal {
        SDN,
        PLUS,
        MINUS,
        MULT,
        COS,
        FACT,
        LINEEND,
    }

    public HashMap<String, Terminal> stringTerminalHashMap = new HashMap<>(){{
        put("SDN", Terminal.SDN);
        put("+", Terminal.PLUS);
        put("-", Terminal.MINUS);
        put("*", Terminal.MULT);
        put("cos", Terminal.COS);
        put("!", Terminal.FACT);
    }};

    public Terminal TIdentifier;
    public NonTerminal NTIdentifier;
    public boolean isTerminal;
    public boolean isNonTerminal;

    public Variable(NonTerminal identifier){
        isNonTerminal = true;
        this.NTIdentifier = identifier;
    }

    public Variable(Terminal identifier){
        isTerminal = true;
        this.TIdentifier = identifier;

    }

    public static final Variable calc = new Variable(Variable.NonTerminal.CalcExp);
    public static final Variable sub = new Variable(Variable.NonTerminal.SubExp);
    public static final Variable mult = new Variable(Variable.NonTerminal.MultExp);
    public static final Variable cosine = new Variable(Variable.NonTerminal.CosineExp);
    public static final Variable fact = new Variable(Variable.NonTerminal.FactExp);
    public static final Variable sdn = new Variable(Variable.Terminal.SDN);
    public static final Variable plus = new Variable(Variable.Terminal.PLUS);
    public static final Variable minus = new Variable(Variable.Terminal.MINUS);
    public static final Variable times = new Variable(Variable.Terminal.MULT);
    public static final Variable factorial = new Variable(Variable.Terminal.FACT);
    public static final Variable cos = new Variable(Variable.Terminal.COS);
    public static final Variable lineEnd = new Variable(Terminal.LINEEND);

    public HashMap<Variable, Token.Tokens> symbolToToken = new HashMap<>(){{
        put(Variable.sdn, Token.Tokens.SDNToken);
        put(Variable.plus, Token.Tokens.AddToken);
        put(Variable.minus, Token.Tokens.SubToken);
        put(Variable.times, Token.Tokens.MultToken);
        put(Variable.cos, Token.Tokens.CosineToken);
        put(Variable.factorial, Token.Tokens.FactToken);
        put(Variable.lineEnd, Token.Tokens.LineEndToken);
    }};
}

