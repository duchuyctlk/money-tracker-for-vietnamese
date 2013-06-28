package com.duchuyctlk.moneytracker.service;

import java.util.Vector;

public class Utilities {

    public static String[] Split(String splitStr, String delimiter) {
        String[] splitArray = null;
        StringBuffer token = new StringBuffer();
        Vector tokens = new Vector();
        // split
        char[] chars = splitStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (delimiter.indexOf(chars[i]) != -1) {
                // we bumbed into a delimiter
                if (token.length() > 0) {
                    tokens.addElement(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(chars[i]);
            }
        }
        // don't forget the "tail"...
        if (token.length() > 0) {
            tokens.addElement(token.toString());
        }
        // convert the vector into an array
        splitArray = new String[tokens.size()];
        for (int i = 0; i < splitArray.length; i++) {
            splitArray[i] = (String) tokens.elementAt(i);
        }
        return splitArray;
    }

    public static String deleteChar(String source, char c) {
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<source.length(); i++)
        {
            if (source.charAt(i) != c)
                buffer.append(source.charAt(i));
        }
        return buffer.toString();
    }
}
