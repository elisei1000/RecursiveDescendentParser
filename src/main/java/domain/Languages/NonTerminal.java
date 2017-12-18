package domain.Languages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisei on 13.11.2017.
 */
public class NonTerminal extends Element {
    private String value;
    private List<Production> productionList;

    public NonTerminal(String value){
        this.value = value;
        this.productionList = new ArrayList<Production>();
    }

    void addProduction(Production p){
        this.productionList.add(p);
    }

    public List<Production> getProductionList(){
        return productionList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return String.format("%s(%s)", "NonTerminal", value);
    }

    @Override
    public boolean equals(Object o){
        if(o!= null && o instanceof NonTerminal)
            return ((NonTerminal) o).getValue().equals(this.value);
        return false;
    }
}
