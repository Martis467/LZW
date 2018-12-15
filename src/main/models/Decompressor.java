package main.models;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class Decompressor {

    private LzwDictionary dictionary;
    private File inputFile;
    private File outputFile;

    public Decompressor(URL filePathUrl, int dictionarySize) throws URISyntaxException {
        this.inputFile = new File(filePathUrl.toURI());
        this.outputFile = new File(inputFile.getName().substring(0, inputFile.getName().indexOf(".")) + ".decoded");
        this.dictionary = new LzwDictionary(dictionarySize, LzwDictionary.DictionaryMode.Clear);
    }

    public void decompress(){
        String encodedString = "";
        String sequence = "";
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(outputFile));

            //Saving dictionary mode
            outputStream.writeByte((byte)dictionary.getMode().getValue());

            int readByte = 0;
            while (-1 != (readByte = inputStream.read())) {

                String currentByte = String.valueOf((char) readByte);
                encodedString += LzwUtils.convertBitsToBitString((byte)readByte);

                if (encodedString.length() < dictionary.getSize())
                    continue;

                if(dictionary.isFull())
                    dictionary.adjust();


                }

            } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /** private void doUnMagicWithDictionary (Integer currentCode, String sequence, DataOutputStream outputStream) throws IOException {
        // Decompress data, reconstructing dictionary.
            if (currentCode == dictionary.getSize() + 1) {
                //extra case handling
                dictionary.addData(sequence + sequence.substring(0, 1));
            } else if (sequence.length() != 0) {
                dictionary.put(dictionary.size() + 1, sequence + dictionary.get(currentCode).substring(0, 1));
            }
        }*/
}
