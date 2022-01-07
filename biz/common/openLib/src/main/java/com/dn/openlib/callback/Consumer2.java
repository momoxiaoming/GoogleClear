package com.dn.openlib.callback;

/**
 * Consumer2
 *
 * @author mmxm
 * @date 2022/1/5 10:58
 */
@FunctionalInterface
public interface Consumer2<T, O> {

    void accept(T t, O o);

}
