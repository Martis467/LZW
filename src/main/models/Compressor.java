package main.models;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;


public class Compressor {

    private LzwDictionary dictionary;
    private File inputFile;
    private File outputFile;

    public Compressor(File filePathUrl, int dictionarySize, LzwDictionary.DictionaryMode mode) throws URISyntaxException {
        this.inputFile = new File(filePathUrl.toURI());
        this.outputFile = new File(inputFile.getName() + ".Encoded");
        this.dictionary = new LzwDictionary(dictionarySize, mode);
    }

    /**
     * Returns the specified 16-bit integer as a byte array.
     * @param value the 16-bit integer to convert
     * @return      the specified 16-bit integer as a byte array
     */
    private byte[] shortToByteArray(short value) {
        return ByteBuffer.allocate(2).putShort(value).array();
    }

    /**
     * Returns the specified byte array as a 16-bit integer.
     * @param data  the byte array integer to convert
     * @return      the specified byte array integer as a 16-bit integer
     */
    private short byteArrayToShort(byte[] data) {
        return ByteBuffer.allocate(2).put(data).getShort(0);
    }

    /**
     * Converts a hexadecimal string to a byte array and returns it.
     * @param str   the hexadecimal string to convert
     * @return      a byte array representation of the hexadecimal string
     */
    private byte[] fromHexString(String str) {
        return DatatypeConverter.parseHexBinary(str);
    }

    /**
     * Returns the first byte of a hexadecimal string.
     * @param str   the hexadecimal string
     * @return      the first byte (i.e. first two characters) of the hex string
     */
    private String firstByte(String str) {
        return str.substring(0, 2);
    }

    public void compress()
    {
        String sequence = "";
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(outputFile));

            //Saving dictionary mode
            outputStream.writeByte((byte)dictionary.getMode().getValue());

            int readByte = 0;
            while (-1 != (readByte = inputStream.read())) {
                String currentByte = String.valueOf((char) readByte);

                if(dictionary.isFull())
                    dictionary.adjust();

                String encodedString = "";

                doMagicWithDictionary(currentByte, sequence, encodedString);
                writeDataToOutput(outputStream, encodedString);
            }

            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeDataToOutput(DataOutputStream outputStream, String encodedString) throws IOException {
        for (int i = 0; i < encodedString.length(); i += 8) {
            if (encodedString.length() < i + 8) {
                encodedString = encodedString.substring(i);
                return;
            }
            String bitString = encodedString.substring(i, i + 8);
            byte b = (byte) Integer.parseInt(bitString, 2);
            outputStream.writeByte(b);
        }
    }

    private void doMagicWithDictionary(String currentByte, String sequence, String encodedString) {
        sequence += currentByte;

        if (dictionary.hasSequence(currentByte)) return;

        dictionary.addSequence(sequence);

        // Remove the last byte from the sequence.
        sequence = sequence.substring(0, sequence.length() - 2);

        // convert value of dictionary to compressed bits and add it to result String
        encodedString += dictionary.getEncodedBits(sequence);

        // Start the sequence afresh with the new byte string.
        sequence = currentByte;
    }
}

