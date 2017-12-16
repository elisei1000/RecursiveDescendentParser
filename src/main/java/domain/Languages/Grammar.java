package domain.Languages;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.io.InvalidObjectException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by elisei on 13.11.2017.
 */
public class Grammar {

    private Map<String, NonTerminal> nonTerminalMap;
    private Map<String, Terminal> terminalMap;
    private List<Production> productionList;

    public Grammar() {
        this.nonTerminalMap = new HashMap<>();
        this.terminalMap = new HashMap<>();
        this.productionList = new ArrayList<>();
    }


    public void addProduction(Production p) {
        NonTerminal nonTerminal = p.getParent();
        nonTerminal.addProduction(p);
        productionList.add(p);
    }



    public NonTerminal getNonTerminal(String value) {
        return nonTerminalMap.computeIfAbsent(value, k -> new NonTerminal(value));
    }

    public Terminal getTerminal(String value) {
        return terminalMap.computeIfAbsent(value, k -> new Terminal(value));
    }

    public Collection<Terminal> getTerminals() {
        return terminalMap.values();
    }

    public Collection<NonTerminal> getNonTerminals() {
        return nonTerminalMap.values();
    }

    public List<Production> getProductions() {
        return productionList;
    }

    public List<Production> getProductionsFor(String value) {
        return productionList.stream().filter(production -> production.getParent().getValue().equals(value))
                .collect(Collectors.toList());
    }

    public boolean isValid() {
        return nonTerminalMap.containsKey("S");
    }

    public boolean isRegular() {
        if (!isValid())
            return false;

        boolean hasSEmpty = false;
        for (Production p : getProductionsFor("S")) {
            if (p.getElementList().size() == 1) {
                if (p.getElementList().get(0).getClass() == Terminal.class) {
                    Terminal t = (Terminal) p.getElementList().get(0);
                    if (t.isEmpty())
                        hasSEmpty = true;
                }
            }

        }

        for (Production p : productionList) {
            List<Element> elements = p.getElementList();
            if (elements.size() > 2 || elements.size() == 0)
                return false;


            if (elements.get(0).getClass() == NonTerminal.class)
                return false;

            if(elements.get(0).getClass() == Terminal.class){
                Terminal c = (Terminal) elements.get(0);
                if(c.getValue() == null){
                    if(elements.size() == 2)
                        return false;
                    if(!p.getParent().getValue().equals("S"))
                        return false;
                }

            }

            if (elements.size() == 2) {
                if (elements.get(1).getClass() == Terminal.class)
                    return false;
                NonTerminal nonTerminal = (NonTerminal) elements.get(1);
                if (hasSEmpty && nonTerminal.getValue().equals("S"))
                    return false;

            }
        }
        return true;
    }

}

