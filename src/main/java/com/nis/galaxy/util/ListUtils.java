package com.nis.galaxy.util;

import java.util.Collections;
import java.util.List;

public class ListUtils {
    public static boolean isEmptyList(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        list.removeAll(Collections.singleton(null));
        if (list.isEmpty()) {
            return true;
        }
        return false;
    }
}
