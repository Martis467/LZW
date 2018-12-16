package main.models;

import main.models.LzwUtils;

import java.io.*;
import java.util.HashMap;

public class Decode {
    private HashMap<Integer, String> dictionary = new HashMap<>();
    private int maxNumberLen;
    private int maxDictSize;
    private String sequence = "";
    private String encodedString = "";
    private int currentNumberLen;
    private LzwUtils.DictionaryMode dictionaryMode;   // 0 - grow infinite, 1 - clear, 2 - full and keep using full
    private boolean fullAndDontAddIt = false;

    public Decode(int maxNumberLen) {
        this.maxNumberLen = maxNumberLen;
        this.dictionaryMode = LzwUtils.DictionaryMode.Clear;
        maxDictSize =(int) Math.pow(2.0,(float) this.maxNumberLen);
        currentNumberLen = maxNumberLen == 8 ? 8 : 9;
    }

    public void resetDefaults(){
        dictionary = new HashMap<>();
        sequence = "";
        encodedString = "";
        fullAndDontAddIt = false;
        maxDictSize =(int) Math.pow(2.0,(float) maxNumberLen);
        currentNumberLen = maxNumberLen == 8 ? 8 : 9;
    }

    public void decode(String fileToDecode, String outputFile) {
        resetDefaults();
        File file = new File(fileToDecode);
        initDictionary();
        try (FileInputStream fs = new FileInputStream(file)) {
            FileOutputStream out = new FileOutputStream(new File(outputFile));
            DataOutputStream outputStream = new DataOutputStream(out);
            int readByte = fs.read();
            dictionaryMode = LzwUtils.DictionaryMode.fromDictionaryValue(readByte);
            while (-1 != (readByte = fs.read())) {
                encodedString += convertBitsToBitString ((byte)readByte);
                if (encodedString.length() < currentNumberLen)
                    continue;

                checkDictionary();
                doUnMagicWithDictionary (convertBitStringToValue(), outputStream);
            }
      outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initDictionary() {
        for (int i = 0; i < 256; i++) {
            dictionary.put(i, String.valueOf((char) i));
        }
    }

    private void checkDictionary() {
        if (dictionary.size() == maxDictSize-1) {

            if (dictionaryMode == LzwUtils.DictionaryMode.Infinite) {
                //increase size and word len
                maxNumberLen++;
                maxDictSize =(int) Math.pow(2.0,(float) this.maxNumberLen);
            } else if (dictionaryMode == LzwUtils.DictionaryMode.Clear) {
                dictionary = new HashMap<>();
                initDictionary();
            } else if (dictionaryMode == LzwUtils.DictionaryMode.Continue) {
                fullAndDontAddIt = true;
            }
        }
        //check whether we need to increase code word lenght
        if (dictionary.size() == Math.pow(2,currentNumberLen) -1 && currentNumberLen < maxNumberLen){
            currentNumberLen++;
        }
    }

    private void doUnMagicWithDictionary (Integer currentCode, DataOutputStream outputStream) throws IOException {
        // Decompress data, reconstructing dictionary.
        if(!fullAndDontAddIt && dictionary.size() < maxDictSize) {
            if (currentCode == dictionary.size() + 1) {
                //extra case handling
                dictionary.put(dictionary.size() + 1, sequence + sequence.substring(0, 1));
            } else if (sequence.length() != 0) {
                dictionary.put(dictionary.size() + 1, sequence + dictionary.get(currentCode).substring(0, 1));
            }
        }

        // Write the code for the last sequence that was present to output.
        outputStream.writeBytes(dictionary.get(currentCode));

        // Start the sequence afresh with the new byte string.
        sequence = dictionary.get(currentCode);
    }

    private int convertBitStringToValue() {
        if (encodedString.length() < currentNumberLen) {
            return -1;
        }  // not perfect but will do, we don't expect big int numbers

        String temp = encodedString.substring(0, currentNumberLen);
        encodedString = encodedString.substring(currentNumberLen);
        return Integer.parseInt(temp, 2);
    }

    private String convertBitsToBitString(byte byteBuffer) {
        return String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0');
    }
}
