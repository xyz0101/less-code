package com.jenkin.proxy.server.utils;

/**
 * @author jenkin
 * @className HeaderEndCounter
 * @description TODO
 * @date 2021/4/6 14:22
 */
public class HttpsResponseEndCounter  implements ByteEndCounter {
    byte[] bytes = new byte[]{21,3};
    @Override
    public boolean countCase(byte o) {
        return 3==o||21==o ;
    }

    /**
     * 计数器计数的条件
     *
     * @param index
     * @param c
     * @return
     */
    @Override
    public boolean countCaseIndex(int index ,byte c) {
        return bytes[index]==c;
    }

    /**
     * 计数器最大的计数数目，达到此条件会终止计数
     *
     * @return
     */
    @Override
    public int getEndCount() {
        return 2;
    }
}
