package test;

import main.models.Compressor;
import main.models.LzwDictionary;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class Tests {
    private static File file = new File("testFile.txt");
    private static File encodedFile = new File("testFile.txt.encoded");


    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        file = new File("testFile.txt");
        Compressor compressor = new Compressor(file, 9, LzwDictionary.DictionaryMode.Clear);
        compressor.compress();
    }

}
