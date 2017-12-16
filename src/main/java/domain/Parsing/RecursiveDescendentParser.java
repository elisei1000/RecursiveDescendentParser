package domain.Parsing;

import domain.Languages.Element;
import domain.Languages.Grammar;
import domain.Languages.NonTerminal;
import domain.Languages.Production;
import domain.Parsing.exceptions.ParserException;
import util.FileUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by elisei on 16.12.2017.
 */
public class RecursiveDescendentParser extends Parser {

    private class NumberedNonTerminal extends NonTerminal{
        private int nr;

        public NumberedNonTerminal(String value, int nr) {
            super(value);
            this.nr = nr;
        }

        public int getNr(){
            return this.nr;
        }

        public void setNr(int nr){
            this.nr = nr;
        }

        public Production getNextProduction(){
            if(nr >= getProductionList().size())
                return null;
            return getProductionList().get(nr++);
        }
    }

    private enum CONFIGURATION_STATE{
        NORMAL, BACK, SUCCESS, ERROR
    }

    private class Configuration{
        int poz;
        CONFIGURATION_STATE state;
        Stack<Element> alpha;
        Stack<Element> beta;
        public Configuration(){
            poz = 1;
            state = CONFIGURATION_STATE.NORMAL;
            alpha = new Stack<>();
            beta = new Stack<>();
        }
    }


    private Configuration configuration;

    public RecursiveDescendentParser(){
        super();
        configuration = new Configuration();
    }

    @Override
    public boolean canApplyMethod() {
        for(Production production : grammar.getProductions()){
            NonTerminal nonTerminal = production.getParent();

            Element firstElement = production.getElementList().get(0);
            if(firstElement instanceof NonTerminal){

            }
        }
        return true;
    }

    @Override
    protected void scanSequence(List<Element> input) {

    }

}
