package compiler;

import java.util.*;

public class ProductionRule {
    private Variable head;
    private List<Object> body;

    public ProductionRule(Variable head, List<Object> body){
        this.head = head;
        this.body = body;
    }
}
