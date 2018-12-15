package main.models;

import java.nio.ByteBuffer;
import java.util.HashMap;


public class LzwDictionary {

    private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
    private DictionaryMode mode;
    private int dictionaryMaxSize;
    private int dictionaryCurrentSize;

    /** Maps hexadecimal strings to 16-bit data codes. */
    private final HashMap<String, Short> dataToCodes;

    /** Maps 16-bit data codes to hexadecimal strings. */
    private final HashMap<Short, String> codesToData;

    /**
     * Initialises a new default instance of an LZW compression dictionary.
     */
    public LzwDictionary() {
        dataToCodes = new HashMap<String, Short>();
        codesToData = new HashMap<Short, String>();
        initializedDictionary();
        initDictionary();
    }

    public LzwDictionary(int size, DictionaryMode mode)
    {
        this.dictionaryMaxSize = size;
        this.mode = mode;
        dataToCodes = new HashMap<String, Short>();
        codesToData = new HashMap<Short, String>();
        initializedDictionary();
        initDictionary();
    }

    private void initializedDictionary() {
        for (int i = 0; i < 256; i++)
            addData(LzwUtils.toHex(i));
    }

    private void initDictionary() {
        for (int i = 0; i < 256; i++) {
            dictionary.put(String.valueOf((char) i), i);
        }
        dictionaryCurrentSize = 8;
    }

    /**
     * Adds a hexadecimal string to the end of this dictionary.
     * @param data  the string to add
     */
    public void addData(String data) {
        if (isFull()) {
            throw new RuntimeException("Compression dictionary is full.");
        }
        dataToCodes.put(data, (short) dataToCodes.size());
        codesToData.put((short) codesToData.size(), data);
    }

    /**
     * Gets whether or not the dictionary has an entry with data matching the specified hexadecimal string.
     * @param data  the string to check for
     * @return      true if dictionary has matching data, otherwise false
     */
    public boolean hasData(String data) {
        return dataToCodes.containsKey(data);
    }

    /**
     * Gets the 16-bit encoded integer associated with the specified hexadecimal string.
     * @param data  the string to check for
     * @return      the associated 16-bit encoded integer
     */
    public Short getCode(String data) {
        return dataToCodes.get(data);
    }

    /**
     * Gets the hexadecimal string associated with the specified 16-bit encoded integer.
     * @param value the 16-bit encoded integer to check for
     * @return      the associated hexadecimal string
     */
    public String getData(short value) {
        return codesToData.get(value);
    }

    public int getSize() {
        return dictionary.size();
    }

    public int getCurrentSize() {return dictionaryCurrentSize;}

    public boolean isFull() {
        if(dictionary.size() != dictionaryMaxSize) return false;
        if(mode == DictionaryMode.Continue) return false;
        return true;
    }

    /**
     * Adjusts dictionary based on the chosen mode
     *  Infinite - Expands the dictionary by one bit
     *  Clear - Empties the dictionary
     *  Continue - Literally does nothing
     */
    public void adjust() {
        switch (mode)
        {
            case Infinite:
            case Clear: dictionary.clear(); break;
            case Continue: return;
        }
    }

    public boolean hasSequence(String sq) {
        return dictionary.containsKey(sq);
    }

    public DictionaryMode getMode() {
        return mode;
    }

    public void addSequence(String sequence) {
        dictionary.put(sequence, dictionary.size());
    }

    public String getEncodedBits(String sequence) {
        int val = dictionary.get(sequence);
        byte[] byteInt = ByteBuffer.allocate(4).putInt(val).array();
        String bitString = LzwUtils.convertBitsToBitString(byteInt, byteInt.length);
        return bitString.substring(bitString.length() - dictionaryCurrentSize);
    }

    /**
     * Enum class for diffenret dictionary modes
     */
    public enum DictionaryMode
    {
        Infinite(0),
        Clear(1),
        Continue(2);

        int value;
        DictionaryMode(int value)
        {
            this.value = value;
        }

        public short getValue(){return (short)value;}
    }
}
