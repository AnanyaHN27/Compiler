package compiler;

import java.util.*;

public class Tree {
    public final Token.Tokens token;
    public final boolean isLeaf;
    public final List<Tree> children;

    public Tree(ProductionRule production, Tree... children) {
        Variable head = production.getHead();
        this.token = head.symbolToToken.get(head);
        this.isLeaf = children.length == 0;
        this.children = isLeaf ? null : Arrays.asList(children);
    }

    protected Tree(Token.Tokens name) {
        this.token = name;
        this.children = null;
        this.isLeaf = true;
    }
}
