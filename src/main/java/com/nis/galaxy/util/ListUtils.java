package com.nis.galaxy.util;

import java.util.List;

public class ListUtils {
    public static boolean isEmptyList(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.remove(null);
        }
        if (list.isEmpty()) {
            return true;
        }
        return false;
    }
}
