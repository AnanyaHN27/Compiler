package compiler;

import java.util.Collections;
import java.util.List;

final class Item {
    public final ProductionRule production;
    private final Variable head;
    private final List<Variable> body;
    public final int position;

    public final Variable nextVar, sym_prev;
    private final List<Variable> prePosition, postPosition;

    public Item(ProductionRule production, int position) {
        this.production = production;
        this.head = production.getHead();
        this.body = Collections.unmodifiableList(production.getBody());
        this.position = position;

        this.prePosition = body.subList(0, position);
        this.postPosition = body.subList(position, body.size());
        this.nextVar = position >= body.size() ? null : body.get(position);
        this.sym_prev = position == 0 ? null : body.get(position - 1);
    }

    public Item(Variable variable, int position) {
        this.production = null;
        this.head = null;
        this.body = Collections.singletonList(variable);
        this.position = position;

        this.prePosition = body.subList(0, position);
        this.postPosition = body.subList(position, body.size());
        this.nextVar = position >= body.size() ? null : body.get(position);
        this.sym_prev = position == 0 ? null : body.get(position - 1);
    }

    public static boolean isKernel(Item item) {
        return item.head == null || item.sym_prev != null;
    }

    public boolean isKernel() {
        return isKernel(this);
    }
}
