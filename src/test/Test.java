package test;

import main.models.Decode;
import main.models.Encode;
import main.models.LzwUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

public class Test {

    private Encode encoder;
    private Decode decoder;
    private File file = new File("testFile.txt");
    private File encodedFile = new File("testFile.txt.encoded");
    private File decodedFile = new File("testFileDecoded.txt");

    public Test() {}

    public static void main(String[] args) throws IOException, URISyntaxException {
        Test test = new Test();
        test.runAllTests();
    }

    public void compareFilesWithGivenContent(String fileContent) throws IOException, URISyntaxException {
        FileOutputStream writer = new FileOutputStream(new File(file.getName()));

        writer.write(fileContent.getBytes());
        writer.close();

        encoder.encode();
        decoder.decode(encodedFile.toURL(), decodedFile.toURL());
        byte[] f1 = Files.readAllBytes(file.toPath());
        byte[] f2 = Files.readAllBytes(decodedFile.toPath());

        if(!Arrays.equals(f1, f2))
        {
          System.out.println("failed");
         // throw new IOException();
        }
        //assert (Arrays.equals(f1, f2));
    }

    public void runAllTests() {

        for(LzwUtils.DictionaryMode dictMode : LzwUtils.DictionaryMode.values()){
            for(int maxNumLen = 9; maxNumLen< 26; ++maxNumLen){
                runTestCases(maxNumLen, dictMode);
            }
        }

    }

    private void runTestCases(int maxNumberLen, LzwUtils.DictionaryMode mode){
        try {
            encoder = new Encode(file.toURL(),maxNumberLen, mode);
            decoder = new Decode();
            compareFilesWithGivenContent("asd\0eeesf");
            compareFilesWithGivenContent("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab");
            compareFilesWithGivenContent("THIS IS TEST STRING agajgnadjkfnejwafgeg THIS IS TEST STRING agajgnadjkfnejwafgeg THIS IS TEST STRING agajgnadjkfnejwafgeg THIS IS TEST STRING agajgnadjkfnejwafgeg");
            compareFilesWithGivenContent("a");
            compareFilesWithGivenContent("csdbtnyjukjgtrfasdefgtry");
            compareFilesWithGivenContent("add your test case here");
            compareFilesWithGivenContent("ab");
            compareFilesWithGivenContent("afsf");
            compareFilesWithGivenContent("THIS IS TEST STRING THIS IS TEST STRING THIS IS TEST STRING THIS IS TEST STRING THIS IS TEST STRING ");
            compareFilesWithGivenContent("TOBEORNOTTOBEORTOBEORNOT");

            int len = 5000;
            StringBuilder longString = new StringBuilder(len);
            for(int i = 0; i < len; i++)
                longString.append((char)i);

            compareFilesWithGivenContent(longString.toString());
            //get rid of that
            file.delete();
            encodedFile.delete();
            decodedFile.delete();

        }catch (IOException e)
        {

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
