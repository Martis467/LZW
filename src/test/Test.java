package test;

import main.models.Decode;
import main.models.Encode;
import main.models.LzwUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class Test {

    private Encode encoder;
    private Decode decoder;
    private File file = new File("testFile.txt");
    private File encodedFile = new File("testFile.txt.encoded");
    private File decodedFile = new File("testFileDecoded.txt");

    public Test()
    {
     //encoder = new Encode(9, LzwUtils.DictionaryMode.Clear);
    // decoder = new Decode(8);
    }


    public void compareFilesWithGivenContent(String fileContent) throws IOException {
        FileOutputStream writer = new FileOutputStream(new File(file.getName()));

        writer.write(fileContent.getBytes());
        writer.close();

        encoder.encode();
        decoder.decode(encodedFile.toString(), decodedFile.toString());
        byte[] f1 = Files.readAllBytes(file.toPath());
        byte[] f2 = Files.readAllBytes(decodedFile.toPath());

        if(!Arrays.equals(f1, f2))
        {
          System.out.println("failed");
         // throw new IOException();
        }
        //assert (Arrays.equals(f1, f2));
    }

    public void runAllTests(){
        try {
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

            int len = 15000;
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

        }
    }

}
