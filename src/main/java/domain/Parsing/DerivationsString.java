package domain.Parsing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisei on 16.12.2017.
 */
public class DerivationsString {

    private List<String> list;

    public DerivationsString(){
        this.list = new ArrayList();
    }

    public void addDerivation(String derivation){
        this.list.add(derivation);
    }

    public List<String> getDerivations(){
        return list;
    }

    public String getDerivation(int index){
        return list.get(index);
    }
}
