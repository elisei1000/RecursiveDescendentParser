import domain.Parsing.Parser;
import domain.Parsing.RecursiveDescendentParser;
import domain.Parsing.exceptions.ParserException;

import java.io.IOException;

/**
 * Created by elisei on 16.12.2017.
 */
public class Main {

    public static void main(String[] args){
        Parser parser = new RecursiveDescendentParser();

        try {
            parser.readGrammar("ourHomework.txt");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }


    }
}
