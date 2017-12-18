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
            parser.readGrammar(
                    "ourHomework.txt"
                    //"course"
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }

        System.out.println(parser.canApplyMethod());

        try {
            System.out.println(parser.scanSequence("c"));;

            System.out.println(parser.scanSequenceFromFile("file"));
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
