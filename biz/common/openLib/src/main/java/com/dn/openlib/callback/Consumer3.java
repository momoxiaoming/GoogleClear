package com.dn.openlib.callback;

/**
 * Consumer3
 *
 * @author mmxm
 * @date 2022/1/5 10:57
 */
@FunctionalInterface
public interface Consumer3<T, O, P> {

    void accept(T t, O o, P p);

}
