package compiler;

import java.util.*;

public class Automata {
    private final Grammar G;
    private final List<Set<Item>> states;
    private final Map<Integer, Map<Variable, Integer>> gotoMap;
    private final Map<Integer, Map<Variable, Action>> actionMap;
    private States.State state;

    private final Stack<Integer> stack = new Stack<>();
    private final Stack<Tree> treeStack = new Stack<Tree>();

    public Automata(Grammar G) {
        this.G = G;
        this.states = G.items();
        this.gotoMap = constructGotoMap();
        this.actionMap = constructActionMap();
        reset();
    }

    public void reset() {
        this.stack.clear();
        this.treeStack.clear();
        this.stack.push(0);
        this.state = States.State.PROCESSING;
    }

    public void passToken(Token token) {
        Variable a = token == null ? new Variable(Variable.Terminal.LINEEND) : (Variable) G.getVariables().stream()
                .filter(x -> x.isTerminal)
                .filter(x -> token.tokenToSymbol.get(token.getType()).TIdentifier == x.TIdentifier);
        while (perform(a, token)) ;
    }


    Tree getTree() {
        return accepted() ? treeStack.peek() : null;
    }

    private boolean perform(Variable a, Token token) {
        Action action = actionMap.get(this.stack.peek()).get(a);
        if (action.type == Action.ActionType.SHIFT){
            stack.push(action.state);
            treeStack.push(token.generateSDNNode());
            return false;
        }
        else if(action.type == Action.ActionType.REDUCE){
            LinkedList<Tree> list = new LinkedList<>();
            for (int i = 0; i < action.production.getBody().size(); i++) {
                stack.pop();
                list.add(treeStack.pop());
            }
            Collections.reverse(list);
            stack.push(gotoState(this.stack.peek(), action.production.getHead()));
            treeStack.push(new Tree(action.production, list.toArray(new Tree[]{})));
            return true;
        }
        else if(action.type == Action.ActionType.ACCEPT){
            state = States.State.ACCEPTED;
            return false;
        }
        else if(action.type == Action.ActionType.ERROR){
            state = States.State.ERROR;
            return false;
        }
        else return false;
    }

    public boolean running() {
        if (state == States.State.PROCESSING) return true;
        else return false;
    }

    public boolean accepted() {
        return state == States.State.ACCEPTED;
    }

    private Integer gotoState(int i, Variable a) {
        return gotoMap.get(i).get(a);
    }

    private Map<Variable, Integer> constructGoto(int i) {
        Map<Variable, Integer> gotos = new HashMap<>();
        for (Variable A : G.getNonTerminals()) {
            gotos.put(A, calculateGotoState(i, A));
        }
        return gotos;
    }

    private Integer calculateGotoState(int i, Variable A) {
        Set<Item> Ij = G.itemGoto(states.get(i), A);
        if (states.contains(Ij)) return states.indexOf(Ij);
        else return null;
    }

    private Map<Integer, Map<Variable, Integer>> constructGotoMap() {
        Map<Integer, Map<Variable, Integer>> _gotoMap = new HashMap<>();

        for (int i = 0; i < states.size(); i++) {
            _gotoMap.put(i, constructGoto(i));
        }

        return _gotoMap;
    }

    // Construction for ACTION

    private Map<Variable, Action> constructAction(int i) {
        Map<Variable, Action> actions = new HashMap<>();
        Set<Item> state = states.get(i);
        for (Item item : state) {
            Integer j;
            if (item.production == null && item.nextVar == null) {
                actionMapInsertOnce(actions, new Variable(Variable.Terminal.LINEEND), Action.accept());
            } else if (item.nextVar == null) {
                for (Variable a : G.follow(item.production.getHead())) {
                    actionMapInsertOnce(actions, a, Action.reduce(item.production));
                }
            } else if ((j = calculateGotoState(i, item.nextVar)) != null && item.nextVar.isTerminal) {
                actionMapInsertOnce(actions, item.nextVar, Action.shift(j));
            }
        }
        for (Variable a : G.getTerminals())
            actions.putIfAbsent(a, Action.error());
        actions.putIfAbsent(new Variable(Variable.Terminal.LINEEND), Action.error());
        return actions;
    }

    private static void actionMapInsertOnce(Map<Variable, Action> map, Variable k, Action v) {
        if (map.containsKey(k)) throw new IllegalStateException("Grammar Not LR(0)");
        else map.put(k, v);
    }

    private Map<Integer, Map<Variable, Action>> constructActionMap() {
        Map<Integer, Map<Variable, Action>> _actionMap = new HashMap<>();
        for (int i = 0; i < states.size(); i++)
            _actionMap.put(i, constructAction(i));
        return _actionMap;
    }
}
