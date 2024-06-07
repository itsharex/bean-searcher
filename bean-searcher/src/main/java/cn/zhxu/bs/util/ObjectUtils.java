package cn.zhxu.bs.util;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectUtils {


    public static Integer toInt(Object value) {
        if (value != null) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            if (value instanceof String) {
                return Integer.valueOf((String) value);
            }
        }
        return null;
    }

    public static Long toLong(Object value) {
        if (value != null) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            if (value instanceof String) {
                return Long.valueOf((String) value);
            }
        }
        return null;
    }

    public static boolean toBoolean(Object value) {
        if (value != null) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            }
            if (value instanceof Number) {
                return ((Number) value).intValue() != 0;
            }
        }
        return false;
    }

    public static String string(Object value) {
        if (value instanceof String) {
            return ((String) value).trim();
        }
        return null;
    }
    
    public static Object firstNotNull(Object[] values) {
        for (Object value: values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public static List<String> toList(Object value) {
        if (value instanceof Collection) {
            Collection<?> list = (Collection<?>) value;
            return list.stream().map(it -> it.toString().trim())
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
        }
        String[] array = null;
        if (value instanceof String) {
            array = ((String) value).split("[^A-Za-z0-9_]");
        }
        if (value instanceof String[]) {
            array = (String[]) value;
        }
        if (array != null) {
            List<String> list = new ArrayList<>();
            for (String str : array) {
                if (StringUtils.isNotBlank(str)) {
                    list.add(str.trim());
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    static final int CASE_DIFF = 'a' - 'A';

    public static void upperCase(Object[] params) {
        for (int i = 0; i < params.length; i++) {
            Object val = params[i];
            if (val instanceof String) {
                params[i] = ((String) val).toUpperCase();
            }
            if (val instanceof Character) {
                char ch = (Character) val;
                if (ch >= 'a' && ch <= 'z') {
                    ch = (char) (ch - CASE_DIFF);
                }
                params[i] = String.valueOf(ch);
            }
        }
    }

}
