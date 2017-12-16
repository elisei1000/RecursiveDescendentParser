package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by elisei on 16.12.2017.
 */
public class FileUtils {

    public static String readFile(String filename) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

        String line = null;
        while((line = bufferedReader.readLine()) != null){
            builder.append(line).append("\n");
        }

        return builder.toString();
    }
}
