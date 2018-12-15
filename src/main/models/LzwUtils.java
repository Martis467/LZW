package main.models;

public class LzwUtils {

    /**
     * Returns integer as a hex string at least two characters long.
     * @param data  the integer to convert
     * @return      the specified integer as a hex string
     */
    public static String toHex(int data) {
        StringBuilder strBuilder  = new StringBuilder(Integer.toHexString(data));
        while (strBuilder.length() < 2) {
            strBuilder.insert(0, '0');
        }
        return strBuilder.toString();
    }

    public static String convertBitsToBitString(byte[] byteBuffer, int size) {
        StringBuilder bitBufferStr = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            bitBufferStr.append(convertByteToBitString(byteBuffer[i]));
        }
        return bitBufferStr.toString();
    }

    public static String convertByteToBitString(byte b){
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    public static String convertBitsToBitString(byte byteBuffer) {
        return String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0');
    }

    public static int convertBitStringToValue(String encodedString, int currentNumberLen) {
        if (encodedString.length() < currentNumberLen) {
            return -1;
        }  // not perfect but will do, we don't expect big int numbers

        String temp = encodedString.substring(0, currentNumberLen);
        encodedString = encodedString.substring(currentNumberLen);
        return Integer.parseInt(temp, 2);
    }
}
