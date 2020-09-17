package compiler;

import java.util.*;
import java.util.stream.Collectors;

public class Grammar {

    private Variable startVariable;
    private List<Variable> variables;
    private List<ProductionRule> productionRules;
    private List<Variable> terminals;
    private List<Variable> nonTerminals;

    private HashMap<ProductionRule, List<Item>> itemPerRule;
    private HashMap<Variable, Set<ProductionRule>> rulesPerNonTerminal;

    public static final Grammar grammar = createProductions();

    public Grammar(Variable startVariable, List<ProductionRule> productionRules){
        this.startVariable = startVariable;
        this.productionRules = productionRules;

        List<Variable> terminals_ = new ArrayList<>();
        List<Variable> nonTerminals_ = new ArrayList<>();
        for (Variable.NonTerminal nonTerminal : Variable.NonTerminal.values()){
            Variable potentialTerminal = new Variable(nonTerminal);
            if (potentialTerminal.isTerminal) terminals_.add(potentialTerminal);
            else nonTerminals_.add(potentialTerminal);
        }
        this.variables = List.of(Variable.calc, Variable.sub, Variable.mult,
                Variable.cosine, Variable.fact, Variable.sdn, Variable.plus,
                Variable.minus, Variable.times, Variable.factorial, Variable.cos,
                Variable.lineEnd);
        this.terminals = terminals_;
        this.nonTerminals = nonTerminals_;

        Map<ProductionRule, List<Item>> _itemPerRule = new HashMap<>();
        Map<Variable, Set<ProductionRule>> _rulesPerNonTerminal = new HashMap<>();

        for (ProductionRule p : productionRules) {
            List<Item> items = new ArrayList<>();
            for (int i = 0; i <= p.getBody().size(); i++) {
                items.add(new Item(p, i));
            }
            _itemPerRule.put(p, items);

            _rulesPerNonTerminal.putIfAbsent(p.getHead(), new HashSet<>());
            _rulesPerNonTerminal.get(p.getHead()).add(p);
        }
        List<Item> startList = new ArrayList<Item>() {{
            add(new Item(startVariable, 0));
            add(new Item(startVariable, 1));
        }};
        _itemPerRule.put(null, startList);

        _itemPerRule.replaceAll((k, v) -> Collections.unmodifiableList(v));
        _rulesPerNonTerminal.replaceAll((k, v) -> Collections.unmodifiableSet(v));
        itemPerRule = (HashMap<ProductionRule, List<Item>>) _itemPerRule;
        rulesPerNonTerminal = (HashMap<Variable, Set<ProductionRule>>) _rulesPerNonTerminal;
    }

    public List<ProductionRule> getProductionRules() {
        return productionRules;
    }

    public List<Variable> getTerminals(){
        return terminals;
    }

    public List<Variable> getVariables(){
        return variables;
    }

    public Variable getStartVariable() {
        return startVariable;
    }

    public static Grammar createProductions(){
        List<ProductionRule> productionRules = new ArrayList<>();
        productionRules.add(new ProductionRule(Variable.calc, List.of(Variable.calc, Variable.plus , Variable.sub)));
        productionRules.add(new ProductionRule(Variable.calc, List.of(Variable.sub)));
        productionRules.add(new ProductionRule(Variable.sub, List.of(Variable.sub, Variable.minus, Variable.mult)));
        productionRules.add(new ProductionRule(Variable.sub, List.of(Variable.mult)));
        productionRules.add(new ProductionRule(Variable.mult, List.of(Variable.mult, Variable.times, Variable.cosine)));
        productionRules.add(new ProductionRule(Variable.mult, List.of(Variable.cosine)));
        productionRules.add(new ProductionRule(Variable.cosine, List.of(Variable.cos, Variable.cosine)));
        productionRules.add(new ProductionRule(Variable.cosine, List.of(Variable.fact)));
        productionRules.add(new ProductionRule(Variable.fact, List.of(Variable.fact, Variable.factorial)));
        productionRules.add(new ProductionRule(Variable.fact, List.of(Variable.sdn)));
        //productionRules.add(new ProductionRule(Variable.Value, List.of(Variable.SDN)));
        return new Grammar(Variable.calc, productionRules);
    }

    private Item nextItem(Item item) {
        if (item.nextVar == null) return null;
        else return itemPerRule.get(item.production).get(item.position + 1);
    }

    private Item prevItem(Item item) {
        if (item.sym_prev == null) return null;
        else return itemPerRule.get(item.production).get(item.position - 1);
    }

    Set<Item> closure(Set<Item> I) {
        Set<Item> J = new HashSet<>(I);
        Set<Item> current = new HashSet<>(J);
        Set<Item> previous = new HashSet<>(J);
        boolean doneOnce = false;
        while(current.size() > 0 || doneOnce==false){
            current = new HashSet<>();
            for (Item A : previous) {
                Variable B = A.nextVar;
                if (!rulesPerNonTerminal.containsKey(B)) continue;
                    for (ProductionRule P : rulesPerNonTerminal.get(B)) {
                        Item item = itemPerRule.get(P).get(0);
                        if (!J.contains(item)){
                            current.add(item);
                        }
                    }

            }
            J.addAll(current);
            previous = current;
            doneOnce = true;
        }
        return J;
    }

    Set<Item> itemGoto(Set<Item> I, Variable X) {
        Set<Item> J = I.stream()
                .filter(x -> x.nextVar == X)
                .map(this::nextItem)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return closure(J);
    }

    List<Set<Item>> items() {
        List<Set<Item>> C = new ArrayList<>();
        C.add(closure(new HashSet<Item>() {{
            add(itemPerRule.get(null).get(0));
        }}));
        List<Set<Item>> current = new ArrayList<>(C);
        List<Set<Item>> previous = new ArrayList<>(C);
        boolean doneOnce = false;
        while (current.size() > 0 || doneOnce == false){
            current = new ArrayList<>();
            for (Set<Item> I : previous)
                for (Variable X : variables) {
                    Set<Item> goneTo = itemGoto(I, X);
                    if (!goneTo.isEmpty() && !C.contains(goneTo)) current.add(goneTo);
                }
            for (Set<Item> I : current) {
                if (!C.contains(I)) C.add(I);
            }
            previous = current;
            doneOnce = true;
        }
        return C;
    }

    public HashMap<ProductionRule, List<Item>> getItemPerRule() {
        return itemPerRule;
    }

    public List<Variable> getNonTerminals() {
        return nonTerminals;
    }

    public Map<Variable, Set<ProductionRule>> getRulesPerNonTerminal() {
        return rulesPerNonTerminal;
    }

    private Map<Variable, Set<Variable>> constructFirstMap() {
        Map<Variable, Set<Variable>> _first = new HashMap<>();
        for (Variable X : terminals)
            _first.put(X, Collections.singleton(X));
        for (Variable X : nonTerminals) {
            _first.put(X, new HashSet<>());
        }

        boolean changed;
        do {
            changed = false;
            for (Variable X : nonTerminals) {
                for (ProductionRule p : rulesPerNonTerminal.get(X)) {
                    Variable firstNonEmpty = null;

                        int old = _first.get(X).size();
                        _first.get(X).addAll(_first.get(firstNonEmpty));
                        changed = changed || _first.get(X).size() != old;

                }
            }
        } while (changed);

        _first.replaceAll((k, v) -> Collections.unmodifiableSet(v));
        return Collections.unmodifiableMap(_first);
    }

    private Map<Variable, Set<Variable>> after() {
        Map<Variable, Set<Variable>> _follow = new HashMap<>();
        for (Variable X : nonTerminals)
            _follow.put(X, new HashSet<>());
        _follow.put(startVariable, new HashSet<Variable>() {{
            add(Variable.lineEnd);
        }});

        boolean changed;
        do {
            changed = false;
            for (ProductionRule p : productionRules) {
                for (int i = 0; i < p.getBody().size(); i++) {
                    Variable B = p.getBody().get(i);
                    if (B.isTerminal) continue;

                    int old = _follow.get(B).size();
                    changed = changed || old != _follow.get(B).size();
                }
            }
        } while (changed);

        _follow.replaceAll((k, v) -> Collections.unmodifiableSet(v));
        return Collections.unmodifiableMap(_follow);
    }

    private Map<Variable, Set<Variable>> constructFollowMap() {
        Map<Variable, Set<Variable>> _follow = new HashMap<>();
        for (Variable X : nonTerminals)
            _follow.put(X, new HashSet<>());
        _follow.put(startVariable, new HashSet<Variable>() {{
            add(Variable.lineEnd);
        }});
        _follow.replaceAll((k, v) -> Collections.unmodifiableSet(v));
        return Collections.unmodifiableMap(_follow);
    }

    Set<Variable> follow(Variable X) {
        Map<Variable, Set<Variable>> map = constructFollowMap();
        return map.get(X);
    }


}
