package com.org.openlib.help;

/**
 * Consumer
 *
 * @author mmxm
 * @date 2022/7/5 19:48
 */
@FunctionalInterface
public interface Consumer<T> {

    void accept(T t);

}
