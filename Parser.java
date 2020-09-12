package compiler;

import java.io.*;
import java.util.*;

public class Parser{

   private int lookahead;
   private LinkedHashMap<Variable, List<Object>> productionRules;

   public Parser() throws IOException{
       lookahead = System.in.read();
       productionRules = setRules();
   }

   public LinkedHashMap<Variable, List<Object>> setRules(){
       LinkedHashMap<Variable, List<Object>> productionRules = new LinkedHashMap<>();
       productionRules.put(Variable.Calc, List.of(Variable.Sum));
       productionRules.put(Variable.Sum, List.of(Variable.Sum, "+", Variable.Sub));
       productionRules.put(Variable.Sum, List.of(Variable.Sum));
       productionRules.put(Variable.Sub, List.of(Variable.Sub, "-", Variable.Mult));
       productionRules.put(Variable.Sub, List.of(Variable.Mult));
       productionRules.put(Variable.Mult, List.of(Variable.Mult, "*", Variable.Cosine));
       productionRules.put(Variable.Cosine, List.of("cos", Variable.Cosine, "+", Variable.Fact));
       productionRules.put(Variable.Fact, List.of(Variable.SDN));
       return productionRules;
   }

   /*
   public void parseExpression(Lexer lexer){
       Token token = lexer
   }
   */

    public LinkedHashSet<LinkedHashMap<Variable, List<Object>>> closure(){
        LinkedHashSet<LinkedHashMap<Variable, List<Object>>> closedSet = new LinkedHashSet<>();
        LinkedHashMap<Variable, List<Object>> firstRule = new LinkedHashMap<>();
        firstRule.put(Variable.Calc, productionRules.get(Variable.Calc));
        closedSet.add(firstRule);
        boolean addedNone = false;
        while(!addedNone){
            for (LinkedHashMap<Variable, List<Object>> rule_: closedSet){
                for (Map.Entry<Variable, List<Object>> rule: rule_.entrySet()){
                    Variable A = rule.getKey();
                    List<Object> B = rule.getValue();
                    boolean addedInRound = false;
                    for (Object item: B){
                        if(item.getClass().isEnum() && productionRules.containsKey(item)){
                            LinkedHashMap<Variable, List<Object>> ruleToAdd = new LinkedHashMap<>();
                            ruleToAdd.put((Variable) item, productionRules.get(item));
                            if(!closedSet.contains(ruleToAdd)){
                                closedSet.add(ruleToAdd);
                                addedInRound = true;
                            }
                        }
                    }
                    if(!addedInRound) addedNone = true;
                }
            }
        }
        return closedSet;
    }

    public List<List<Object>> goTo(Variable X){
        LinkedHashSet<LinkedHashMap<Variable, List<Object>>> closedSet = closure();
        List<List<Object>> transitionList = new ArrayList<>();
        for (LinkedHashMap<Variable, List<Object>> map: closedSet){
            for (Map.Entry<Variable, List<Object>> rule: map.entrySet()){
                if(rule.getKey() == X && transitionList.contains(rule.getValue())){
                    transitionList.add(rule.getValue());
                }
            }
        }
        return transitionList;
    }

    public HashMap<Variable, List<List<Object>>> goToForAll(){
        HashMap<Variable, List<List<Object>>> transitionsForEach = new HashMap<>();
        for (Variable v: Variable.values()){
            List<List<Object>> perTransitionList = goTo(v);
            transitionsForEach.put(v, perTransitionList);
        }
        return transitionsForEach;
    }
}