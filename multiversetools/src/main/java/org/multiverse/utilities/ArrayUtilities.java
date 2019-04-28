package org.multiverse.utilities;

import java.util.ArrayList;

public class ArrayUtilities {
    /**
     * Merges the specified arraylists and returns the merged list
     * @param list1
     * @param list2
     */
    public static ArrayList<String> mergeLists(ArrayList<String> list1, ArrayList<String> list2) {
        ArrayList<String> res = new ArrayList<>();

        for(String s : list1) {
            res.add(s);
        }
        for(String s : list2) {
            res.add(s);
        }

        return res;
    }

    public static void displayArray(String[] array) {
        System.out.println("[Neuron.MT.StringUtilities]: Displaying a string array...");
        for (String s : array) {
            System.out.print(s + ", ");
        }
    }

    public static void displayArray(ArrayList<String> arrayList) {
        System.out.println("[Neuron.MT.StringUtilities]: Displaying an arraylist...");
        for(String s : arrayList) {
            System.out.println("[Neuron]:   " + s);
        }
        System.out.println("");
    }
}
