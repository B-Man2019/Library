package org.shoebob.library.data;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class SearchAndSortUtils {
    public static int calcLevenshteinDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        return LevenshteinDistance.getDefaultInstance().apply(a, b);
    }
}
