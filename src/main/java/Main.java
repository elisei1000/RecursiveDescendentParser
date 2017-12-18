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

        try{
            parser.readGrammar("course");
            System.out.println("Read course grammar ok.");
            if(parser.canApplyMethod()){
                System.out.println("Can apply method!");
                System.out.println(parser.scanSequence("a c c"));
            }
            else{
                System.out.println("Cannot apply parser method!");
            }
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            parser.readGrammar(
                    "ourHomework.txt"
            );
            System.out.println("Read my grammar ok. ");
            if(parser.canApplyMethod()){
                System.out.println("Can apply method: True");
                System.out.println(parser.scanSequenceFromFile("file"));
            }
            else{
                System.out.println("Cannot apply method.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }
}
