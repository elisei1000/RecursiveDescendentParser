package domain.Parsing;

import domain.Languages.*;
import domain.Parsing.exceptions.ParserException;
import util.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by elisei on 16.12.2017.
 */
public abstract class Parser {
    protected Grammar grammar;
    private Pattern nonTerminalPattern = Pattern.compile("[A-Za-z0-9_]+");
    private Pattern terminalPattern = Pattern.compile("[A-Za-z0-9]+");

    public Parser(){
        grammar = new Grammar();
    }

    public abstract boolean canApplyMethod();

    protected abstract void scanSequence(List<Element> input);

    public void scanSequence(String data) throws ParserException{
        String[] parts = data.split(" ");

        List<Element> input = new ArrayList<>();
        for(String part : parts){
            part = part.trim();
            if(!terminalPattern.matcher(part).matches())
                throw new ParserException(String.format(
                        "Invalid terminal: %s",
                        part
                ));

            input.add(new Terminal(part));
        }

        scanSequence(input);
    }

    public void scanSequenceFromFile(String filename) throws IOException, ParserException {
        String data;
        data = FileUtils.readFile(filename);
        scanSequence(data);
    }


    public void readGrammar(String filename) throws  IOException, ParserException{
        String data;
        data = FileUtils.readFile(filename);
        loadGrammar(data);
    }

    private Terminal parseTerminal(String data) {
        Terminal terminal;

        data = data.trim();
        if(!terminalPattern.matcher(data).matches())
            return null;

        return grammar.getTerminal(data);
    }



    private NonTerminal parseNonTerminal(String data){
        NonTerminal nonTerminal;

        data = data.trim();
        if(!data.startsWith("<") || !data.endsWith(">"))
            return null;

        data = data.substring(1, data.length() - 1);
        if(data.contains("<") || data.contains(">"))
            return null;

        if(!nonTerminalPattern.matcher(data).matches())
            return null;

        return grammar.getNonTerminal(data);
    }

    private List<Production> parseProductions(NonTerminal parent, String data) {
        List<Production> productions = new ArrayList<>();
        String[] prodsList = data.split("[|]");

        if(prodsList.length == 0)
            return null;

        for(String prod : prodsList){
            prod = prod.trim();

            String[] parts = prod.split(" ");
            if(parts.length == 0)
                return null;

            Production production = new Production(parent);
            for(String part : parts){
                part = part.trim();

                Element element = parseNonTerminal(part);
                if(element == null){
                    element = parseTerminal(part);
                    if(element == null)
                        return null;
                }
                production.addElement(element);
            }
            productions.add(production);
        }
        return productions;
    }

    private void loadGrammar(String data) throws ParserException {
        String[] lines = data.split("\n");
        int lineNr;

        lineNr = 0;
        grammar = new Grammar();
        try{
            for(String line : lines){
                lineNr+=1;
                if(line.isEmpty())
                    continue;

                String[] parts = line.split("->");
                if(parts.length != 2)
                    throw new ParserException(
                            String.format("Invalid production(s) at line: %d", lineNr));

                NonTerminal parent = parseNonTerminal(parts[0]);
                if(parent == null)
                    throw new ParserException(
                            String.format("Invalid parent of the production(s) at line: %d", lineNr));

                List<Production> productions = parseProductions(parent, parts[1]);
                if(productions == null || productions.size() == 0)
                    throw new ParserException(
                            String.format("Invalid productionsList at line: %d", lineNr)
                    );

                productions.forEach(grammar::addProduction);
            }
        }
        catch (ParserException e){
            grammar = null;
            throw  e;
        }

    }

}
