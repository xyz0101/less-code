package com.jenkin.proxy.server.utils;

/**
 * @author jenkin
 * @className EndCounter
 * @description TODO
 * @date 2021/4/6 14:19
 */
public interface  ByteEndCounter  {

    /**
     * 计数器计数的条件
     * @param t
     * @return
     */
    boolean countCase(byte t);
    /**
     * 计数器计数的条件
     * @param t
     * @return
     */
    boolean countCaseIndex(int index,byte c);
    /**
     * 计数器最大的计数数目，达到此条件会终止计数
     * @return
     */
    int getEndCount();




}
