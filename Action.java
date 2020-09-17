package compiler;

public class Action {
    public final ActionType type;
    public final ProductionRule production;
    public final int state;

    private Action(ActionType type, ProductionRule production, int state) {
        this.type = type;
        this.production = production;
        this.state = state;
    }

    public static Action shift(int state) {
        return new Action(ActionType.SHIFT, null, state);
    }
    public static Action reduce(ProductionRule production) {
        return new Action(ActionType.REDUCE, production, -1);
    }
    public static Action accept() {
        return new Action(ActionType.ACCEPT, null, -1);
    }
    public static Action error() {
        return new Action(ActionType.ERROR, null, -1);
    }

    public enum ActionType {
        SHIFT,
        REDUCE,
        ACCEPT,
        ERROR
    }
}

