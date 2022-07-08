package com.org.openlib.help;

/**
 * Consumer2
 *
 * @author mmxm
 * @date 2022/7/5 19:48
 */
@FunctionalInterface
public interface Consumer2<T, O> {

    void accept(T t, O o);

}
