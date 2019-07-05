package code.copyfile.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess"})
public class V {
    public static <T> boolean empty(T o) {
        return null == o;
    }

    public static <T> boolean noEmpty(T o) {
        return !empty(o);
    }

    public static <T> boolean empty(T[] o) {
        return null == o || 0 == o.length;
    }

    public static <T> boolean noEmpty(T[] o) {
        return !empty(o);
    }


    public static boolean empty(Collection o) {
        return null == o || 0 == o.size();
    }

    public static boolean noEmpty(Collection o) {
        return !empty(o);
    }

    public static boolean empty(CharSequence s) {
        return null == s || 0 == s.length();
    }

    public static boolean noEmpty(CharSequence s) {
        return !empty(s);
    }

    public static boolean empty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean noEmpty(Map map) {
        return !empty(map);
    }

    //流支持
    public static <T, R> R noEmpty(Collection<T> o, Function<Collection<T>, R> consumer) {
        boolean b = !empty(o);
        if (b) {
            return consumer.apply(o);
        }
        return null;
    }

    //流支持
    public static <T, R> R noEmpty(CharSequence o, Function<CharSequence, R> consumer) {
        boolean b = !empty(o);
        if (b) {
            return consumer.apply(o);
        }
        return null;
    }

    //流支持
    public static <T, R> R noEmpty(T[] o, Function<T[], R> function) {
        boolean b = !empty(o);
        if (b) {
            return function.apply(o);
        }
        return null;
    }

    //流支持
    public static <T, R> R noEmpty(T o, Function<T, R> function) {
        boolean b = !empty(o);
        if (b) {
            return function.apply(o);
        }
        return null;
    }

    public static boolean isTrue(Boolean b) {
        return Boolean.TRUE.equals(b);
    }

    /**
     * 取第一个不为空对象
     */
    @SafeVarargs
    public static <T> T or(T... list) {
        return Arrays.stream(list).filter(Objects::nonNull).findAny().orElse(null);
    }

    public static <T, F> F and(T t, Function<T, F> fun) {
        if (null != t) {
            return fun.apply(t);
        }
        return null;
    }
}
