package com.org.openlib.help;

/**
 * Consumer3
 *
 * @author mmxm
 * @date 2022/7/5 19:48
 */
@FunctionalInterface
public interface Consumer3<T, O, P> {

    void accept(T t, O o, P p);

}
