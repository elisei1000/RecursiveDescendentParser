package domain.Languages;

/**
 * Created by elisei on 13.11.2017.
 */
public class Terminal extends Element {
    private String value;
    public Terminal(String value){
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public boolean isEmpty(){
        return value == null;
    }

    @Override
    public String toString(){
        return String.format("%s(%s)", "Terminal",
                (value == null)?"empty":value);
    }

    @Override
    public boolean equals(Object c){
        if(c instanceof Terminal){
            return ((Terminal) c).value.equals(this.value);
        }
        return false;
    }

}
