package compiler;

import java.util.List;

public class ProductionRule {
    private Variable head;
    private List<Variable> body;

    public ProductionRule(Variable head, List<Variable> body){
        this.head = head;
        this.body = body;
    }

    public Variable getHead() {
        return head;
    }

    public List<Variable> getBody() {
        return body;
    }
}
