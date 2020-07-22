package me.illusion.configgui.util;

import java.util.Comparator;

public class StringComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        if(o1.equalsIgnoreCase(o2))
            return 0;

        for(int i = 0; i < o1.length(); i++) {
            char c = o1.charAt(i);

            if(o2.length() <= i)
                return 1;

            char c2 = o2.charAt(i);

            if(c == c2)
                continue;
            if(Character.toUpperCase(c) == c2)
                return 1;
            break;
        }
        return -1;
    }
}