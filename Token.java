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
    private Double value;

    public Token(Tokens type, Double value) {
        this.type = type;
        this.value = value;
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
}