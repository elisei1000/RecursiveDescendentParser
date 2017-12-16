package domain.Languages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisei on 13.11.2017.
 */
public class Production {

    private List<Element> elementList;
    private NonTerminal parent;


    public Production(NonTerminal parent){
        elementList = new ArrayList<>();
        this.parent = parent;
    }



    public void addElement(Element e){
        this.elementList.add(e);
    }

    public List<Element> getElementList() {
        return elementList;
    }

    public NonTerminal getParent() {
        return parent;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        elementList.forEach(element -> stringBuilder.append(element.toString()));
        return String.format("%s->%s", parent.getValue(), stringBuilder.toString());
    }
}
