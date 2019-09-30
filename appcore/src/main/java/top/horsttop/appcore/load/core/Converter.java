package top.horsttop.appcore.load.core;


import top.horsttop.appcore.load.callback.Callback;

/**
 * Created by horsttop on 2016/04/10.
 */
public interface Converter<T> {
   Class<?extends Callback> map(T t);
}
