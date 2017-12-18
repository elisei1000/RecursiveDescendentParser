package domain.Parsing;

import domain.Languages.*;

import java.util.*;

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
        public Configuration(NonTerminal s){
            poz = 0;
            state = CONFIGURATION_STATE.NORMAL;
            alpha = new Stack<>();
            beta = new Stack<>();
            beta.add(s);
        }
    }

    public RecursiveDescendentParser(){
        super();
    }

    @Override
    public boolean canApplyMethod() {

        for(NonTerminal parent : grammar.getNonTerminals()){
            LinkedList<NonTerminal> queue = new LinkedList<>();
            HashSet<NonTerminal> visited = new HashSet<>();
            boolean recursive = false;
            visited.add(parent);
            queue.add(parent);

            while(!recursive && queue.size() > 0){
                NonTerminal firstQueueNonTerminal = queue.pop();
                for(Production prod: firstQueueNonTerminal.getProductionList()){
                    Element firstElement = prod.getElementList().get(0);
                    if(firstElement instanceof NonTerminal){
                        NonTerminal firstNonTerminal = (NonTerminal) firstElement;
                        if(firstNonTerminal == parent){
                            recursive = true;
                        }
                        else{
                            if(!visited.contains(firstNonTerminal)){
                                visited.add(firstNonTerminal);
                                queue.add(firstNonTerminal);
                            }
                        }
                    }
                }
            }
            if(recursive) return false;
        }
        return true;
    }

    @Override
    protected String scanSequence(List<Element> input) {
        Configuration configuration = new Configuration(grammar.getNonTerminal("S"));

        while(configuration.state != CONFIGURATION_STATE.SUCCESS && configuration.state != CONFIGURATION_STATE.ERROR){
            switch (configuration.state){
                case NORMAL:
                {
                    if(configuration.poz == input.size() && configuration.beta.empty())
                        configuration.state = CONFIGURATION_STATE.SUCCESS;
                    else{
                        if(configuration.beta.empty() || configuration.poz == input.size()){
                            configuration.state = CONFIGURATION_STATE.BACK;
                        }
                        else{
                            Element element = configuration.beta.peek();
                            if(element instanceof NonTerminal){//expandare
                                configuration.beta.pop();
                                NonTerminal nonTerminal = (NonTerminal) element;
                                configuration.alpha.add(new NumberedNonTerminal(nonTerminal.getValue(), 0));
                                new LinkedList<>(nonTerminal.getProductionList().get(0).getElementList()).descendingIterator()
                                        .forEachRemaining(configuration.beta::add);
                            }
                            else{
                                Terminal terminal = (Terminal) element;
                                if(terminal.equals(input.get(configuration.poz))){
                                    //
                                    // avans
                                    //
                                    configuration.beta.pop();
                                    configuration.poz ++;
                                    configuration.alpha.push(element);
                                }
                                else{//insucces de moment
                                    configuration.state = CONFIGURATION_STATE.BACK;
                                }
                            }
                        }
                    }
                    break;
                }
                case BACK:
                {
                    Element element = configuration.alpha.peek();
                    if(element instanceof Terminal)//revenire
                    {
                        configuration.poz -= 1;
                        configuration.beta.push(configuration.alpha.pop());
                    }
                    else {
                        //
                        // alta incercare
                        //
                        NumberedNonTerminal numberedNonTerminal = (NumberedNonTerminal) element;


                        int j = numberedNonTerminal.getNr();

                        List<Production> nonTerminalProductions =
                                grammar.getNonTerminal(numberedNonTerminal.getValue()).getProductionList();

                        //
                        // removing old propduction
                        //
                        Production oldProduction = nonTerminalProductions.get(
                                numberedNonTerminal.getNr()
                        );
                        for (Element oldProductionElement : oldProduction.getElementList()) {
                            configuration.beta.pop();
                        }


                        if (j >= nonTerminalProductions.size() - 1) {
                            if(configuration.poz == 0 && numberedNonTerminal.equals(grammar.getNonTerminal("S")))
                                configuration.state = CONFIGURATION_STATE.ERROR;
                            else {
                                configuration.alpha.pop();
                                configuration.beta.push(grammar.getNonTerminal(numberedNonTerminal.getValue()));
                            }
                        } else {
                            j += 1;
                            numberedNonTerminal.setNr(j);
                            Production production = nonTerminalProductions.get(j);
                            // punem gamma j+1 in beta
                            new LinkedList<>(production.getElementList()).descendingIterator()
                                    .forEachRemaining(configuration.beta::add);
                            configuration.state = CONFIGURATION_STATE.NORMAL;
                        }


                    }
                }
            }
        }
        if( configuration.state == CONFIGURATION_STATE.ERROR)
            return "";

        List<String> strings = new ArrayList<>();
        strings.add("<S>");
        List<Element> alphaSet = new ArrayList<>();
        alphaSet.addAll(configuration.alpha);
        for(Element e : alphaSet){
            if(e instanceof NumberedNonTerminal) {
                NumberedNonTerminal numberedNonTerminal = (NumberedNonTerminal) e;
                String newString = strings.get(strings.size() - 1);
                List<String> builder = new ArrayList<>();
                for (Element productionElement :
                        grammar.getNonTerminal(numberedNonTerminal.getValue())
                        .getProductionList()
                        .get(numberedNonTerminal.getNr())
                        .getElementList())
                {
                    if(productionElement instanceof Terminal)
                        builder.add(((Terminal) productionElement).getValue());
                    else
                    {
                        NonTerminal nonTerminal = (NonTerminal) productionElement;
                        builder.add(
                                String.format("<%s>", nonTerminal.getValue())
                        );
                    }
                }
                newString = newString.replaceFirst(
                        String.format("<%s>", numberedNonTerminal.getValue()),
                        String.join(" ", builder)
                );
                strings.add(newString);
            }
        }
        return String.join(" =>\n ", strings);
    }

}
