package compiler;

public class SDNNode extends Tree{

    private double value;

    protected SDNNode(Token.Tokens name, Double value) {
        super(name);
        this.value = value;
    }
}
