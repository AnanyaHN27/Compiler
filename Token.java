package compiler;

public class Token {
    public enum Tokens {
        InvalidToken,
        SDNToken,
        AddToken,
        SubToken,
        MultToken,
        CosineToken,
        FactToken,
        LineEndToken,
    }

    private Tokens type;
    private double value;

    public Token(Tokens type, double value) {
        this.type = type;
        this.value = value;
    }

    public Token(Tokens type) {
        this.type = type;
    }
}